package com.example.secondbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.secondbook.Adapter.SelectImageAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.example.secondbook.tool.Tool.getPath;

public class SelectImageActivity extends AppCompatActivity implements  SelectImageAdapter.ShowText{

    TextView cancel,detemined,Toatalnumber;
    GridView gridView;
    SelectImageAdapter selectImageAdapter;

    public static List<String>imagePaths=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_image);
        cancel=findViewById(R.id.cancel);
        detemined=findViewById(R.id.determine);
        Toatalnumber=findViewById(R.id.titleNumber);
        gridView=findViewById(R.id.gv_1);

        //初始化操作
        imagePaths.clear();

        List<String> result=getPath("/storage/emulated/0/DCIM");
        if(result==null){
            gridView.setVisibility(View.INVISIBLE);
            Toast.makeText(SelectImageActivity.this,"选择的地址有误，未检测到图片",Toast.LENGTH_SHORT).show();
        }else {
            selectImageAdapter = new SelectImageAdapter(SelectImageActivity.this, R.layout.selectimageitem, result, SelectImageActivity.this);
            gridView.setAdapter(selectImageAdapter);
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        detemined.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(imagePaths.size()!=0){
                   Intent intent=new Intent();
                   setResult(RESULT_OK,intent);
               }
               finish();
            }
        });


    }


    @Override
    public void showtext(int number) {
        StringBuilder ss=new StringBuilder();
        ss.append("已选择");
        ss.append(number);
        ss.append("项");
        Toatalnumber.setText(ss.toString());
    }
}
