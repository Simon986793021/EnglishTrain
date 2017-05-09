package com.graduation.englishtrain.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.graduation.englishtrain.R;
import com.graduation.englishtrain.Utils;
import com.graduation.englishtrain.adapter.CourseListAdapter;
import com.graduation.englishtrain.model.CourseDetail;
import com.graduation.englishtrain.model.CourseList;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by jiangbo on 2017/5/5.
 */

public class CourseDetailActivity extends Activity implements View.OnClickListener{
    private TextView back;
    private TextView toolbarcenter;
    private OkHttpClient client=new OkHttpClient();
    private TextView contenttextview;
    private ImageView imageview;
    private String courseId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        back= (TextView) findViewById(R.id.tv_back);
        toolbarcenter= (TextView) findViewById(R.id.tv_activity_toolbar_center);
        toolbarcenter.setText("课程详情");
        contenttextview= (TextView) findViewById(R.id.tv_course_content);
        imageview= (ImageView) findViewById(R.id.iv_course_detail);
        back.setOnClickListener(this);
        if (Utils.isNetworkAvailable(CourseDetailActivity.this))
        {
            loadData();
        }
        else {
            Utils.showToast("请检查网络",CourseDetailActivity.this);
        }
    }

    private void loadData() {
        Intent intent=getIntent();
        courseId=intent.getStringExtra("courseId");
        String url="http://123.207.19.116/jiangbo/courseDetail.do?courseId="+courseId;
        getRequest(url);
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
                            Log.i(">>>>>>>>>>>>",content);
                            CourseDetail coursedetail=gson.fromJson(content,CourseDetail.class);
                            final CourseDetail.Course course=coursedetail.course;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    contenttextview.setText(course.content);
                                    String url="http://123.207.19.116/jiangbo/getImg.do?"+course.img2;
                                    Log.i(">>>>>>>>>",url);
                                    Glide.with(CourseDetailActivity.this).load(url)
                                            .error(R.drawable.coursedetail)
                                            .into(imageview);//Glide 加载图片
                                  //  listview.setAdapter(new CourseListAdapter(course,getActivity()));
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
            default:
                break;
        }
    }
}
