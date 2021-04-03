package com.example.secondbook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.secondbook.db.Person;
import com.example.secondbook.fragment.HomeFragment;
import com.example.secondbook.fragment.MyInfoFragment;
import com.example.secondbook.fragment.RequestFragment;
import com.example.secondbook.fragment.SellBookFragment;

import org.litepal.FluentQuery;
import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private RadioGroup navigationBar;
    private RadioButton home,sellbook,my;
    private SharedPreferences pref;
    private final FragmentManager manager=getSupportFragmentManager();
    RequestFragment requestFragment_middle,requestFragment_right;
    MyInfoFragment myInfoFragment;
    SellBookFragment sellBookFragment;
    HomeFragment homeFragment;

    public static final int login_requestCode=1;//登录请求号码
    public static final int Take_photo=2;//请求拍照
    public static final int Choose_photo=3;//从相册中选取
    public static final int permission_requestCode=4;//权限请求
    public static final int permission_choosePhoto=5;//请求获取图片权限请求



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        //初始化控件
        navigationBar=findViewById(R.id.rg_1);
        home=findViewById(R.id.rb_1);
        sellbook=findViewById(R.id.rb_2);
        my=findViewById(R.id.rb_3);

        //sharedP存储
        pref=getSharedPreferences("data",MODE_PRIVATE);

        //初始化碎片
        //请求登录碎片
        requestFragment_middle=new RequestFragment("出售");
        requestFragment_right=new RequestFragment("我的");
        //我的信息碎片
        myInfoFragment=new MyInfoFragment(MainActivity.this);
        pref=getSharedPreferences("data",MODE_PRIVATE);
        myInfoFragment.MyName=pref.getString("name","计量大学");
        myInfoFragment.MyPhonenumber=pref.getString("account","计量大学");
        myInfoFragment.MyImageUrl=pref.getString("imagepath","null");


        //卖书碎片
        sellBookFragment=new SellBookFragment(MainActivity.this);

        //主页碎片
        homeFragment=new HomeFragment(MainActivity.this);



        navigationBar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId){
                        case R.id.rb_1:
                            //按下了主页按钮
                            home.setTextColor(getApplicationContext().getResources().getColor(R.color.yellow));
                            sellbook.setTextColor(getApplicationContext().getResources().getColor(R.color.iconColor));
                            my.setTextColor(getApplicationContext().getResources().getColor(R.color.iconColor));
                            FragmentTransaction transaction_1 = manager.beginTransaction();
                            transaction_1.replace(R.id.main_fragment, homeFragment);
                            transaction_1.commit();
                            break;
                        case R.id.rb_2:
                            //按下了卖书按钮
                            home.setTextColor(getApplicationContext().getResources().getColor(R.color.iconColor));
                            sellbook.setTextColor(getApplicationContext().getResources().getColor(R.color.yellow));
                            my.setTextColor(getApplicationContext().getResources().getColor(R.color.iconColor));
                            String status=pref.getString("status","false");
                            if(status.equals("false")){
                                FragmentTransaction transaction = manager.beginTransaction();
                                transaction.replace(R.id.main_fragment, requestFragment_middle);
                                transaction.commit();
                            }else if(status.equals("true")){
                                //写下已登录状态的逻辑
                                FragmentTransaction transaction = manager.beginTransaction();
                                transaction.replace(R.id.main_fragment, sellBookFragment);
                                transaction.commit();
                            }
                            break;
                        case R.id.rb_3:
                            //按下了我的按钮
                            home.setTextColor(getApplicationContext().getResources().getColor(R.color.iconColor));
                            sellbook.setTextColor(getApplicationContext().getResources().getColor(R.color.iconColor));
                            my.setTextColor(getApplicationContext().getResources().getColor(R.color.yellow));
                            String status1=pref.getString("status","false");
                            if(status1.equals("false")){
                                FragmentTransaction transaction = manager.beginTransaction();
                                transaction.replace(R.id.main_fragment, requestFragment_right);
                                transaction.commit();
                            }else if(status1.equals("true")){
                                //写下已登录状态的逻辑
                                FragmentTransaction transaction = manager.beginTransaction();
                                transaction.replace(R.id.main_fragment, myInfoFragment);
                                transaction.commit();
                            }
                            break;
                        default:
                            break;

                    }
            }
        });

        //先默认主页被选中
        home.setChecked(true);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case login_requestCode:
                if(resultCode==RESULT_OK){
                    Log.d("lnk进入活动 onActivity", "onActivityResult: ");
                    myInfoFragment.MyImageUrl=data.getStringExtra("imagepath");
                    myInfoFragment.MyPhonenumber=data.getStringExtra("account");
                    myInfoFragment.MyName=data.getStringExtra("name");
                    if(sellbook.isChecked()){
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.replace(R.id.main_fragment, sellBookFragment);
                        transaction.commit();
                    }else if(my.isChecked()){
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.replace(R.id.main_fragment, myInfoFragment);
                        transaction.commit();
                    }
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("lnk 进入活动求取权限", "onRequestPermissionsResult: ");
        switch (requestCode){
            case permission_requestCode:
                myInfoFragment.onRequestPermissionsResult(requestCode,permissions,grantResults);
                break;
            case permission_choosePhoto:
                sellBookFragment.onRequestPermissionsResult(requestCode,permissions,grantResults);
                break;
        }
    }


    public void ExitInterface(){
        if(sellbook.isChecked()){
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.main_fragment, requestFragment_middle);
            transaction.commit();
        }else if(my.isChecked()){
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.main_fragment, requestFragment_right);
            transaction.commit();
        }
    }
}
