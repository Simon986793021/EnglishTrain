package com.graduation.englishtrain.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.graduation.englishtrain.R;
import com.graduation.englishtrain.model.CourseList;

import java.util.List;

/**
 * Created by jiangbo on 2017/5/5.
 */

public class CourseListAdapter extends BaseAdapter {
    public static List<CourseList.Course> list;
    private Context context;
    public CourseListAdapter(List<CourseList.Course> list,Context context)
    {
        this.context=context;
        this.list=list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view==null)
        {
            viewHolder=new ViewHolder();
            view=LayoutInflater.from(context).inflate(R.layout.item_fragment_course,null,false);
            viewHolder.title= (TextView) view.findViewById(R.id.tv_title);
            viewHolder.content= (TextView) view.findViewById(R.id.tv_content);
            viewHolder.imageView= (ImageView) view.findViewById(R.id.iv_title);
            view.setTag(viewHolder);
        }
            viewHolder= (ViewHolder) view.getTag();
            String url="http://123.207.19.116/jiangbo/getImg.do?img="+list.get(i).img1;
            Glide.with(context).load(url)
                    .error(R.drawable.error)
                    .into(viewHolder.imageView);//Glide 加载图片
            viewHolder.title.setText(list.get(i).courseName);
            viewHolder.content.setText(list.get(i).content);
        return view;
    }
    private class ViewHolder
    {
        private TextView title;
        private TextView content;
        private ImageView imageView;
    }
}
