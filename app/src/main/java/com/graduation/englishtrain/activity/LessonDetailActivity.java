package com.graduation.englishtrain.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.graduation.englishtrain.R;
import com.graduation.englishtrain.Utils;
import com.graduation.englishtrain.model.CourseDetail;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by jiangbo on 2017/5/5.
 */

public class LessonDetailActivity extends Activity implements View.OnClickListener{
    private TextView back;
    private TextView toolbarcenter;
    private OkHttpClient client=new OkHttpClient();
    private TextView contenttextview;
    private ImageView imageview;
    private String courseId;
    private Button button;
    private Intent intent;
    private String lessonId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_detail);
        intent=getIntent();
        back= (TextView) findViewById(R.id.tv_back);
        toolbarcenter= (TextView) findViewById(R.id.tv_activity_toolbar_center);
        toolbarcenter.setText("课程详情");
        contenttextview= (TextView) findViewById(R.id.tv_course_content);
        imageview= (ImageView) findViewById(R.id.iv_course_detail);
        button= (Button) findViewById(R.id.bt_order);
        back.setOnClickListener(this);
        if (Utils.isNetworkAvailable(LessonDetailActivity.this))
        {
            loadData();
            button.setOnClickListener(this);
        }
        else {
            Utils.showToast("请检查网络",LessonDetailActivity.this);
        }

    }


    private void loadData() {

        courseId=intent.getStringExtra("courseId");
        if (courseId!=null)
        {
            String url="http://123.207.19.116/jiangbo/courseDetail.do?courseId="+courseId;
            getRequest(url);
        }

    }

    private void getRequest(String url) {
            //加载数据

            final Gson gson =new Gson();
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
                            final String content=response.body().string();
                            CourseDetail coursedetail=gson.fromJson(content,CourseDetail.class);
                            final CourseDetail.Course course=coursedetail.course;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    contenttextview.setText(course.content);
                                    String url="http://123.207.19.116/jiangbo/getImg.do?"+course.img2;
                                    Glide.with(LessonDetailActivity.this).load(url)
                                            .error(R.drawable.coursedetail)
                                            .into(imageview);//Glide 加载图片
                                }
                            });

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tv_back:
                finish();
                break;
            case R.id.bt_order:
                startOrder();
            default:
                break;
        }
    }

    private void startOrder() {
        String lessonId=intent.getStringExtra("lessonId");
        String orderurl="http://123.207.19.116/jiangbo/orderLesson.do?lessonId="+lessonId;
        SharedPreferences sp=getSharedPreferences("cookie", Context.MODE_PRIVATE);
        String username=sp.getString("username",null);
        String password=sp.getString("password", null);
        if (lessonId!=null)
        {
            if (username!=null&&password!=null)
            {
                startRequest(orderurl,username,password);
            }
            else
            {
                Intent intent=new Intent(LessonDetailActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        }
        else {
            Utils.showToast("系统维护",LessonDetailActivity.this);
        }

    }

    private void startRequest(String orderurl,String username,String password) {
        final Request request=new Request.Builder()
                .header("Cookie", "userName="+username+"; password="+password)
                .get()
                .url(orderurl)
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response reponse;
                try {
                    reponse=client.newCall(request).execute();
                    if (reponse.isSuccessful())
                    {
                        String content=reponse.body().string();
                        Log.i(">>>>>>>",content);
                        if (content.contains("1"));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Utils.showToast("预约成功",LessonDetailActivity.this);
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
