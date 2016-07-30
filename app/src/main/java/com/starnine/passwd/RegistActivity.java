package com.starnine.passwd;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.starnine.passwd.utils.LoadSave;
import com.starnine.passwd.utils.MySQLiteOpenHelper;
import com.starnine.passwd.utils.User;

import java.io.File;

/**
 *
 */
public class RegistActivity extends AppCompatActivity implements View.OnClickListener{

    private TextInputLayout passwdWrapper, passwdWrapper_2;
    private Toast toast;

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
        passwdWrapper = (TextInputLayout) findViewById(R.id.passwdWrapper);
        passwdWrapper_2 = (TextInputLayout) findViewById(R.id.passwdWrapper_2);
        passwdWrapper.setHint("请输入密码");
        passwdWrapper_2.setHint("再次输入密码");

        findViewById(R.id.btn_regist).setOnClickListener(this);

        findViewById(R.id.tv_input).setOnClickListener(this);
    }

    public void toast(String s){
        if(toast==null)
            toast=Toast.makeText(this,s,Toast.LENGTH_SHORT);
        else
            toast.setText(s);
        toast.show();
    }
    @Override
    public void onClick(View view) {
        passwdWrapper.setErrorEnabled(false);
        passwdWrapper_2.setErrorEnabled(false);
        String s;
        String s1;
        MySQLiteOpenHelper mySQLiteOpenHelper;
        switch (view.getId()){
            case R.id.btn_regist:
                s = passwdWrapper.getEditText().getText().toString().trim();
                s1 = passwdWrapper_2.getEditText().getText().toString().trim();
                if (s.equals("")) {
                    passwdWrapper.setError("请输入密码");
                } else if (s1.equals("")) {
                    passwdWrapper_2.setError("请再次输入密码");
                }else if(!s.equals(s1)){
                    passwdWrapper_2.setError("请输入和上面一样的密码");
                }else{
                    passwdWrapper.setErrorEnabled(false);
                    passwdWrapper_2.setErrorEnabled(false);
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
