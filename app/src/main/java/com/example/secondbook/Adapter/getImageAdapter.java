package com.example.secondbook.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.secondbook.R;

import java.util.List;

public class getImageAdapter extends RecyclerView.Adapter<getImageAdapter.ViewHolder> {

    private List<String>imgPaths;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;


        public ViewHolder( View itemView) {
            super(itemView);
            imageView= itemView.findViewById(R.id.image);
        }

    }

    public getImageAdapter(List<String>imgpaths,Context context){
        this.imgPaths=imgpaths;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.getimageitem,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String path=imgPaths.get(position);
        Glide.with(context).load(path).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imgPaths.size();
    }
}
