package com.graduation.englishtrain.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.graduation.englishtrain.activity.MainActivity;
import com.graduation.englishtrain.adapter.CourseListAdapter;
import com.graduation.englishtrain.base.BaseFragment;
import com.graduation.englishtrain.model.CourseList;
import com.panxw.android.imageindicator.AutoPlayManager;
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

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home,container,false);
        imageIndicatorView= (ImageIndicatorView) view.findViewById(R.id.indicate_view);
        Utils.loadImage(imageIndicatorView);
        listview= (ListView) view.findViewById(R.id.lv_fragment_course);
        loadData();//加载数据
        Onclick();//点击
        return view;
    }

    private void Onclick() {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String courseId=CourseListAdapter.list.get(position).id;
                Log.i("Simon",courseId);
                Intent intent=new Intent(getActivity(),CourseDetailActivity.class);
                intent.putExtra("id",id);
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
                        Log.i(">>>",content);
                        Log.i(">>>>","获取成功");
                        CourseList courseList=gson.fromJson(content,CourseList.class);
                        final List<CourseList.Course> course=courseList.rows;
                        Log.i(">>>",course.toString());
                        Log.i("Simon",course.get(1).courseName);
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

