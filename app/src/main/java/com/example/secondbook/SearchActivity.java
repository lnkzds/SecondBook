package com.example.secondbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.secondbook.Adapter.BookAdapter;
import com.example.secondbook.db.BookInformation;
import com.example.secondbook.tool.Tool;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.litepal.LitePal;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    ImageView back,delete;
    EditText searchTitle;
    Button search;
    TextView rmss;
    TextView text1,text2,text3,text4,text5,text6,text7;
    List<TextView>textlists=new ArrayList<>();
    ListView listView;

    List<BookInformation>mybooks=new ArrayList<>();

    BookAdapter mybookAdapter;

    private int location_list_item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        setclick();
    }

    private void initView() {

        //初始化控件
        back=findViewById(R.id.back);
        searchTitle=findViewById(R.id.SearchTitle);
        search=findViewById(R.id.searchButton);
        rmss=findViewById(R.id.rmss);
        text1=findViewById(R.id.text1);
        text2=findViewById(R.id.text2);
        text3=findViewById(R.id.text3);
        text4=findViewById(R.id.text4);
        text5=findViewById(R.id.text5);
        text6=findViewById(R.id.text6);
        text7=findViewById(R.id.text7);
        delete=findViewById(R.id.delete);
        listView=findViewById(R.id.list_view);

        //容器装  方便设置点击函数
        textlists.clear();
        textlists.add(text1);
        textlists.add(text2);
        textlists.add(text3);
        textlists.add(text4);
        textlists.add(text5);
        textlists.add(text6);
        textlists.add(text7);

        LitePal.getDatabase();

        //初始化listView
        mybookAdapter=new BookAdapter(SearchActivity.this,R.layout.bookitem,mybooks);
        listView.setAdapter(mybookAdapter);
        listView.setVisibility(View.INVISIBLE);
    }

    private void setclick() {

        //后退键
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //×键
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchTitle.setText("");
                rmss.setVisibility(View.VISIBLE);
                for(int i=0;i<textlists.size();i++){
                    textlists.get(i).setVisibility(View.VISIBLE);
                }
                delete.setVisibility(View.INVISIBLE);
                listView.setVisibility(View.INVISIBLE);//列表不可见
            }
        });


        searchTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchTitle.setCursorVisible(true);
            }
        });

        //搜索按钮设置点击函数
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String content=searchTitle.getText().toString();
               if(content==null||content.equals("")){
                   Tool.ToastShow(getLayoutInflater(),SearchActivity.this,0,"输入不能为空!",Gravity.CENTER,0,-200,Toast.LENGTH_SHORT);
                   searchTitle.setCursorVisible(false);
               }else{
                    updataListView(content);
               }
                //隐藏输入法
                InputMethodManager im = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(searchTitle.getWindowToken(), 0);
            }
        });

        //为每个文本框添加事件
        for(int i=0;i<textlists.size();i++){
            final int finalI = i;
            textlists.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchTitle.setText(textlists.get(finalI).getText().toString());
                    updataListView(textlists.get(finalI).getText().toString());

                }
            });
        }

    }

    //更新ListView
    private void updataListView(String content) {

        //删除键显示
        delete.setVisibility(View.VISIBLE);

        //将热门搜索以及文本框隐藏掉
        rmss.setVisibility(View.INVISIBLE);
        for(int j=0;j<textlists.size();j++){
            textlists.get(j).setVisibility(View.INVISIBLE);
        }
        //列表框可见
        listView.setVisibility(View.VISIBLE);

        List<BookInformation> bookInformations = LitePal.findAll(BookInformation.class);
        if(mybooks.size()!=0){
            mybooks.clear();
        }

        //listView添加数据
        for(int i=0;i<bookInformations.size();i++){
            if(Tool.isequal(content,bookInformations.get(i).getBookName())){
                mybooks.add(bookInformations.get(i));
            }
        }

        if(mybooks.size()!=0){
            mybookAdapter.notifyDataSetChanged();

            //listView 添加子项点击效果
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    location_list_item =position;
                    Intent intent=new Intent(SearchActivity.this, BookDetailsActivity.class);
                    intent.putExtra("BookDetail",mybooks.get(location_list_item));
                    startActivityForResult(intent,1);
                }
            });
        }else{
            //如果查不到书籍
            mybookAdapter.notifyDataSetChanged();
            Tool.ToastShow(getLayoutInflater(),SearchActivity.this,0,"没查到书籍!",Gravity.CENTER,0,-300,Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    //收藏按钮发生了变化
                    Log.d("lnk  return  good", "onActivityResult: ");

                    SharedPreferences pref=getSharedPreferences("data",MODE_PRIVATE);
                    String account=pref.getString("account","null");


                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<String>>() {}.getType();
                    ArrayList<String> finalOutputString ;
                    //list列表里发生变化
                    String status=data.getStringExtra("status");
                    if(status.equals("false")){
                        //如果是删除操作
                        Log.d("lnk  delete", "onActivityResult: ");
                        //列表变化
                        BookInformation information = mybooks.get(location_list_item);
                        String collectiorList = information.getCollectiorList();
                        finalOutputString = gson.fromJson(collectiorList, type);
                        finalOutputString.remove(account);
                        if(finalOutputString.size()==0){
                            information.setToDefault("CollectiorList");
                        }else{
                            String inputString= gson.toJson(finalOutputString);
                            information.setCollectiorList(inputString);
                        }

                        //数据库更新

                        information.updateAll("id=?",information.getId()+"");

                    }else if(status.equals("true")){
                        //如果是增加操作
                        Log.d("lnk  add", "onActivityResult: ");
                        //列表更新
                        BookInformation information = mybooks.get(location_list_item);
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
