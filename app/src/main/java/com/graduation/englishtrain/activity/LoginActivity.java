package com.graduation.englishtrain.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.graduation.englishtrain.R;
import com.graduation.englishtrain.Utils;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


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
    private String userName;
    private String passWord;
    private OkHttpClient client=new OkHttpClient();
    public static boolean isLogin=false;
    private Handler handler;
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
        button.setOnClickListener(this);
        back.setOnClickListener(this);
        handler=new MyHandler();
    }
    /*
   由于退出登录跳转到此页面  点击返回 ，又回到了退出登录页面，出现逻辑错误，强行让其跳转到首页
    */
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
    }
    class MyHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (msg.arg1==1)
        {
            Utils.showToast("登录成功",LoginActivity.this);
        }
        else if (msg.arg2==2)
        {
            Utils.showToast("账号与密码不匹配",LoginActivity.this);
        }
    }
    };
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tv_back:
                Intent intent1=new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent1);
                break;
            case R.id.tv_register:
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_login:
                if (Utils.isNetworkAvailable(LoginActivity.this))
                {
                    getData();
                    startLogin();
                }
                else {
                    Utils.showToast("请检查网络",LoginActivity.this);
                }
                break;
            default:
                break;
        }
    }

    private void startLogin() {
        String url="http://123.207.19.116/jiangbo/verifyLogin.do?"+"userName="+userName+"&password="+passWord;
        final Request request=new Request.Builder()
                .get()
                .url(url)
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response;
                Message msg=handler.obtainMessage();
                try {
                    response=client.newCall(request).execute();
                    final String content=response.body().string();
                    if (content.contains("1")){
                        isLogin=true;

                        msg.arg1=1;
                        handler.sendMessage(msg);

                        SharedPreferences sp=getSharedPreferences("cookie",Context.MODE_PRIVATE);
                        sp.edit().putString("username",userName)
                                .putString("password",passWord)
                                .commit();

                        finish();
                    }
                    else {
                       msg.arg2=2;
                        handler.sendMessage(msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }).start();
    }

    public void getData() {
            userName=username.getText().toString().trim();
            passWord=password.getText().toString().trim();
    }
}
