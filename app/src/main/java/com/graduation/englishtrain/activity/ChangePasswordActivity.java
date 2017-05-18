package com.graduation.englishtrain.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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
 * Created by Simon on 2017/5/18.
 */

public class ChangePasswordActivity extends Activity implements View.OnClickListener {
    private TextView back;
    private TextView toolbarcenter;
    private EditText oldpasswordeditText;
    private EditText newpasswordeditText;
    private Button button;
    private OkHttpClient client=new OkHttpClient();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        back= (TextView) findViewById(R.id.tv_back);
        toolbarcenter= (TextView) findViewById(R.id.tv_activity_toolbar_center);
        toolbarcenter.setText("修改密码");
        oldpasswordeditText= (EditText) findViewById(R.id.et_old_password);
        newpasswordeditText= (EditText) findViewById(R.id.et_new_password);
        button= (Button) findViewById(R.id.bt_changepassword);
        back.setOnClickListener(this);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tv_back:
                finish();
                break;
            case R.id.bt_changepassword:
                String oldpassword=oldpasswordeditText.getText().toString();
                checkOldPassword(oldpassword);
                break;
            default:
                break;
        }
    }

    private void checkOldPassword(String oldpassword) {
        String url="http://123.207.19.116/jiangbo/verifyUserPwd.do?pwd="+oldpassword;
        SharedPreferences sp=getSharedPreferences("cookie", Context.MODE_PRIVATE);
        String username=sp.getString("username",null);
        String password=sp.getString("password", null);
        final Request request=new Request.Builder()
                .header("Cookie", "userName="+username+"; password="+password)
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
                        Log.i(">>>>>>>",content);
                        if (content.contains("1"))
                        {
                           chagePassword();
                        }
                        else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Utils.showToast("请输入正确的旧密码", ChangePasswordActivity.this);
                                }
                        });
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            private void chagePassword() throws IOException {
                String newpassword=newpasswordeditText.getText().toString();
                if (!TextUtils.isEmpty(newpassword))
                {
                    SharedPreferences sp=getSharedPreferences("cookie", Context.MODE_PRIVATE);
                    String username=sp.getString("username",null);
                    String password=sp.getString("password", null);
                    Response response;
                    String url="http://123.207.19.116/jiangbo/editUserPwd.do?pwd="+newpassword;
                    Log.i(">>>",url);
                    final Request request=new Request.Builder()
                            .header("Cookie", "userName="+username+"; password="+password)
                            .get()
                            .url(url)
                            .build();


                    response=client.newCall(request).execute();
                    if (response.isSuccessful())
                    {
                        String content=response.body().string();
                        Log.i("<<<<",content);
                        if (content.contains("1"))
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Utils.showToast("修改成功,下次登录请使用新密码",ChangePasswordActivity.this);
                                }
                            });

                        }
                    }
                }
                else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utils.showToast("新密码不能为空",ChangePasswordActivity.this);
                        }
                    });
                }
            }
        }).start();
    }
}
