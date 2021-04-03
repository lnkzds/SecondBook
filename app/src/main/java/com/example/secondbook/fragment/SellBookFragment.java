package com.example.secondbook.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secondbook.MainActivity;
import com.example.secondbook.R;
import com.example.secondbook.SelectImageActivity;
import com.example.secondbook.db.BookInformation;
import com.example.secondbook.Adapter.getImageAdapter;
import com.google.gson.Gson;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.example.secondbook.tool.Tool.ToastShow;

public class SellBookFragment extends Fragment implements View.OnClickListener {

    private Context context;
    private EditText  bookName,author,price,publishing,SellIntroduction;
    private Button  uploadBook,clearImage,ImmediateUpload;
    private RecyclerView recyclerView;

    private List<String>imgPaths=new ArrayList<>();
    private getImageAdapter imageAdapter;

    public SellBookFragment( Context context) {
        this.context = context;
    }


    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sellbook_fragment, container, false);
        bookName=view.findViewById(R.id.bookName);
        author=view.findViewById(R.id.author);
        price=view.findViewById(R.id.price);
        publishing=view.findViewById(R.id.publishing);
        SellIntroduction=view.findViewById(R.id.SellIntroduction);
        uploadBook=view.findViewById(R.id.UploadBook);
        clearImage=view.findViewById(R.id.clearImage);
        ImmediateUpload=view.findViewById(R.id.ImmediateUpload);
        recyclerView=view.findViewById(R.id.recycler_view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //初始化操作
        bookName.setText("");
        author.setText("");
        price.setText("");
        publishing.setText("");
        SellIntroduction.setText("");

        //按钮初始化
        uploadBook.setOnClickListener(this);
        clearImage.setOnClickListener(this);
        ImmediateUpload.setOnClickListener(this);

        //recyclerView   adapter初始化
        imgPaths.clear();
        recyclerView.setVisibility(View.GONE);
        LinearLayoutManager  layoutManager=new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        imageAdapter=new getImageAdapter(imgPaths,context);
        recyclerView.setAdapter(imageAdapter);

        //数据库初始化
        LitePal.getDatabase();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.UploadBook:
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MainActivity.permission_choosePhoto);
                } else {
                    Intent intent=new Intent(getActivity(), SelectImageActivity.class);
                    startActivityForResult(intent,MainActivity.Choose_photo);
                }
                break;
            case R.id.clearImage:
                imgPaths.clear();
                imageAdapter.notifyDataSetChanged();
                recyclerView.setVisibility(View.GONE);
                break;
            case R.id.ImmediateUpload:
                //bookName,author,price,publishing,SellIntroduction
                String MyBookname=bookName.getText().toString();
                String MyAuthor=author.getText().toString();
                String MyPrice=price.getText().toString();
                String MyPublishing=publishing.getText().toString();
                String MySellIntroduction=SellIntroduction.getText().toString();
                LayoutInflater layoutInflater = getLayoutInflater();
                if(MyBookname.equals("")){
                    ToastShow(layoutInflater, context,0,"书名不能为空!", Gravity.CENTER,0,300, Toast.LENGTH_SHORT);
                }else if(MyPrice.equals("")){
                    ToastShow(layoutInflater, context,0,"价格不能为空!", Gravity.CENTER,0,300, Toast.LENGTH_SHORT);
                }else if(MyAuthor.equals("")){
                    ToastShow(layoutInflater, context,0,"作者不能为空!", Gravity.CENTER,0,300, Toast.LENGTH_SHORT);
                }else if(MyPublishing.equals("")){
                    ToastShow(layoutInflater, context,0,"出版社不能为空!", Gravity.CENTER,0,300, Toast.LENGTH_SHORT);
                }else if(MySellIntroduction.equals("")){
                    ToastShow(layoutInflater, context,0,"售书简介不能为空!", Gravity.CENTER,0,300, Toast.LENGTH_SHORT);
                }else if(imgPaths.size()==0){
                    ToastShow(layoutInflater, context,0,"图片不能为空!", Gravity.CENTER,0,300, Toast.LENGTH_SHORT);
                } else{
                    ToastShow(layoutInflater, context,1,"上传成功!", Gravity.CENTER,0,300, Toast.LENGTH_SHORT);
                    //提交数据
                    SharedPreferences pref=context.getSharedPreferences("data",Context.MODE_PRIVATE);
                    String Myaccount=pref.getString("account","null");
                    String MyaccountName=pref.getString("name","null");
                    String MyAccountImagePath=pref.getString("imagepath","null");
                    //设置一些基本参数
                    BookInformation bookInformation=new BookInformation();
                    bookInformation.setAccount(Myaccount);
                    bookInformation.setAccountName(MyaccountName);
                    bookInformation.setAccountImagePath(MyAccountImagePath);
                    bookInformation.setBookAuthor(MyAuthor);
                    bookInformation.setBookName(MyBookname);
                    bookInformation.setBookPrice(MyPrice);
                    bookInformation.setBookpublishing(MyPublishing);
                    bookInformation.setBookSellIntroduction(MySellIntroduction);
                    bookInformation.setIsCollection("false");
                    SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
                    //设置时间
                    Date date = new Date(System.currentTimeMillis());
                    String Mydate=formatter.format(date).toString();
                    bookInformation.setCurrentTime(Mydate);
                    //设置图片地址
                    Gson gson = new Gson();
                    String MyBookImagePaths= gson.toJson(imgPaths);
                    bookInformation.setImagepaths(MyBookImagePaths);
                    //保存数据
                    bookInformation.save();


                    //清空页面
                    bookName.setText("");
                    author.setText("");
                    price.setText("");
                    publishing.setText("");
                    SellIntroduction.setText("");
                    imgPaths.clear();
                    imageAdapter.notifyDataSetChanged();
                    recyclerView.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MainActivity.permission_choosePhoto:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Intent intent=new Intent(getActivity(), SelectImageActivity.class);
                    startActivityForResult(intent,MainActivity.Choose_photo);
                }
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case MainActivity.Choose_photo:
                if (resultCode == RESULT_OK){
                    //如果选择成功则更新RecyclerView 控件
                    int start_poistion=imgPaths.size();
                    int end_poistion=imgPaths.size()+SelectImageActivity.imagePaths.size();
                    recyclerView.setVisibility(View.VISIBLE);
                    for(int i=0;i<SelectImageActivity.imagePaths.size();i++){
                        imgPaths.add(SelectImageActivity.imagePaths.get(i));
                       // Log.d("lnk  path 路径", SelectImageActivity.imagePaths.get(i));
                    }
                    imageAdapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(imgPaths.size()-1);
                }
                break;
        }
    }
}
