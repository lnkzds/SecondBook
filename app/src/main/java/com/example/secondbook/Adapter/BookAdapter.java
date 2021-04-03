package com.example.secondbook.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.secondbook.R;
import com.example.secondbook.SelectImageActivity;
import com.example.secondbook.db.BookInformation;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BookAdapter extends ArrayAdapter<BookInformation> {

    private int resourceId;

    public BookAdapter(@NonNull Context context, int resource, @NonNull List<BookInformation> objects) {
        super(context, resource, objects);
        this.resourceId=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       // position -= listView.getHeaderViewsCount();
        final BookInformation bookInformation = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView==null){
            view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.bookImageView=view.findViewById(R.id.bookImage);
            viewHolder.bookName=view.findViewById(R.id.bookName);
            viewHolder.sellbookIntroduction=view.findViewById(R.id.sellbookIntroduction);
            viewHolder.accountName=view.findViewById(R.id.accountName);
            viewHolder.bookPrice=view.findViewById(R.id.bookPrice);
            viewHolder.accountImage=view.findViewById(R.id.accountImagePath);
            view.setTag(viewHolder);
        }else{
            view=convertView;
            viewHolder= (ViewHolder) view.getTag();
        }
        Gson gson=new Gson();
        String imagepaths = bookInformation.getImagepaths();
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        ArrayList<String> bookImageList = gson.fromJson(imagepaths, type);

        Glide.with(getContext()).load(bookImageList.get(0)).into(viewHolder.bookImageView);
        viewHolder.bookName.setText(bookInformation.getBookName());
        viewHolder.sellbookIntroduction.setText(bookInformation.getBookSellIntroduction());
        viewHolder.accountName.setText(bookInformation.getAccountName());
        viewHolder.bookPrice.setText(bookInformation.getBookPrice());

        if(bookInformation.getAccountImagePath().equals("null")){
            Glide.with(getContext()).load(R.drawable.jl).into(viewHolder.accountImage);
        }else{
            Glide.with(getContext()).load(bookInformation.getAccountImagePath()).into(viewHolder.accountImage);
        }
        return view;
    }

    class ViewHolder{
        ImageView bookImageView;
        TextView bookName;
        TextView sellbookIntroduction;
        TextView accountName;
        TextView bookPrice;
        CircleImageView accountImage;
    }
}
