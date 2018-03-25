package com.example.mr_zyl.project824.pro.mine.view;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.mr_zyl.project824.utils.ToastUtil;

/**
 * Created by TFHR02 on 2016/12/15.
 */
public class MediaService extends Service {
    private String TAG="MediaService";
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        String command= intent.getStringExtra("command");
        ToastUtil.showToast(this,command);
        Log.i(TAG, "onBind: "+command);
    }
}
