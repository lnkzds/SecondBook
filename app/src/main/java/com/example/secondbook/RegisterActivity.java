package com.example.secondbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.secondbook.db.Person;

import org.litepal.LitePal;

import java.util.List;

import static com.example.secondbook.tool.Tool.GeneratePassword;
import static com.example.secondbook.tool.Tool.ToastShow;
import static com.example.secondbook.tool.Tool.ToastShowImg;
import static com.example.secondbook.tool.Tool.VerifyAccount;
import static com.example.secondbook.tool.Tool.VerifyName;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    TextView back,getCode;
    EditText phoneNumber,VerificationCode,password,name;
    Button registered;
    ImageView eye;
    RadioButton boy,girl;
    String code;
    LayoutInflater inflater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        InitView();
    }
    private void InitView(){
        //初始化控件
        back=findViewById(R.id.back);
        registered=findViewById(R.id.registered);
        getCode=findViewById(R.id.getCode);
        phoneNumber=findViewById(R.id.mobile);
        VerificationCode=findViewById(R.id.verificationCode);
        password=findViewById(R.id.password);
        name=findViewById(R.id.name);
        eye=findViewById(R.id.eye);
        boy=findViewById(R.id.rb_1);
        girl=findViewById(R.id.rb_2);

        //设置点击事件
        back.setOnClickListener(this);
        registered.setOnClickListener(this);
        getCode.setOnClickListener(this);
        eye.setOnClickListener(this);


        //密码设置不可见
        password.setInputType(129);

        //初始化弹出信息控件
        inflater = getLayoutInflater();
        //验证码
        code=null;

        //数据库
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
            case R.id.registered:
                String phonenumber=phoneNumber.getText().toString();
                String verificationcode=VerificationCode.getText().toString();
                String mima=password.getText().toString();
                String myname=name.getText().toString();
                boolean gender=boy.isChecked()||girl.isChecked();
                if(!VerifyAccount(phonenumber)){
                    ToastShow(inflater,RegisterActivity.this,0,"手机号错误!", Gravity.CENTER,0,400,Toast.LENGTH_SHORT);
                }else if((code==null)||(verificationcode.equals(""))||!(code.equals(verificationcode))){
                    ToastShow(inflater,RegisterActivity.this,0,"验证码错误!", Gravity.CENTER,0,400,Toast.LENGTH_SHORT);
                }else if(mima.equals("")){
                    ToastShow(inflater,RegisterActivity.this,0,"密码不能为空!", Gravity.CENTER,0,400,Toast.LENGTH_SHORT);
                }else if(!VerifyName(myname)){
                    ToastShow(inflater,RegisterActivity.this,0,"名字不符合规范，请重新输入!", Gravity.CENTER,0,400,Toast.LENGTH_SHORT);
                }else if(!gender){
                    ToastShow(inflater,RegisterActivity.this,0,"请选择性别!", Gravity.CENTER,0,400,Toast.LENGTH_SHORT);
                }else{
                    //成功
                    ToastShow(inflater,RegisterActivity.this,1,"注册成功",Gravity.CENTER,0,400,Toast.LENGTH_SHORT);
                    Person person=new Person();
                    person.setAccount(phonenumber);
                    person.setGender(boy.isChecked()?"男":"女");
                    person.setName(myname);
                    person.setPassword(mima);
                    person.save();
                    finish();
                }
                break;
            case R.id.getCode:
                //输入法隐藏
                InputMethodManager inputMethodManager =(InputMethodManager)getApplicationContext().
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(password.getWindowToken(), 0); //隐藏
                String number=phoneNumber.getText().toString();
                if(!VerifyAccount(number)){
                    ToastShow(inflater,RegisterActivity.this,0,"请输入正确格式的手机号!", Gravity.CENTER,0,400,Toast.LENGTH_SHORT);
                    break;
                }
                List<Person> people = LitePal.where("account=?", number).find(Person.class);
                if(people.size()!=0){
                    ToastShow(inflater,RegisterActivity.this,0,"账号已被注册!请更换手机号",Gravity.CENTER,0,400,Toast.LENGTH_SHORT);
                    break;
                } else{
                    code=GeneratePassword();
                    ToastShowImg(inflater,RegisterActivity.this,"验证码为: "+code, Gravity.CENTER,0,400,Toast.LENGTH_LONG);
                }
                break;
            case R.id.back:
                finish();
                break;
            default:
                break;
        }
    }

}
