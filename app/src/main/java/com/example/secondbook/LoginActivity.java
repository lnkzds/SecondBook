package com.example.secondbook;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.secondbook.db.Person;
import com.example.secondbook.tool.Tool;

import org.litepal.FluentQuery;
import org.litepal.LitePal;

import java.util.List;

import static com.example.secondbook.tool.Tool.ToastShow;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    TextView back,registered,forget;
    Button login;
    ImageView eye;
    EditText password,account;
    LayoutInflater inflater;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        InitView();
    }

    private void InitView(){
        //初始化控件
        back=findViewById(R.id.back);
        registered=findViewById(R.id.registered);
        forget=findViewById(R.id.forget);
        login=findViewById(R.id.login);
        eye=findViewById(R.id.eye);
        password=findViewById(R.id.password);
        account=findViewById(R.id.account);

        //设置点击事件
        back.setOnClickListener(this);
        registered.setOnClickListener(this);
        forget.setOnClickListener(this);
        login.setOnClickListener(this);
        eye.setOnClickListener(this);

        //密码设置不可见
        password.setInputType(129);
        //字体设置下划线
        registered.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        forget.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        //初始化弹出信息控件
        inflater = getLayoutInflater();

        //初始化数据库
        LitePal.getDatabase();

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.eye:
                //切换可见和不可见
                if(password.getInputType()==128)
                    password.setInputType(129);
                else if(password.getInputType()==129)
                    password.setInputType(128);
                break;
            case R.id.back:
                finish();
                break;
            case R.id.login:
                //输入法隐藏
                InputMethodManager inputMethodManager =(InputMethodManager)getApplicationContext().
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(password.getWindowToken(), 0); //隐藏
                if(TextUtils.isEmpty(account.getText().toString())){
                    ToastShow(inflater,LoginActivity.this,0,"账号不能为空",Gravity.CENTER,0,300,Toast.LENGTH_SHORT);
                }else if(TextUtils.isEmpty(password.getText().toString())){
                    ToastShow(inflater,LoginActivity.this,0,"密码不能为空",Gravity.CENTER,0,300,Toast.LENGTH_SHORT);
                }else{
                    String accoun=account.getText().toString();
                    List<Person> people = LitePal.where("account=?", accoun).find(Person.class);
                    if(people.size()==0){
                        ToastShow(inflater,LoginActivity.this,0,"账号还未注册，请先注册",Gravity.CENTER,0,300,Toast.LENGTH_SHORT);
                    }else{
                        String pass=password.getText().toString();
                        if(!(people.get(0).getPassword().equals(pass))){
                            ToastShow(inflater,LoginActivity.this,0,"密码输入错误!",Gravity.CENTER,0,300,Toast.LENGTH_SHORT);
                        }else{
                            //登录成功 操作
                            ToastShow(inflater,LoginActivity.this,1,"登录成功,请稍候...",Gravity.CENTER,0,300,Toast.LENGTH_SHORT);
                            Intent intent=new Intent();
                            //把账号  名称  图片等信息传回去
                            intent.putExtra("account",accoun);
                            intent.putExtra("name",people.get(0).getName());
                            intent.putExtra("imagepath",people.get(0).getImagepath());

                            setResult(RESULT_OK,intent);
                            SharedPreferences.Editor editor=getSharedPreferences("data",MODE_PRIVATE).edit();
                            editor.putString("status","true");
                            editor.putString("name",people.get(0).getName());
                            editor.putString("imagepath",people.get(0).getImagepath());
                            editor.putString("account",accoun);
                            editor.apply();
                            finish();
                        }
                    }
                }
                break;
            case R.id.registered:
                //打开注册界面
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.forget:
                //打开忘记密码界面
                Intent tent=new Intent(LoginActivity.this,ForgetPasswordActivity.class);
                startActivity(tent);
                break;
            default:
                break;
        }
    }



}
