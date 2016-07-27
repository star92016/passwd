package com.starnine.passwd;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.starnine.passwd.utils.LoadSave;
import com.starnine.passwd.utils.MySQLiteOpenHelper;
import com.starnine.passwd.utils.User;

import java.io.File;

public class RegistActivity extends AppCompatActivity implements View.OnClickListener{
private EditText et_passwd;
    private EditText et_passwd2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp=getSharedPreferences("config", Context.MODE_PRIVATE);
        if(!sp.getString("passwd","").equals("")){
            toast("你已注册");
            finish();
            return;
        }
        setContentView(R.layout.activity_regist);
        et_passwd=(EditText)findViewById(R.id.et_passwd);
        et_passwd2=(EditText)findViewById(R.id.et_passwd2);
        findViewById(R.id.btn_regist).setOnClickListener(this);

        findViewById(R.id.tv_input).setOnClickListener(this);
    }
    private Toast toast;
    public void toast(String s){
        if(toast==null)
            toast=Toast.makeText(this,s,Toast.LENGTH_SHORT);
        else
            toast.setText(s);
        toast.show();
    }
    @Override
    public void onClick(View view) {
        String s;
        String s1;
        MySQLiteOpenHelper mySQLiteOpenHelper;
        switch (view.getId()){
            case R.id.btn_regist:
                s=et_passwd.getText().toString().trim();
                s1=et_passwd2.getText().toString().trim();
                if(s.equals("")||s1.equals("")){
                    toast("不能为空");
                }else if(!s.equals(s1)){
                    toast("不一致");
                }else{
                    User.Regist(this,s);
                    toast("注册成功");
                    finish();
                    startActivity(new Intent(this,LoginActivity.class));
                }
                break;
            case R.id.tv_input:
                new Thread(){
                    public void run(){
                        new LoadSave().Load(new File(Environment.getExternalStorageDirectory().
                                getAbsolutePath()+"/passwd"));
                    }
                }.start();
                toast("导入成功");
                break;
        }
    }
}
