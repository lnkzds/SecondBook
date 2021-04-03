package com.example.secondbook.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.secondbook.R;
import com.example.secondbook.UploadAndCollectionActivity;
import com.example.secondbook.db.BookInformation;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.litepal.LitePal;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class uploadCommentAdapter extends ArrayAdapter<BookInformation> {

    private int category;
    private int resourceId;
    private String account;

    Gson gson = new Gson();
    Type type = new TypeToken<ArrayList<String>>() {}.getType();
    ArrayList<String>finalOutputString;




    public uploadCommentAdapter(@NonNull Context context, int resource, @NonNull List<BookInformation> objects,int category,String account) {
        super(context, resource, objects);
        this.category=category;
        this.resourceId=resource;
        this.account=account;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final BookInformation bookInformation = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView==null){
            view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.bookImageView=view.findViewById(R.id.bookImage);
            viewHolder.bookName=view.findViewById(R.id.bookName);
            viewHolder.time=view.findViewById(R.id.time);
            viewHolder.bookprice=view.findViewById(R.id.bookPrice);
            viewHolder.delete=view.findViewById(R.id.delete);
            view.setTag(viewHolder);
        }else{
            view=convertView;
            viewHolder= (ViewHolder) view.getTag();
        }

        String imagepaths = bookInformation.getImagepaths();
        ArrayList<String> bookImageList = gson.fromJson(imagepaths, type);
        Glide.with(getContext()).load(bookImageList.get(0)).into(viewHolder.bookImageView);
        viewHolder.bookName.setText(bookInformation.getBookName());
        viewHolder.bookprice.setText(bookInformation.getBookPrice());
        viewHolder.time.setText(bookInformation.getCurrentTime());
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Log.d("lnk", "onClick:  按钮");
               // Log.d("lnk  poistion", position+"");
                Toast.makeText(getContext(),"删除成功!",Toast.LENGTH_SHORT).show();
                // 对应数据库删除或更新
                if(category==1){
                    LitePal.deleteAll(BookInformation.class,"id = ?",bookInformation.getId()+"");
                }else if(category==0){

                    String collectiorList = bookInformation.getCollectiorList();
                    finalOutputString = gson.fromJson(collectiorList, type);
                    finalOutputString.remove(account);
                    if(finalOutputString.size()==0){
                        bookInformation.setToDefault("CollectiorList");
                    }else{
                        String inputString= gson.toJson(finalOutputString);
                        bookInformation.setCollectiorList(inputString);
                    }
                    bookInformation.updateAll("id=?",bookInformation.getId()+"");
                }
                //列表中删除
                UploadAndCollectionActivity.books.remove(position);
                UploadAndCollectionActivity uploadAndCollectionActivity=(UploadAndCollectionActivity)getContext();
                uploadAndCollectionActivity.updateData();
            }
        });
        return view;
    }


    class ViewHolder{
        ImageView bookImageView;
        TextView bookName;
        TextView time;
        TextView bookprice;
        Button delete;
    }

}
