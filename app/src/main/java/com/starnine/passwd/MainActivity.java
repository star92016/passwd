package com.starnine.passwd;
//张中俊测试修改
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }
    private void initView(){
        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.btn_regist).setOnClickListener(this);
        findViewById(R.id.btn_home).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                startActivity(new Intent(this,LoginActivity.class));
                break;
            case R.id.btn_regist:
                startActivity(new Intent(this, RegistActivity.class));
                break;
            case R.id.btn_home:
                startActivity(new Intent(this,HomeActivity.class));
            default:
                break;
        }
    }
}
