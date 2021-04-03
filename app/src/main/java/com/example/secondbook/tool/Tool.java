package com.example.secondbook.tool;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.secondbook.MainActivity;
import com.example.secondbook.R;
import com.example.secondbook.RegisterActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//工具类 提供一些静态方法
public  class Tool {




    // 判断账号是否满足12位的数字格式
    public static Boolean VerifyAccount(String password){
        if("".equals(password)||password.length()!=11)
            return false;
        for(int i=0;i<password.length();i++) {
            if(password.charAt(i)>='0'&&password.charAt(i)<='9') {
                continue;
            }else {
                return false;
            }
        }
        return true;
    }
    //判断输入的昵称是否符合规范
    public static Boolean VerifyName(String name){
        if(name.equals(""))
            return false;
        for(int i=0;i<name.length();i++) {
            if((name.charAt(i)>='0'&&name.charAt(i)<='9')||(name.charAt(i)>='a'&&name.charAt(i)<='z')||name.charAt(i)>='A'&&name.charAt(i)<='Z'||(name.charAt(i)>19968&&name.charAt(i)<40869)) {
                continue;
            }else {
                return false;
            }
        }
        return true;
    }

    //随机生成四位数的验证码
    public static String GeneratePassword(){
        StringBuilder mima=new StringBuilder();
        Random rand = new Random();
        for(int i=0;i<4;i++){
            mima.append(rand.nextInt(10));
        }
        return mima.toString();
    }

    //显示自定义的提示框
    public static  void ToastShow(LayoutInflater inflater,Context context, int selection, String content, int gravity, int xOffset, int yOffset, int duration){

        View toastview = inflater.inflate(R.layout.toast, null);
        TextView textView = (TextView) toastview.findViewById(R.id.text);
        textView.setText(content);
        LinearLayout linearLayout=toastview.findViewById(R.id.linearlayout);
        if(selection==0) {
            linearLayout.setBackgroundColor(context.getResources().getColor(R.color.black));
            textView.setTextColor(context.getResources().getColor(R.color.white));
        }else{
            linearLayout.setBackgroundColor(context.getResources().getColor(R.color.lucency));
            textView.setTextColor(context.getResources().getColor(R.color.green));
        }
        Toast toast = new Toast(context);
        toast.setGravity(gravity, xOffset, yOffset);
        toast.setDuration(duration);
        toast.setView(toastview);
        toast.show();
    }
    //显示自定义带图片的提示框
    public static void ToastShowImg(LayoutInflater inflater,Context context,String content,int gravity,int xOffset, int yOffset,int duration){

        View toastviewImg=inflater.inflate(R.layout.toastimage, null);
        TextView textView = (TextView) toastviewImg.findViewById(R.id.text);
        textView.setText(content);
        Toast toast = new Toast(context);
        toast.setGravity(gravity, xOffset, yOffset);
        toast.setDuration(duration);
        toast.setView(toastviewImg);
        toast.show();
    }

    //搜索某一目录下的所有 jpg，png图片   flodpath 表示目录
    public static List<String> getPath(String folderpath){
        File file=new File(folderpath);
        String[] files = file.list();
        if(files==null){
            //Toast.makeText(MainActivity.this,"输入的地址中不存在任何文件",Toast.LENGTH_SHORT).show();
            Log.d("lnk  输入的地址中不存在任何文件", "getPath: ");
            return null;
        }
        List<String>documents=new ArrayList<>();  //存放jpg 文件
        List<String>directorys=new ArrayList<>();  //存放  目录
        for(int i=0;i<files.length;i++){
            File current_file=new File(folderpath+"/"+files[i]);
            if(current_file.isDirectory()){
                directorys.add(current_file.getAbsolutePath());
            }else if(current_file.isFile()&&isImageFile(current_file.getName())){
                documents.add(current_file.getAbsolutePath());
            }
        }
        if(directorys.size()==0){
            return documents;  //如果没有目录则直接返回文件
        }
        //如果存在目录
        int idex=0;
        while(true){
            File file1=new File(directorys.get(idex));
            String[] files1 = file1.list();
            for(int i=0;i<files1.length;i++){
                File current_file=new File(file1.getAbsolutePath()+"/"+files1[i]);
                if(current_file.isDirectory()){
                    directorys.add(current_file.getAbsolutePath());
                }else if(current_file.isFile()&&isImageFile(current_file.getName())){
                    documents.add(current_file.getAbsolutePath());
                }
            }
            idex++;
            if(idex==directorys.size()){
                break;
            }
        }
        return documents;
    }

    //判断文件是否是jpg\png格式
    private static boolean isImageFile(String name) {
        String fileend=name.substring(name.lastIndexOf(".")+1,name.length());
        if(fileend.equalsIgnoreCase("jpg")){
            return  true;
        }else if(fileend.equalsIgnoreCase("png")){
            return true;
        }
        return false;
    }

    public static boolean isequal(String s1,String s2){

        int length1=s1.length();
        int length2=s2.length();
        for(int i=0;i<length1;i++){
            for(int j=0;j<length2;j++){
                if(s1.charAt(i)==s2.charAt(j)){
                    return true;
                }
            }
        }
        return false;
    }

}
