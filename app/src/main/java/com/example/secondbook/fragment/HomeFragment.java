package com.example.secondbook.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.secondbook.Adapter.BookAdapter;
import com.example.secondbook.BookDetailsActivity;
import com.example.secondbook.R;
import com.example.secondbook.SearchActivity;
import com.example.secondbook.db.BookInformation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.litepal.LitePal;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class HomeFragment extends Fragment {

    EditText searchTitle;
    ListView bookList;
    List<BookInformation> bookInformations=new ArrayList<>();
    List<BookInformation> ReversebookInformations=new ArrayList<>(); //逆序
    Context context;

    //广告效果
    private AtomicInteger atomicInteger=new AtomicInteger();
    ImageView indicator;
    ImageView[]  indicators;
    ViewPager  viewPager;
    ViewGroup  viewGroup;

    //悬浮按钮
    FloatingActionButton floatingActionButton;


    //当前listview item 的位置
    private int location_list_item;
    public HomeFragment( Context context) {
        this.context = context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        searchTitle=view.findViewById(R.id.SearchTitle);
        bookList=view.findViewById(R.id.bookslist);
        floatingActionButton=view.findViewById(R.id.PlaceTop);
        LitePal.getDatabase();//更新数据库
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //搜索数据库数据
        bookInformations.clear();
        ReversebookInformations.clear();
        bookInformations = LitePal.findAll(BookInformation.class);
        for(int i=bookInformations.size()-1;i>=0;i--){
            ReversebookInformations.add(bookInformations.get(i));
        }
        BookAdapter bookAdapter=new BookAdapter(context,R.layout.bookitem,ReversebookInformations); //逆序处理 方便显示


        View header = LayoutInflater.from(context).inflate(R.layout.listheadview, null);
        bookList.addHeaderView(header);
        bookList.setAdapter(bookAdapter);

        //添加广告效果
        viewPager=header.findViewById(R.id.adv);
        viewGroup=header.findViewById(R.id.view_adv);

        List<View>views=new ArrayList<>();
        ImageView img1=new ImageView(context);
        img1.setBackgroundResource(R.drawable.jl1);
        views.add(img1);
        ImageView img2=new ImageView(context);
        img2.setBackgroundResource(R.drawable.jl2);
        views.add(img2);
        ImageView img3=new ImageView(context);
        img3.setBackgroundResource(R.drawable.jl3);
        views.add(img3);
        ImageView img4=new ImageView(context);
        img4.setBackgroundResource(R.drawable.jl4);
        views.add(img4);

        indicators=new ImageView[views.size()];
        for(int i=0;i<views.size();i++){
            indicator=new ImageView(context);
            indicator.setLayoutParams(new LinearLayout.LayoutParams(40,40));
            indicator.setPadding(10,10,10,10);
            indicators[i]=indicator;
            if(i==0){
                indicators[i].setBackgroundResource(R.drawable.focus);
            }else{
                indicators[i].setBackgroundResource(R.drawable.nofocus);
            }
            viewGroup.addView(indicator);
        }

        viewPager.setAdapter(new MyPageAdapter(views));

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                atomicInteger.getAndSet(position);
                for(int i=0;i<4;i++){
                    if(i==position){
                        indicators[i].setBackgroundResource(R.drawable.focus);
                    }else{
                        indicators[i].setBackgroundResource(R.drawable.nofocus);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


        //悬浮按钮添加效果
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookList.smoothScrollToPosition(0);
            }
        });


        //listView 添加子项点击效果
        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               location_list_item =position-bookList.getHeaderViewsCount();
               Intent intent=new Intent(context, BookDetailsActivity.class);
               intent.putExtra("BookDetail",ReversebookInformations.get(location_list_item));
               startActivityForResult(intent,1);
            }
        });

        //标题栏添加效果
        searchTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, SearchActivity.class);
                startActivity(intent);
            }
        });

    }


    class MyPageAdapter extends PagerAdapter{

        private List<View>viewList;

        public MyPageAdapter(List<View> viewlist){
            this.viewList=viewlist;
        }

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view==object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            container.addView(viewList.get(position));
            return viewList.get(position);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(viewList.get(position));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    //收藏按钮发生了变化
                    Log.d("lnk  return  good", "onActivityResult: ");

                    SharedPreferences pref=context.getSharedPreferences("data",MODE_PRIVATE);
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
                        BookInformation information = ReversebookInformations.get(location_list_item);
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
                        BookInformation information = ReversebookInformations.get(location_list_item);
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
