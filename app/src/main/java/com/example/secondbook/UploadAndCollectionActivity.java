package com.example.secondbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.secondbook.Adapter.uploadCommentAdapter;
import com.example.secondbook.db.BookInformation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.litepal.LitePal;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UploadAndCollectionActivity extends AppCompatActivity {

    ListView listView;
    TextView textView;
    TextView back;
    uploadCommentAdapter myAdapter;
    FloatingActionButton floatingActionButton;
    public static List<BookInformation> books=new ArrayList<>();

    private int location_list_item;
    String category;
    Gson gson = new Gson();
    Type type = new TypeToken<ArrayList<String>>() {}.getType();
    String account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_and_collection);
        listView=findViewById(R.id.listview);
        textView=findViewById(R.id.titleText);
        back=findViewById(R.id.back);
        floatingActionButton=findViewById(R.id.PlaceTop);

        //为返回设置退出信息
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //初始化 books数据
        books.clear();
        SharedPreferences pref=getSharedPreferences("data",MODE_PRIVATE);


        Intent intent=getIntent();
        category=intent.getStringExtra("category");//根据类别 来确定是  点击了 我的上传 还是我的 收藏
        if(category.equals("upload")){
            textView.setText("我的上传");
            account=pref.getString("account","null");
            books= LitePal.where("account = ?",account).find(BookInformation.class);
            //Log.d("lnk 上传  books size", books.size()+"");
            myAdapter=new uploadCommentAdapter(UploadAndCollectionActivity.this,R.layout.uploadcollection,books,1,account);
        }else if(category.equals("collection")){
            textView.setText("我的收藏");
            List<BookInformation> all = LitePal.findAll(BookInformation.class);
            account=pref.getString("account","null");
            for(int i=0;i<all.size();i++){
                String collectiorList = all.get(i).getCollectiorList();
                if(collectiorList!=null) {
                    ArrayList<String> finalOutputString = gson.fromJson(collectiorList, type);
                    if(finalOutputString.indexOf(account)>=0){
                        books.add(all.get(i));
                    }
                }
            }
            Log.d("lnk  收藏 books size", books.size()+"");
            myAdapter=new uploadCommentAdapter(UploadAndCollectionActivity.this,R.layout.uploadcollection,books,0,account);
        }

        listView.setAdapter(myAdapter);
        //为列表设置点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                location_list_item = position;
                Intent intent=new Intent(UploadAndCollectionActivity.this, BookDetailsActivity.class);
                intent.putExtra("BookDetail",books.get(location_list_item));
                startActivityForResult(intent,1);
            }
        });


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.smoothScrollToPosition(0);
            }
        });
    }

    public void updateData(){
        myAdapter.notifyDataSetChanged();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    //收藏按钮发生了变化
                        String status=data.getStringExtra("status");
                        ArrayList<String> finalOutputString ;
                        if(status.equals("false")){
                            BookInformation bookInformation = books.get(location_list_item);
                            String collectiorList = bookInformation.getCollectiorList();
                            finalOutputString = gson.fromJson(collectiorList, type);
                            finalOutputString.remove(account);
                            if(finalOutputString.size()==0){
                                Log.d("lnk  size=0", "onActivityResult: ");
                                bookInformation.setToDefault("CollectiorList");
                            }else{
                                Log.d("lnk  size !=0", "onActivityResult: ");
                                String inputString= gson.toJson(finalOutputString);
                                bookInformation.setCollectiorList(inputString);
                            }
                            Log.d("lnk  id", bookInformation.getId()+"");
                            bookInformation.updateAll("id=?",bookInformation.getId()+"");

                            //如果加载的是 我的收藏界面  则list也需要发生变化
                            if(category.equals("collection")) {
                                books.remove(location_list_item);
                                updateData();
                            }
                        } else if(status.equals("true")){
                            //如果是增加操作
                            //列表更新
                            BookInformation information =  books.get(location_list_item);
                            String collectiorList = information.getCollectiorList();
                            if(collectiorList==null){
                                finalOutputString=new ArrayList<>();

                            }else {
                                finalOutputString = gson.fromJson(collectiorList, type);

                            }
                            finalOutputString.add(account);
                            String inputString= gson.toJson(finalOutputString);
                            information.setCollectiorList(inputString);

                            //数据库更新
                            information.updateAll("id=?",information.getId()+"");
                        }
                    }



                break;
            default:
                break;
        }
    }
}
