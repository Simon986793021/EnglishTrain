package com.graduation.englishtrain.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.graduation.englishtrain.R;
import com.graduation.englishtrain.Utils;
import com.graduation.englishtrain.activity.LoginActivity;
import com.graduation.englishtrain.activity.MyLessonActivity;
import com.graduation.englishtrain.activity.MyinfoActivity;
import com.graduation.englishtrain.base.BaseFragment;

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
 * Description: This is MyFragment
 */
public class MyFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private ListView listView;
    private OkHttpClient client=new OkHttpClient();
    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        listView = (ListView) view.findViewById(R.id.lv_fragment_my);
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), getData(), R.layout.listview_fragment_my, new String[]{"leftImage", "centerText", "rightImage"}, new int[]{R.id.im_listview_left, R.id.tv_listview_center, R.id.im_listview_right});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        return view;
    }

    private List<Map<String, Object>> getData() {
        List list = new ArrayList();
        Map<String, Object> map = new HashMap<>();
        map.put("leftImage", R.drawable.myinfo);
        map.put("centerText", "我的资料");
        map.put("rightImage", R.drawable.gogogo);
        list.add(map);

        map = new HashMap<>();
        map.put("leftImage", R.drawable.ordercourse);
        map.put("centerText", "我的预约");
        map.put("rightImage", R.drawable.gogogo);
        list.add(map);

        map = new HashMap<>();
        map.put("leftImage", R.drawable.loginout);
        map.put("centerText", "退出登录");
        map.put("rightImage", R.drawable.gogogo);
        list.add(map);



        return list;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                if (Utils.isNetworkAvailable(mactivity))
                {
                    Intent infointent = new Intent(mactivity, MyinfoActivity.class);
                    startActivity(infointent);
                }
                else {
                    Utils.showToast("请检查网络",mactivity);
                }
                break;
            case 1:
                if (Utils.isNetworkAvailable(mactivity))
                {
                    Intent intent=new Intent(mactivity,MyLessonActivity.class);
                    startActivity(intent);
                }
                else {
                    Utils.showToast("请检查网络",mactivity);
                }
                break;
            case 2:
                if (Utils.isNetworkAvailable(getActivity()))
                {
                    quitDialog();
                }
                else {
                    Utils.showToast("请检查网络",getActivity());
                }
                break;
            default:
                break;
        }
    }

    /*
              退出登录  对话框
               */
    private void quitDialog() {
        final AlertDialog quitDialog = new AlertDialog.Builder(getActivity()).create();
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.loginout, null, false);
        quitDialog.show();
                /*
                 *  直接从xml设置dialog不能铺满整个宽度 ，通过以下代码设置
                 */
        Window window = quitDialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.FILL_PARENT;
        window.setAttributes(layoutParams);

        quitDialog.setContentView(view);
        quitDialog.getWindow().setGravity(Gravity.BOTTOM);

        TextView logoutTextView = (TextView) view.findViewById(R.id.tv_logout);
        TextView cancelTextView = (TextView) view.findViewById(R.id.tv_cancel);

        logoutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitDialog.cancel();
                SharedPreferences sp=getActivity().getSharedPreferences("cookie", Context.MODE_PRIVATE);
                String username=sp.getString("username",null);
                String password=sp.getString("password", null);
                String url="http://123.207.19.116/jiangbo/loginOut.do";
                final Request request=new Request.Builder()
                        .get()
                        .url(url)
                        .header("Cookie", "userName="+username+"; password="+password)
                        .build();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Response response;
                        try {
                           response=client.newCall(request).execute();
                            if(response.isSuccessful())
                            {
                                String content=response.body().string();
                                if (content.contains("login out successful"))
                                {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent=new Intent(getActivity(), LoginActivity.class);
                                            startActivity(intent);
                                            Utils.showToast("退出成功",getActivity());
                                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("cookie", Context.MODE_PRIVATE);
                                            sharedPreferences.edit().clear().commit();
                                        }
                                    });
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });

        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitDialog.cancel();
            }
        });


    }
}