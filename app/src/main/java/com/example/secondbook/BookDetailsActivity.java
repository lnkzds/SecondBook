package com.example.secondbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.example.secondbook.Adapter.commentAdapter;
import com.example.secondbook.db.BookInformation;
import com.example.secondbook.db.Comment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BookDetailsActivity extends AppCompatActivity {


    ImageView accountImage;
    TextView accountName,Time;
    TextView account,publishing,price,author,sellbookIntroductionr,IsCollection;
    CheckBox checkBox;
    Intent return_Inetnet;
    BookInformation bookInformation;
    ImageView back;
    boolean exit_flag;

    //广告效果
    private AtomicInteger atomicInteger=new AtomicInteger();
    ImageView indicator;
    ImageView[]  indicators;
    ViewPager  viewPager;
    ViewGroup viewGroup;
    List<View> views;


    //评论效果
    RecyclerView recyclerView;
    TextView commentNum;
    EditText commentText;
    Button send;
    commentAdapter myAdapter;
    List<Comment> comments;

    String current_account;
    String current_accountImagePath,current_name;
    int current_comment_num;
    SharedPreferences pref;
    NestedScrollView scrollView;

    LinearLayout bottom;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化数据
        setContentView(R.layout.activity_book_details);
        accountImage=findViewById(R.id.accountImagePath);
        accountName=findViewById(R.id.accountName);
        Time=findViewById(R.id.time);
        account=findViewById(R.id.account);
        publishing=findViewById(R.id.publishing);
        price=findViewById(R.id.price);
        author=findViewById(R.id.author);
        sellbookIntroductionr=findViewById(R.id.sellbookIntroduction);
        checkBox=findViewById(R.id.Ischeck);
        IsCollection=findViewById(R.id.ss);
        back=findViewById(R.id.back);
        return_Inetnet=new Intent();
        recyclerView=findViewById(R.id.recycler_view);
        commentNum=findViewById(R.id.commentNum);
        commentText=findViewById(R.id.contentText);
        send=findViewById(R.id.send);
        scrollView=findViewById(R.id.scroll);
        bottom=findViewById(R.id.bottom);




        //返回控件添加返回效果
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //获得传过来的book信息
        bookInformation=(BookInformation)getIntent().getSerializableExtra("BookDetail");

        //通过book信息初始化各控件
        if(bookInformation.getAccountImagePath().equals("null")){
            Glide.with(getApplicationContext()).load(R.drawable.jl).into(accountImage);
        }else {
            Glide.with(getApplicationContext()).load(bookInformation.getAccountImagePath()).into(accountImage);
        }
        accountName.setText(bookInformation.getAccountName());
        Time.setText(bookInformation.getCurrentTime());
        account.setText(bookInformation.getAccount());
        publishing.setText(bookInformation.getBookpublishing());
        price.setText(bookInformation.getBookPrice());
        author.setText(bookInformation.getBookAuthor());
        sellbookIntroductionr.setText(bookInformation.getBookSellIntroduction());


        // 如果是未登录状态
         pref=getSharedPreferences("data",MODE_PRIVATE);
         String status=pref.getString("status","false");
         if(status.equals("false")){
             checkBox.setVisibility(View.INVISIBLE);
             IsCollection.setVisibility(View.INVISIBLE);
             commentText.setVisibility(View.INVISIBLE);
             send.setVisibility(View.INVISIBLE);
             bottom.setVisibility(View.INVISIBLE);

         }


        //检查是否已收藏
        exit_flag=false;
        String collectiorList=bookInformation.getCollectiorList();
        SharedPreferences pref=getSharedPreferences("data",MODE_PRIVATE);
        String account=pref.getString("account","null");
        if(collectiorList!=null){
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<String>>() {}.getType();
            ArrayList<String> finalOutputString = gson.fromJson(collectiorList, type);
            int idex=finalOutputString.indexOf(account);
            if(idex>=0){
                exit_flag=true;
            }else{
                exit_flag=false;
            }
        }
        if(exit_flag){
            checkBox.setChecked(true);
            IsCollection.setText("已收藏");
            IsCollection.setTextColor(getResources().getColor(R.color.huang));
        }else{
            IsCollection.setText("未收藏");
            IsCollection.setTextColor(getResources().getColor(R.color.iconColor));
        }


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               //Log.d("lnk  check box", "onCheckedChanged: ");
               if(isChecked){
                   if(exit_flag==false){
                       setResult(RESULT_OK,return_Inetnet);
                       return_Inetnet.putExtra("status","true");
                   }else{
                       setResult(RESULT_CANCELED,return_Inetnet);
                   }
                   IsCollection.setText("已收藏");
                   IsCollection.setTextColor(getResources().getColor(R.color.huang));
               }else {
                   if(exit_flag==true){
                       setResult(RESULT_OK,return_Inetnet);
                       return_Inetnet.putExtra("status","false");
                   }else{
                       setResult(RESULT_CANCELED,return_Inetnet);
                   }
                   IsCollection.setText("未收藏");
                   IsCollection.setTextColor(getResources().getColor(R.color.iconColor));
               }
           }
        });

        //添加书片移动效果
        viewPager=findViewById(R.id.adv);
        viewGroup=findViewById(R.id.view_adv);

        //书籍图片
        Gson gson=new Gson();
        String imagepaths = bookInformation.getImagepaths();
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        ArrayList<String> bookImageList = gson.fromJson(imagepaths, type);
        views=new ArrayList<>();
        for(int i=0;i<bookImageList.size();i++){
            ImageView img1=new ImageView(BookDetailsActivity.this);
            Glide.with(getApplicationContext()).load(bookImageList.get(i)).into(img1);
            views.add(img1);
        }



        indicators=new ImageView[views.size()];
        for(int i=0;i<views.size();i++){
            indicator=new ImageView(BookDetailsActivity.this);
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
                for(int i=0;i<views.size();i++){
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
        
        //初始化评论部分
        
        initComment();
        
    }

    private void initComment() {
        LitePal.getDatabase();
        comments = LitePal.where("bookId = ?", bookInformation.getId() + "").find(Comment.class);

        //获取当前账户的信息


        myAdapter=new commentAdapter(comments,BookDetailsActivity.this,bookInformation.getAccount());
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(myAdapter);  //初始化recylclerView

        //初始化标题
        current_comment_num=comments.size();
        StringBuilder builder=new StringBuilder();
        builder.append("评论(");
        builder.append(current_comment_num);
        builder.append(")");
        commentNum.setText(builder.toString());



        commentText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentText.setCursorVisible(true);
            }
        });

        //为按钮设置点击事件
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content=commentText.getText().toString();

                pref=getSharedPreferences("data",MODE_PRIVATE);
                String status=pref.getString("status","false");
                if(status.equals("false")){

                }else {
                    if (content == null || content.equals("")) {
                        Toast.makeText(BookDetailsActivity.this, "输入内容不能为空! ", Toast.LENGTH_SHORT).show();
                        commentText.setCursorVisible(false);
                    } else {
                        Toast.makeText(BookDetailsActivity.this, "提交成功! ", Toast.LENGTH_SHORT).show();
                        commentText.setText("");

                        //为列表添加数据
                        //获取当前账户的信息
                        current_accountImagePath = pref.getString("imagepath", "null");
                        current_account = pref.getString("account", "null");
                        current_name = pref.getString("name", "null");
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
                        //设置时间
                        Date date = new Date(System.currentTimeMillis());
                        String Mydate = formatter.format(date).toString();
                        Comment comment = new Comment();
                        comment.setAccount(current_account);
                        comment.setAccountimage(current_accountImagePath);
                        comment.setTime(Mydate);
                        comment.setBookId(bookInformation.getId());
                        comment.setText(content);
                        comment.setAccountname(current_name);

                        comments.add(comment);
                        myAdapter.notifyDataSetChanged();
                        scrollView.fullScroll(View.FOCUS_DOWN);//滚到底部
                        //添加到数据库
                        comment.save();

                        //更改文本框显示
                        current_comment_num = current_comment_num + 1;
                        StringBuilder builder = new StringBuilder();
                        builder.append("评论(");
                        builder.append(current_comment_num);
                        builder.append(")");
                        commentNum.setText(builder.toString());


                    }
                    //隐藏输入法
                    InputMethodManager im = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(accountName.getWindowToken(), 0);
             }
         }
         });

    }

    class MyPageAdapter extends PagerAdapter {

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
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {

            viewList.get(position).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(BookDetailsActivity.this,FullScreenImageActivity.class);
                    intent.putExtra("position",position);
                    intent.putExtra("paths",bookInformation.getImagepaths());
                    startActivity(intent);

                }
            });
            container.addView(viewList.get(position));
            return viewList.get(position);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(viewList.get(position));
        }
    }



}
