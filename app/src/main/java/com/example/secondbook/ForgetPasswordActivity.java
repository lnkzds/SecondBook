package com.example.secondbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener{

    Button reset;
    TextView getCode,back;
    ImageView eye;
    EditText phoneNumber,VerificationCode,password;
    String code;
    LayoutInflater inflater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        InitView();
    }
    private void InitView(){
        //初始化控件
        back=findViewById(R.id.back);
        getCode=findViewById(R.id.getCode);
        eye=findViewById(R.id.eye);
        reset=findViewById(R.id.commit);
        phoneNumber=findViewById(R.id.mobile);
        VerificationCode=findViewById(R.id.verification);
        password=findViewById(R.id.password);

        //设置点击事件
        back.setOnClickListener(this);
        getCode.setOnClickListener(this);
        eye.setOnClickListener(this);
        reset.setOnClickListener(this);

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
        switch(v.getId()) {
            case R.id.eye:
                if (password.getInputType() == 128)
                    password.setInputType(129);
                else if (password.getInputType() == 129)
                    password.setInputType(128);
                break;
            case R.id.getCode:
                //输入法隐藏
                String number=phoneNumber.getText().toString();

                if(!VerifyAccount(number)){
                    ToastShow(inflater,ForgetPasswordActivity.this,0,"请输入正确格式的手机号!", Gravity.CENTER,0,0,Toast.LENGTH_SHORT);
                    break;
                }
                List<Person> people = LitePal.where("account=?", number).find(Person.class);
                if(people.size()==0){
                    ToastShow(inflater,ForgetPasswordActivity.this,0,"账号未被注册，无法重设密码!",Gravity.CENTER,0,0,Toast.LENGTH_SHORT);
                    break;
                } else{
                    code=GeneratePassword();
                    ToastShowImg(inflater,ForgetPasswordActivity.this,"验证码为: "+code, Gravity.CENTER,0,0,Toast.LENGTH_LONG);
                }
                break;
            case R.id.commit:
                String phonenumber=phoneNumber.getText().toString();
                String verificationcode=VerificationCode.getText().toString();
                String mima=password.getText().toString();
                if(!VerifyAccount(phonenumber)){
                    ToastShow(inflater,ForgetPasswordActivity.this,0,"手机号错误!", Gravity.CENTER,0,0,Toast.LENGTH_SHORT);
                }else if((code==null)||(verificationcode.equals(""))||!(code.equals(verificationcode))){
                    ToastShow(inflater,ForgetPasswordActivity.this,0,"验证码错误!", Gravity.CENTER,0,0,Toast.LENGTH_SHORT);
                }else if(mima.equals("")){
                    ToastShow(inflater,ForgetPasswordActivity.this,0,"密码不能为空!", Gravity.CENTER,0,0,Toast.LENGTH_SHORT);
                }else{
                    //成功
                    ToastShow(inflater,ForgetPasswordActivity.this,1,"重置密码成功",Gravity.CENTER,0,0,Toast.LENGTH_SHORT);
                    Person person=new Person();
                    person.setPassword(mima);
                    person.updateAll("account=?",phonenumber);
                    finish();
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
