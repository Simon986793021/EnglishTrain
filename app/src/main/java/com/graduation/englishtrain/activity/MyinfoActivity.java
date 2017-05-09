package com.graduation.englishtrain.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.graduation.englishtrain.R;
import com.graduation.englishtrain.Utils;
import com.graduation.englishtrain.model.User;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by jiangbo on 2017/4/6.
 */

public class MyinfoActivity extends Activity implements View.OnClickListener{
    private TextView back;
    private TextView toolbarcenter;
    private OkHttpClient client=new OkHttpClient();
    private TextView usernameTextview;
    private TextView realname;
    private TextView nickname;
    private TextView phonenum;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        back= (TextView) findViewById(R.id.tv_back);
        toolbarcenter= (TextView) findViewById(R.id.tv_activity_toolbar_center);
        toolbarcenter.setText("我的资料");
        back.setOnClickListener(this);
        usernameTextview= (TextView) findViewById(R.id.tv_username);
        realname= (TextView) findViewById(R.id.tv_realname);
        nickname= (TextView) findViewById(R.id.tv_nickname);
        phonenum= (TextView) findViewById(R.id.tv_phonenum);
        if (Utils.isNetworkAvailable(MyinfoActivity.this))
        {
            getUser();
        }
        else {
            Utils.showToast("请检查网络",MyinfoActivity.this);
        }
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    public void getUser() {
        final Gson gson=new Gson();
        SharedPreferences sp=getSharedPreferences("cookie", Context.MODE_PRIVATE);
        final String username=sp.getString("username",null);
        String password=sp.getString("password", null);
        String url="http://123.207.19.116/jiangbo/userInfo.do";
        final Request request=new Request.Builder()
                .get()
                .header("Cookie", "userName="+username+"; password="+password)
                .url(url)
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response;
                try {
                    response=client.newCall(request).execute();
                    if (response.isSuccessful())
                    {
                        String content=response.body().string();
                        User user=gson.fromJson(content,User.class);
                        final User.UserInfo userInfo=user.user;
                        Log.i(">>>",userInfo.userName);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                usernameTextview.setText(userInfo.userName);
                                realname.setText(userInfo.realName);
                                nickname.setText(userInfo.nickName);
                                phonenum.setText(userInfo.phone);
                            }
                        });
                        Log.i("SIMON",content);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
}
