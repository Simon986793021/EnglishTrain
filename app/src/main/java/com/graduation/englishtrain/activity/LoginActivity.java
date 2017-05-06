package com.graduation.englishtrain.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.graduation.englishtrain.R;


/**
 * Created by jiangbo on 2017/5/6.
 */

public class LoginActivity extends Activity implements View.OnClickListener{
    private TextView back;
    private TextView toolbarcenter;
    private TextView register;
    private EditText username;
    private EditText password;
    private Button button;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        back= (TextView) findViewById(R.id.tv_back);
        toolbarcenter= (TextView) findViewById(R.id.tv_activity_toolbar_center);
        toolbarcenter.setText("登录");
        register= (TextView) findViewById(R.id.tv_register);
        register.setOnClickListener(this);
        username= (EditText) findViewById(R.id.et_username_login);
        password= (EditText) findViewById(R.id.et_password_login);
        button= (Button) findViewById(R.id.bt_login);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_register:
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_login:
                getData();
            default:
                break;
        }
    }

    public void getData() {
    }
}
