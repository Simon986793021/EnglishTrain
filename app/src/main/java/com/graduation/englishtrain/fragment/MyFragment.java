package com.graduation.englishtrain.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.graduation.englishtrain.R;
import com.graduation.englishtrain.activity.MyinfoActivity;
import com.graduation.englishtrain.base.BaseFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Author:jiangbo
 * Time:2017/4/5
 * Description: This is MyFragment
 */
public class MyFragment extends BaseFragment implements AdapterView.OnItemClickListener{
    private ListView listView;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_my,container,false);
        listView= (ListView) view.findViewById(R.id.lv_fragment_my);

        SimpleAdapter adapter=new SimpleAdapter(getActivity(),getData(),R.layout.listview_fragment_my,new String[]{"leftImage","centerText","rightImage"},new int[]{R.id.im_listview_left,R.id.tv_listview_center,R.id.im_listview_right});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        return  view;
    }

    private List<Map<String,Object>> getData(){
        List list=new ArrayList();
        Map<String,Object> map=new HashMap<>();
        map.put("leftImage",R.drawable.myinfo);
        map.put("centerText","我的资料");
        map.put("rightImage",R.drawable.gogogo);
        list.add(map);

        map=new HashMap<>();
        map.put("leftImage",R.drawable.loginout);
        map.put("centerText","退出登录");
        map.put("rightImage",R.drawable.gogogo);
        list.add(map);

        return  list;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position)
        {
            case 0:
                Intent infointent=new Intent(mactivity,MyinfoActivity.class);
                startActivity(infointent);
                break;
        }
    }
}
