package com.graduation.englishtrain.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.graduation.englishtrain.R;
import com.graduation.englishtrain.Utils;
import com.graduation.englishtrain.model.LessonOrderList;
import com.graduation.englishtrain.model.TeacherAndCourse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by jiangbo on 2017/5/10.
 */

public class MyLessonActivity extends Activity{
    private ListView listView;
    private OkHttpClient client=new OkHttpClient();
    private TextView back;
    private TextView toolbarcenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_lesson);
        listView= (ListView) findViewById(R.id.lv_activity_my_lesson);
        back= (TextView) findViewById(R.id.tv_back);
        toolbarcenter= (TextView) findViewById(R.id.tv_activity_toolbar_center);
        toolbarcenter.setText("我的预约");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (Utils.isNetworkAvailable(MyLessonActivity.this))
        {
            loadData();
        }
       else
        {
            Utils.showToast("请检查网络",MyLessonActivity.this);
        }
    }

    private void loadData() {
        SharedPreferences sp=getSharedPreferences("cookie", Context.MODE_PRIVATE);
        String username=sp.getString("username",null);
        String password=sp.getString("password", null);
        if (username!=null&&password!=null)
        {
            startRequest(username,password);
        }
        else
        {
            Intent intent=new Intent(MyLessonActivity.this,LoginActivity.class);
            startActivity(intent);
        }
    }

    private void startRequest(String username, String password) {
        String url="http://123.207.19.116/jiangbo/listMyLessonOrder.do";
        final Gson gson=new Gson();
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
                        LessonOrderList orderlist=gson.fromJson(content,LessonOrderList.class);
                        final List<LessonOrderList.LessonOrder> lessonorder=orderlist.rows;
                        runOnUiThread(new Runnable() {
                            public List<Map<String,Object>> getData() {
                                List<Map<String, Object>> list=new ArrayList<>();

                                for (int i=0;i<lessonorder.size();i++)

                                {
                                    Map<String,Object> map=new HashMap<>();
                                    map.put("coursename",lessonorder.get(i).courseName);
                                    map.put("tachername",lessonorder.get(i).teacherName);
                                    list.add(map);
                                }
                                return list;
                            }

                            @Override
                            public void run() {
                                listView.setAdapter(new SimpleAdapter(MyLessonActivity.this,getData(),R.layout.item_order_lesson,new String []{"coursename","tachername"},new int[]{R.id.tv_coursename,R.id.tv_teachername}));
                            }
                        });

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
