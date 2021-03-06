package com.graduation.englishtrain.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;

import com.graduation.englishtrain.R;
import com.graduation.englishtrain.Utils;
import com.graduation.englishtrain.fragment.HomeFragment;
import com.graduation.englishtrain.fragment.MyFragment;
import com.graduation.englishtrain.fragment.TrainFragment;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private RadioGroup radioGroup;
    int curCursor;
    private Fragment homeFragment = new HomeFragment();
    private Fragment trainFragment = new TrainFragment();
    private Fragment myFragment = new MyFragment();
    private List<Fragment> fragmentList = Arrays.asList(homeFragment,trainFragment,myFragment);
    private FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
            initFootBar();
    }
    private void initFootBar() {
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.foot_bar_home:
                        curCursor = 0;
                        break;
                    case R.id.foot_bar_train:
                        curCursor = 1;
                        break;
                    case R.id.foot_bar_my:
                        SharedPreferences sp=getSharedPreferences("cookie", Context.MODE_PRIVATE);
                        String username=sp.getString("username",null);
                        String password=sp.getString("password", null);
                        if (username!=null&&password!=null)
                        {
                            curCursor = 2;
                        }
                      //  if (Utils.isLogin(MainActivity.this)){

                        //}

                        else {
                            Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                            startActivity(intent);
                        }
                        break;
                }
                addFragmentToStack(curCursor);
            }

        });
        addFragmentToStack(0);
    }

    private void addFragmentToStack(int cursor) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentList.get(cursor);
        if (!fragment.isAdded()) {
            fragmentTransaction.add(R.id.fl_content, fragment);
        }
        for (int i = 0; i < fragmentList.size(); i++) {
            Fragment f = fragmentList.get(i);
            if (i == cursor && f.isAdded()) {
                fragmentTransaction.show(f);
            } else if (f != null && f.isAdded() && f.isVisible()) {
                fragmentTransaction.hide(f);
            }
        }
        fragmentTransaction.commitAllowingStateLoss();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = fragmentList.get(curCursor);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
