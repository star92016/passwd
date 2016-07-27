package com.starnine.passwd;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.starnine.passwd.utils.LoadSave;
import com.starnine.passwd.utils.User;

import java.io.File;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText et_passwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp=getSharedPreferences("config", Context.MODE_PRIVATE);
        if(sp.getString("passwd","").equals("")){
            finish();
            startActivity(new Intent(this,RegistActivity.class));
            return;
        }
        if(User.IsLogin()){
            finish();
            startActivity(new Intent(this,HomeActivity.class));
            return;
        }
        setContentView(R.layout.activity_login);
        findViewById(R.id.btn_login).setOnClickListener(this);
        et_passwd=(EditText) findViewById(R.id.et_passwd);
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
        String passwd;
        switch (view.getId()){
            case R.id.btn_login:
                passwd=et_passwd.getText().toString().trim();
                if(User.Login(this,passwd)){
                    toast("登录成功");
                    finish();
                    startActivity(new Intent(this,HomeActivity.class));
                }else{
                    toast("密码错误");
                    et_passwd.setText("");
                }
                break;
        }
    }
}
