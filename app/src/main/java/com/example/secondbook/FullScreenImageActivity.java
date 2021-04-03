package com.example.secondbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FullScreenImageActivity extends AppCompatActivity {


    ViewPager viewPager;
    List<View> views;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        viewPager=findViewById(R.id.adv);

        Intent intent=getIntent();
        //书籍图片
        Gson gson=new Gson();
        String imagepaths = intent.getStringExtra("paths");
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        ArrayList<String> bookImageList = gson.fromJson(imagepaths, type);

        views=new ArrayList<>();
        for(int i=0;i<bookImageList.size();i++){
            ImageView img1=new ImageView(FullScreenImageActivity.this);
            Glide.with(getApplicationContext()).load(bookImageList.get(i)).into(img1);
            views.add(img1);
        }

        int index=intent.getIntExtra("position",0);
        viewPager.setAdapter(new MyPageAdapter(views));
        viewPager.setCurrentItem(index);
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
                    finish();
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
