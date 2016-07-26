package com.starnine.passwd;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class RegistActivity extends AppCompatActivity implements View.OnClickListener{
private EditText et_passwd;
    private EditText et_passwd2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        et_passwd=(EditText)findViewById(R.id.et_passwd);
        et_passwd2=(EditText)findViewById(R.id.et_passwd2);
        findViewById(R.id.btn_regist).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String s;
        String
        switch (view.getId()){
            case R.id.btn_regist:
                break;
        }
    }
}
