package com.example.mr_zyl.project824.utils;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyUtils {
    // 将 instance 变量声明成 volatile,主要原因是其另一个特性：禁止指令重排序优化   http://blog.csdn.net/wzgiceman/article/details/51809985
    private volatile static RequestQueue requestQueue;

    public static RequestQueue getQueue(Context context) {
        if (requestQueue == null) {//第一次检查
            synchronized (VolleyUtils.class) {//当多个线程调用getInstance方法时，可能会创建多个实例，因此需要对其进行同步。
                //第二次判断（执行两次检测很有必要：当多线程调用时，如果多个线程同时执行完了第一次检查，其中一个进入同步代码块创建了实例，后面的线程因第二次检测不会创建新实例。）
                if (requestQueue == null) {
                    requestQueue = Volley.newRequestQueue(context.getApplicationContext());
                }
            }
        }
        return requestQueue;
    }

    public interface Callback{
        void onError(String error);
        void onSuccess(String result);
    }

}
