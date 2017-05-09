package com.graduation.englishtrain.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.graduation.englishtrain.R;
import com.graduation.englishtrain.Utils;
import com.graduation.englishtrain.activity.CourseDetailActivity;
import com.graduation.englishtrain.activity.LoginActivity;
import com.graduation.englishtrain.adapter.CourseListAdapter;
import com.graduation.englishtrain.base.BaseFragment;
import com.graduation.englishtrain.model.CourseList;
import com.panxw.android.imageindicator.ImageIndicatorView;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author:jiangbo
 * Time:2017/4/5
 * Description: This is HomeFragment
 */
public class HomeFragment extends BaseFragment {
    private OkHttpClient client=new OkHttpClient();
    private ImageIndicatorView imageIndicatorView;
    private final String COURSEURL="http://123.207.19.116/jiangbo/getHotCourseList.do";
    private ListView listview;
    private Handler handler;
    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home,container,false);
        imageIndicatorView= (ImageIndicatorView) view.findViewById(R.id.indicate_view);
        Utils.loadImage(imageIndicatorView);
        listview= (ListView) view.findViewById(R.id.lv_fragment_course);
        loadData();//加载数据
        Onclick();//点击
        handler=new MyHandler();
        SharedPreferences sp=getActivity().getSharedPreferences("cookie", Context.MODE_PRIVATE);
        String username=sp.getString("username",null);
        String password=sp.getString("password", null);
        if (username!=null&&password!=null)
        {
            startLogin(username,password);
        }


        return view;
    }
    class MyHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1==1)
            {
               Log.i("SIMON","登录成功");
            }
            else if (msg.arg2==2)
            {
                Log.i("SIMON","账号与密码不匹配");
            }
        }
    };
    private void startLogin(String username,String password) {

        String url="http://123.207.19.116/jiangbo/verifyLogin.do?"+"userName="+username+"&password="+password;
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

                        msg.arg1=1;
                        handler.sendMessage(msg);

//                        SharedPreferences sp=getSharedPreferences("cookie", Context.MODE_PRIVATE);
//                        sp.edit().putString("username",userName)
//                                .putString("password",passWord)
//                                .commit();

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

    private void Onclick() {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String courseId=CourseListAdapter.list.get(position).id;
                Log.i("Simon",courseId);
                Intent intent=new Intent(getActivity(),CourseDetailActivity.class);
                intent.putExtra("courseId",courseId);
                startActivity(intent);
            }
        });
    }

    //加载数据
    private void loadData() {
        final Gson gson =new Gson();
        final Request request=new Request.Builder()
                .get()
                .url(COURSEURL)
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
                        CourseList courseList=gson.fromJson(content,CourseList.class);
                        final List<CourseList.Course> course=courseList.rows;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                listview.setAdapter(new CourseListAdapter(course,getActivity()));
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

