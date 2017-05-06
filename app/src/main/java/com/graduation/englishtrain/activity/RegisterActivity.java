package com.graduation.englishtrain.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

public class RegisterActivity extends Activity implements View.OnClickListener{
    private TextView back;
    private TextView toolbarcenter;
    private Button button;
    private EditText username;
    private EditText realname;
    private EditText nickname;
    private EditText phonenum;
    private EditText password;
    private String userName;
    private String nickName;
    private String phoneNum;
    private String realName;
    private String passWord;
    private OkHttpClient client=new OkHttpClient();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        back= (TextView) findViewById(R.id.tv_back);
        toolbarcenter= (TextView) findViewById(R.id.tv_activity_toolbar_center);
        button= (Button) findViewById(R.id.bt_register);
        username= (EditText) findViewById(R.id.et_username);
        realname= (EditText) findViewById(R.id.et_realname);
        nickname= (EditText) findViewById(R.id.et_nickname);
        phonenum= (EditText) findViewById(R.id.et_phonenum);
        password= (EditText) findViewById(R.id.et_password);
        toolbarcenter.setText("注册");
        button.setOnClickListener(this);
        back.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tv_back:
                finish();
                break;
            case R.id.bt_register:
                if (Utils.isNetworkAvailable(RegisterActivity.this)) {
                    getData();
                    if (userName.isEmpty()||realName.isEmpty()||nickName.isEmpty()||phoneNum.isEmpty()||passWord.isEmpty())
                    {
                        Utils.showToast("不能为空",RegisterActivity.this);
                    }
                    else if (!Utils.checkCellphone(phoneNum))
                    {
                        Utils.showToast("请输入正确的手机号码",RegisterActivity.this);
                    }
                    else if (passWord.length()<6)
                    {
                        Utils.showToast("密码长度不能小于6",RegisterActivity.this);
                    }
                    else {
                        upLoadData();
                    }

                }
                else {
                    Utils.showToast("请检查网络",RegisterActivity.this);
                }
                break;
            default:
                break;
        }
    }

    private void upLoadData() {
            String url= "http://123.207.19.116/jiangbo/userReg.do?"+"userName="+userName+"&password="+passWord+"&phone="+phoneNum+""+"&realName="+realName+"&nickName="+nickName;
             final Request request=new Request.Builder()
                .get()
                .url(url)
                .build();
        Log.i(">>>>>>",url);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response;
                try {
                    response=client.newCall(request).execute();
                    if (response.isSuccessful())
                    {
                        final String content=response.body().string();
                        Log.i(">>>>>",content);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                               if (content.contains("1"))
                               {

                                   Utils.showToast("注册成功",RegisterActivity.this);
                                   Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                                   startActivity(intent);
                               }
                               else
                               {
                                   Utils.showToast("系统维护中",RegisterActivity.this);
                               }
                            }
                        });

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void getData() {
        userName=username.getText().toString().trim();
        realName=realname.getText().toString().trim();
        nickName=nickname.getText().toString().trim();
        phoneNum=phonenum.getText().toString().trim();
        passWord=password.getText().toString().trim();
    }
}
