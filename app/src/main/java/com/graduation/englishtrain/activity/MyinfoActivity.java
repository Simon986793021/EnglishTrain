package com.graduation.englishtrain.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.graduation.englishtrain.R;

import java.io.IOException;

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
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        back= (TextView) findViewById(R.id.tv_back);
        toolbarcenter= (TextView) findViewById(R.id.tv_activity_toolbar_center);
        toolbarcenter.setText("我的资料");
        back.setOnClickListener(this);
        getUser();
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    public void getUser() {
        String url="http://123.207.19.116/jiangbo/userInfo.do";
        Log.i("SIMON",url);
        final Request request=new Request.Builder()
                .get()
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
                        Log.i("SIMON",content);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
}
