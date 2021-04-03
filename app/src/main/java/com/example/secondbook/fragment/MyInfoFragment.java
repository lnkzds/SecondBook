package com.example.secondbook.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.example.secondbook.LoginActivity;
import com.example.secondbook.MainActivity;
import com.example.secondbook.R;
import com.example.secondbook.RegisterActivity;
import com.example.secondbook.UploadAndCollectionActivity;
import com.example.secondbook.db.Person;
import com.example.secondbook.tool.Tool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class MyInfoFragment extends Fragment implements View.OnClickListener {

    //控件
    private RelativeLayout myUpload, myComment, myEdit;
    private CircleImageView circleImageView;
    private Button save, log_out;
    private EditText edit;
    private TextView Phone_Text;

    //popwindow 控件
    private Dialog dialog;
    private View inflate;
    private Button camera, pic, cancel;

    //属性
    public String MyName, MyImageUrl, MyPhonenumber;
    private Context context;
    public Uri imageUri;



    public MyInfoFragment( Context context) {
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Log.d("lnk", "onCreateView: ");
        View view = inflater.inflate(R.layout.myinfo_fragment, container, false);
        myUpload = view.findViewById(R.id.upload);
        myComment = view.findViewById(R.id.comment);
        myEdit = view.findViewById(R.id.editinformation);
        circleImageView = view.findViewById(R.id.image);
        save = view.findViewById(R.id.save);
        log_out = view.findViewById(R.id.logout);
        edit = view.findViewById(R.id.edit);
        Phone_Text = view.findViewById(R.id.phoneNumber);


        //初始化数据
        if ((MyImageUrl!=null)&&(!MyImageUrl.equals("null"))){
           if(MyImageUrl.charAt(0)=='c') {
               imageUri=Uri.parse(MyImageUrl);
               Bitmap bitmap= null;
               try {
                   bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                   Matrix m = new Matrix();
                   m.setRotate(90, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
                   Bitmap bm1 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
                   circleImageView.setImageBitmap(bm1);
               } catch (FileNotFoundException e) {
                   e.printStackTrace();
               }
           }else{
               Glide.with(context).load(MyImageUrl).into(circleImageView);
           }
        }
        edit.setText(MyName);
        Phone_Text.setText(MyPhonenumber);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        log_out.setOnClickListener(this);
        myUpload.setOnClickListener(this);
        myComment.setOnClickListener(this);
        myEdit.setOnClickListener(this);
        save.setOnClickListener(this);
        circleImageView.setOnClickListener(this);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case MainActivity.Take_photo:
                if (resultCode == RESULT_OK) {
                    MyImageUrl=imageUri.toString();
                    Log.d("lnk  照片位置", MyImageUrl);
                    try {
                        Bitmap bitmap= BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                        Matrix m = new Matrix();
                        m.setRotate(90, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
                        Bitmap bm1 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
                        circleImageView.setImageBitmap(bm1);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Log.d("lnk  拍照片成功", "onActivityResult: ");
                }
                break;
            case MainActivity.Choose_photo:
                if(resultCode==RESULT_OK){
                    if(Build.VERSION.SDK_INT>=19){
                        handleImageOnKitKat(data);
                    }else{
                        handleImageBeforeKitKat(data);
                    }
                    Log.d("lnk  选取照片成功", "onActivityResult: ");
                }
                break;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editinformation:
                save.setVisibility(View.VISIBLE);
                edit.setCursorVisible(true);
                edit.setFocusable(true);
                edit.setFocusableInTouchMode(true);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    edit.requestFocus();
                    imm.showSoftInput(edit, 0);
                }
                edit.setTextColor(getResources().getColor(R.color.black));
                edit.setBackground(getResources().getDrawable(R.drawable.white_contour));
                break;
            case R.id.save:
                String name = edit.getText().toString();
                if(!Tool.VerifyName(name)){
                    Toast.makeText(context,"名称不符合规范，只能包含中音文和数字!",Toast.LENGTH_SHORT).show();
                    edit.setText(MyName);
                    break;
                }
                this.MyName=name;
                save.setVisibility(View.INVISIBLE);
                edit.setCursorVisible(false);
                edit.setFocusable(false);
                edit.setFocusableInTouchMode(false);
                edit.setTextColor(getResources().getColor(R.color.white));
                edit.setBackground(getResources().getDrawable(R.drawable.nocontour));
                InputMethodManager inputMethodManager =(InputMethodManager)getActivity().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(edit.getWindowToken(), 0); //隐藏
                break;
            case R.id.image:
                dialog = new Dialog(context, R.style.DialogTheme);
                inflate = LayoutInflater.from(context).inflate(R.layout.pop, null);
                //初始化控件
                camera = inflate.findViewById(R.id.camera);
                pic = inflate.findViewById(R.id.pic);
                cancel = inflate.findViewById(R.id.cancel);
                camera.setOnClickListener(this);
                pic.setOnClickListener(this);
                cancel.setOnClickListener(this);
                //将布局设置给Dialog
                dialog.setContentView(inflate);
                //获取当前Activity所在的窗体
                Window dialogWindow = dialog.getWindow();
                //设置Dialog从窗体底部弹出
                dialogWindow.setGravity(Gravity.BOTTOM);
                //获得窗体的属性
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                lp.y = 15;//设置Dialog距离底部的距离
                //       将属性设置给窗体
                dialogWindow.setAttributes(lp);
                dialog.show();//显示对话框
                break;
            case R.id.camera:
                dialog.dismiss();
                String outpath = MyPhonenumber + ".jpg";
                File outputImage = new File(getActivity().getExternalCacheDir(), outpath);
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT >= 24) {
                    imageUri = FileProvider.getUriForFile(context, "com.example.secondbook.fileprovider", outputImage);
                } else {
                    imageUri = Uri.fromFile(outputImage);
                }
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, MainActivity.Take_photo);
                break;
            case R.id.pic:
                dialog.dismiss();
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MainActivity.permission_requestCode);
                } else {
                    openAlbum();
                }
                break;
            case R.id.cancel:
                dialog.dismiss();
                break;
            case R.id.logout:
                AlertDialog.Builder  dialoge=new AlertDialog.Builder(context);
                dialoge.setTitle("提示");
                dialoge.setMessage("确定要退出登录吗");
                dialoge.setCancelable(false);
                dialoge.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialoge.setNeutralButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //退出登录操作
                        SharedPreferences.Editor editor=context.getSharedPreferences("data",MODE_PRIVATE).edit();
                        editor.putString("status","false");
                        editor.apply();
                        Person person=new Person();
                        person.setImagepath(MyImageUrl);
                        person.setName(MyName);
                        person.updateAll("account =? ",MyPhonenumber);
                        MainActivity activity = (MainActivity)getActivity();
                        activity.ExitInterface();
                    }
                });
                dialoge.show();
                break;
            case R.id.upload:
                Intent intent_2=new Intent(context, UploadAndCollectionActivity.class);
                intent_2.putExtra("category","upload");
                startActivity(intent_2);
                break;
            case R.id.comment:
                Intent intent_3=new Intent(context, UploadAndCollectionActivity.class);
                intent_3.putExtra("category","collection");
                startActivity(intent_3);
                break;
            default:
                Log.d("lnk  按到了空按钮", "onClick: ");
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("lnk    碎片请求权限", "onRequestPermissionsResult: ");
        switch (requestCode){
            case MainActivity.permission_requestCode:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }
                break;
        }
    }

    private void openAlbum() {
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,MainActivity.Choose_photo);
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri=data.getData();
        String imagepath=getImagePath(uri,null);
        MyImageUrl=imagepath;
        Glide.with(context).load(imagepath).into(circleImageView);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data) {
        String imagepath=null;
        Uri uri=data.getData();
        Log.d("lnk    相册Uri", uri.toString());
        if(DocumentsContract.isDocumentUri(context,uri)){
            String docId=DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id=docId.split(":")[1];
                String selection=MediaStore.Images.Media._ID+"="+id;
                imagepath=getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri= ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagepath=getImagePath(contentUri,null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            imagepath=getImagePath(uri,null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            imagepath= uri.getPath();
        }
        MyImageUrl=imagepath;
        Log.d("lnk 实际位置", MyImageUrl);
        Glide.with(context).load(imagepath).into(circleImageView);
    }

    private String getImagePath(Uri uri,String selection) {
        String path=null;
        Cursor cursor=getActivity().getContentResolver().query(uri,null,selection,null,null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       // Log.d("lnk  碎片destroy", "onDestroy: ");
        SharedPreferences.Editor editor=getActivity().getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putString("name",MyName);
        editor.putString("imagepath",MyImageUrl);
        editor.putString("account",MyPhonenumber);
        editor.apply();
    }
}
