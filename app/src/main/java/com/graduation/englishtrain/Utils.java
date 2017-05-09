package com.graduation.englishtrain;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.panxw.android.imageindicator.AutoPlayManager;
import com.panxw.android.imageindicator.ImageIndicatorView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author:jiangbo
 * Time:2017/4/5
 * Description: This is Utils
 */
public class Utils {
    private static  boolean isLogin=false;
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
    /*
 自定义Toast
  */
    public static void showToast(String string, Context context)
    {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.utils_toast,null,false);
        TextView textView= (TextView) view.findViewById(R.id.tv_toast);
        textView.setText(string);
        Toast toast=new Toast(context);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }
    /**
     * 检测当的网络（WLAN、3G/2G）状态
     * @param context Context
     * @return true 表示网络可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected())
            {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED)
                {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * 验证手机号码
     *
     * 移动号码段:139、138、137、136、135、134、150、151、152、157、158、159、182、183、187、188、147、182
     * 联通号码段:130、131、132、136、185、186、145
     * 电信号码段:133、153、180、189、177
     *
     * @param cellphone
     * @return
     */
    public static boolean checkCellphone(String cellphone) {
        String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,2,5-9])|(177))\\d{8}$";
        Pattern pattern=Pattern.compile(regex);
        Matcher matcher=pattern.matcher(cellphone);
        return matcher.matches();
    }
    /*
    *判断是否登录
    *
    */
    public static boolean isLogin()
    {
        final OkHttpClient client=new OkHttpClient();
        String url="http://123.207.19.116/jiangbo/isLogin.do";
        Log.i(">>>>>>>",url);
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
                        Log.i(">>>>>>>",content+"");
                        if (content.contains("1"))
                        {

                         isLogin =true;

                        }
                        else {
                            isLogin =false;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return isLogin;
    }
    /*
    * 将时间戳转换为时间
    */
    public static String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
}
