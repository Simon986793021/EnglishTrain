package com.graduation.englishtrain.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.graduation.englishtrain.R;
import com.graduation.englishtrain.Utils;
import com.graduation.englishtrain.base.BaseFragment;
import com.graduation.englishtrain.model.CourseList;
import com.graduation.englishtrain.model.LessonList;
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
 * Author:jiangbo
 * Time:2017/4/5
 * Description: This is TrainFragment
 */
public class TrainFragment extends BaseFragment implements View.OnClickListener{
    private TextView textView;
    private ListView listview;
    private String lessonurl="http://123.207.19.116/jiangbo/userSearchLesson.do?period=0&courseId=0&teacherId=0";
    private OkHttpClient client=new OkHttpClient();
    private Spinner teachersp;
    private Spinner coursesp;
    private Spinner timesp;
    private List<TeacherAndCourse.Teacher> teacherList;
    private List<TeacherAndCourse.Course> courselist;
    private String teacherid;
    private String courseid;
    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frament_train,container,false);
        textView= (TextView) view.findViewById(R.id.tv_select);
        textView.setOnClickListener(this);
        listview= (ListView) view.findViewById(R.id.lv_lesson_list);

        if(Utils.isNetworkAvailable(getActivity())) {
            loadData();
        }
        else {
            Utils.showToast("请检查网络",getActivity());
        }
        return view;
    }

    private void loadData() {
        final Gson gson =new Gson();
        final Request request=new Request.Builder()
                .get()
                .url(lessonurl)
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
                        final LessonList lessonlist=gson.fromJson(content,LessonList.class);
                        final List<LessonList.Lesson> lesson=lessonlist.rows;
                        getActivity().runOnUiThread(new Runnable() {
                            public List<Map<String,Object>> getData() {
                                List<Map<String, Object>> list=new ArrayList<>();

                                for (int i=0;i<lesson.size();i++)

                                {
                                    Map<String,Object> map=new HashMap<>();
                                    map.put("coursename",lesson.get(i).courseName);
                                    map.put("tachername",lesson.get(i).teacherName);
                                    map.put("starttime", Utils.stampToDate(lesson.get(i).lessonStartTime));
                                    list.add(map);
                                }
                                return list;
                            }

                            @Override
                            public void run() {
                                listview.setAdapter(new SimpleAdapter(getActivity(),getData(),R.layout.item_fragment_lesson,new String []{"coursename","tachername","starttime"},new int[]{R.id.tv_coursename,R.id.tv_teachername,R.id.tv_starttime}));
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
            case R.id.tv_select:
                if (Utils.isNetworkAvailable(getActivity()))
                {
                    selectDialog();
                }
                else {
                    Utils.showToast("请检查网络",getActivity());
                }
                break;

            default:
                break;
        }
    }

    private void selectDialog() {

        final AlertDialog dialog=new AlertDialog.Builder(getActivity()).create();
        dialog.setCanceledOnTouchOutside(true);
        View view=LayoutInflater.from(getActivity()).inflate(R.layout.dialog_select,null,false);
        teachersp= (Spinner) view.findViewById(R.id.sn_teacher);
        coursesp= (Spinner) view.findViewById(R.id.sn_course);
        timesp= (Spinner) view.findViewById(R.id.sn_time);
        String [] time=new String[]{"","一周以内","一个月以内"};
        timesp.setAdapter(new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,time));
        String url="http://123.207.19.116/jiangbo/courseAndTeacher.do";
        final Gson gson=new Gson();
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
                        TeacherAndCourse teacherAndCourse=gson.fromJson(content,TeacherAndCourse.class);
                        courselist=teacherAndCourse.courses;
                        teacherList=teacherAndCourse.teachers;
                        final List<String> teacherName=new ArrayList<String>();
                        teacherName.add("");
                        for (int i=1;i<teacherList.size()+1;i++)
                        {
                            teacherName.add(teacherList.get(i-1).name);
                        }
                        final List<String> courseName=new ArrayList<>();
                            courseName.add("");
                            for (int i=1;i<courselist.size()+1;i++)
                            {

                                courseName.add(courselist.get(i-1).courseName);
                            }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                teachersp.setAdapter(new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,teacherName));
                                coursesp.setAdapter(new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,courseName));
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        dialog.show();
        dialog.setContentView(view);
        dialog.getWindow().setGravity(Gravity.CENTER);
        Button sure= (Button) view.findViewById(R.id.bt_sure);
        Button cancel= (Button) view.findViewById(R.id.bt_cancel);

        teachersp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0)
                {
                    teacherid="0";
                }
                else {
                     teacherid=teacherList.get(position-1).id.toString();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                teacherid="0";
            }
        });
        coursesp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0)
                {
                    courseid="0";
                }
                else {
                     courseid=courselist.get(position-1).id.toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                courseid="0";
            }
        });


        /*
        开始搜索
         */
        sure.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Log.i(">>>>>>>>>>>>>>",courseid+teacherid);
                Utils.showToast(courseid+teacherid,getActivity());
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }
}
