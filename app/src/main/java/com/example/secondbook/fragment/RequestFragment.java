package com.example.secondbook.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.secondbook.LoginActivity;
import com.example.secondbook.MainActivity;
import com.example.secondbook.R;
import com.example.secondbook.RegisterActivity;

import static android.app.Activity.RESULT_OK;

public class RequestFragment extends Fragment {

    private Button login,registered;
    private TextView textView;
    private String content;
    public RequestFragment(String content){
        this.content=content;
    }

    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        //Log.d("lnk", "onCreateView: ");
        View view=inflater.inflate(R.layout.request_fragment,container,false);
        login=view.findViewById(R.id.login);
        registered=view.findViewById(R.id.registered);
        textView=view.findViewById(R.id.fragmentText);
        textView.setText(content);
        return view;
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        //Log.d("lnk", "onActivityCreated: ");
        super.onActivityCreated(savedInstanceState);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), LoginActivity.class);
               // getActivity().startActivityForResult(intent,MainActivity.login_requestCode);
                getActivity().startActivityForResult(intent,MainActivity.login_requestCode);
            }
        });
        registered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
    public void setName(String content){
        this.content=content;
        textView.setText(content);
    }


}
