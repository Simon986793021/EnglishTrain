package com.graduation.englishtrain.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.graduation.englishtrain.R;
import com.graduation.englishtrain.Utils;
import com.graduation.englishtrain.base.BaseFragment;
import com.panxw.android.imageindicator.ImageIndicatorView;


/**
 * Author:jiangbo
 * Time:2017/4/5
 * Description: This is TrainFragment
 */
public class TrainFragment extends BaseFragment {
    private ImageIndicatorView imageIndicatorView;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frament_train,container,false);
        imageIndicatorView= (ImageIndicatorView) view.findViewById(R.id.indicate_view);
        Utils.loadImage(imageIndicatorView);
        return view;
    }
}
