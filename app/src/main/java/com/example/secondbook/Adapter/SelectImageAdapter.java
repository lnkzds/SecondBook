package com.example.secondbook.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.secondbook.R;
import com.example.secondbook.SelectImageActivity;

import java.util.List;

public class SelectImageAdapter extends ArrayAdapter<String>{

    private int resourceId;
    private ShowText showText;
    private int totalnumber;  //显示有多少项被选中

    public SelectImageAdapter(@NonNull Context context, int resource, @NonNull List<String> objects, ShowText showtext) {
        super(context, resource, objects);
        resourceId=resource;
        this.showText=showtext;
        totalnumber=0;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final String name = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView==null){
            view=LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.imageView=view.findViewById(R.id.image);
            viewHolder.checkBox=view.findViewById(R.id.selected);
            view.setTag(viewHolder);
        }else{
            view=convertView;
            viewHolder= (ViewHolder) view.getTag();
        }

        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    totalnumber++;
                    showText.showtext(totalnumber);
                    SelectImageActivity.imagePaths.add(name);
                    Log.d("lnk img  add path ", position+"   "+name);
                }else{
                    totalnumber--;
                    showText.showtext(totalnumber);
                    SelectImageActivity.imagePaths.remove(name);
                    Log.d("lnk img remove path ", position+"   "+name);
                }
            }
        });
        Glide.with(getContext()).load(name).into(viewHolder.imageView);
        return view;
    }

    public interface ShowText{
        void showtext(int number);
    }

    class ViewHolder{
        ImageView imageView;
        CheckBox checkBox;
    }


}
