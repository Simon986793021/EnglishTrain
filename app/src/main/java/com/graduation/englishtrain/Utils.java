package com.graduation.englishtrain;


import com.panxw.android.imageindicator.AutoPlayManager;
import com.panxw.android.imageindicator.ImageIndicatorView;

/**
 * Author:jiangbo
 * Time:2017/4/5
 * Description: This is Utils
 */
public class Utils {

    //广告栏图片加载
    public static void loadImage(ImageIndicatorView imageIndicatorView) {
        // 声明一个数组, 指定图片的ID
        final Integer[] resArray = new Integer[] {R.drawable.home_banner01, R.drawable.home_banner02,
                R.drawable.home_banner03, R.drawable.home_banner04};
        // 把数组交给图片展播组件
        imageIndicatorView.setupLayoutByDrawable(resArray);
        // 展播的风格
//        indicate_view.setIndicateStyle(ImageIndicatorView.INDICATE_ARROW_ROUND_STYLE);
        imageIndicatorView.setIndicateStyle(ImageIndicatorView.INDICATE_USERGUIDE_STYLE);
        // 显示组件
        imageIndicatorView.show();
        final AutoPlayManager autoBrocastManager = new AutoPlayManager(imageIndicatorView);
        //设置开启自动广播
        autoBrocastManager.setBroadcastEnable(true);
        //设置开始时间和间隔时间
        autoBrocastManager.setBroadcastTimeIntevel(3000, 3000);
        //设置循环播放
        autoBrocastManager.loop();
    }
}
