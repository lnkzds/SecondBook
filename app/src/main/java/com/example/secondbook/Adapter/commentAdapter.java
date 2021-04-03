package com.example.secondbook.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.secondbook.R;
import com.example.secondbook.db.Comment;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class commentAdapter extends RecyclerView.Adapter<commentAdapter.ViewHolder> {

    private List<Comment> mCommentList;
    private Context context;
    private String bookAuthor;
    static class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView accountImage;
        TextView accountName;
        TextView time;
        TextView author;
        TextView content;

        public ViewHolder(View view){
            super(view);
            accountImage=view.findViewById(R.id.accountImagePath);
            accountName=view.findViewById(R.id.accountName);
            time=view.findViewById(R.id.time);
            author=view.findViewById(R.id.author);
            content=view.findViewById(R.id.content);
        }
    }

    public commentAdapter(List<Comment>list,Context context,String BookAuthor){
        this.mCommentList=list;
        this.context=context;
        this.bookAuthor=BookAuthor;
    }



    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment=mCommentList.get(position);
        holder.accountName.setText(comment.getAccountname());
        holder.time.setText(comment.getTime());
        holder.content.setText(comment.getText());

        String imagePath=comment.getAccountimage();
        if(imagePath.equals("null")){
            Glide.with(context).load(R.drawable.jl).into(holder.accountImage);
        }else {
            Glide.with(context).load(imagePath).into(holder.accountImage);
        }
        String Author=comment.getAccount();
        if(Author.equals(bookAuthor)){
            holder.author.setVisibility(View.VISIBLE);
        }else {
            holder.author.setVisibility(View.GONE);
        }

    }



    @Override
    public int getItemCount() {
        return mCommentList.size();
    }
}
