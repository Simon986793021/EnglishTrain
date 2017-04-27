package com.graduation.englishtrain.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.graduation.englishtrain.R;

/**
 * Created by jiangbo on 2017/4/6.
 */

public class MyinfoActivity extends Activity implements View.OnClickListener{
    private TextView back;
    private TextView toolbarcenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        back= (TextView) findViewById(R.id.tv_back);
        toolbarcenter= (TextView) findViewById(R.id.tv_activity_toolbar_center);
        toolbarcenter.setText("我的资料");
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
