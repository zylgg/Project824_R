package com.example.mr_zyl.project824;


import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;


public class AppReceiver extends BroadcastReceiver {
    private final String TAG = this.getClass().getSimpleName();
    private ComponentName defaultComponent;
    private ComponentName testComponent;
    private PackageManager packageManager;
    @Override
    public void onReceive(Context context, Intent intent) {
        PackageManager pm = context.getPackageManager();
//        //拿到我们注册的MainActivity组件
//        defaultComponent = new ComponentName(context, "com.example.mr_zyl.project824.LaunchActivity");  //拿到默认的组件
//        //拿到我注册的别名test组件
//        testComponent = new ComponentName(context, "com.fu.changeicondemo.test");
//        packageManager = context.getPackageManager();

        if (TextUtils.equals(intent.getAction(), Intent.ACTION_PACKAGE_ADDED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
//            LogUtils.printInfo(TAG, "--------安装成功" + packageName);
            Toast.makeText(context, "安装成功" + packageName, Toast.LENGTH_LONG).show();

        } else if (TextUtils.equals(intent.getAction(), Intent.ACTION_PACKAGE_REPLACED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
//            LogUtils.printInfo(TAG, "--------替换成功" + packageName);
            Toast.makeText(context, "替换成功" + packageName, Toast.LENGTH_LONG).show();

        } else if (TextUtils.equals(intent.getAction(), Intent.ACTION_PACKAGE_REMOVED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
//            LogUtils.printInfo(TAG, "--------卸载成功" + packageName);
            Toast.makeText(context, "卸载成功" + packageName, Toast.LENGTH_LONG).show();
        }
    }

    public void changeIcon() {
        disableComponent(defaultComponent);
        enableComponent(testComponent);
    }


    /**
     * 启用组件
     *
     * @param componentName
     */
    private void enableComponent(ComponentName componentName) {
        int state = packageManager.getComponentEnabledSetting(componentName);
        if (state == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
            //已经启用
            return;
        }
        packageManager.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    /**
     * 禁用组件
     *
     * @param componentName
     */
    private void disableComponent(ComponentName componentName) {
        int state = packageManager.getComponentEnabledSetting(componentName);
        if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED) {
            //已经禁用
            return;
        }
        packageManager.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

}
