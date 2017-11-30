package com.example.mr_zyl.project.pro.mine.view.activity;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.MyLocationStyle;
import com.example.mr_zyl.project.R;
import com.example.mr_zyl.project.pro.base.view.BaseActivity;

import butterknife.BindView;

public class GMapActivity extends BaseActivity implements AMap.OnMyLocationChangeListener {

    private static final String TAG = "GMapActivity";
    @BindView(R.id.mv_gmap)
    MapView mv_gmap;
    private AMap aMap;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            Log.i(TAG, "onLocationChanged: "+aMapLocation);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mv_gmap.onCreate(savedInstanceState);
//        //初始化定位
//        mLocationClient = new AMapLocationClient(getApplicationContext());
//        //初始化AMapLocationClientOption对象
//        mLocationOption = new AMapLocationClientOption();
//        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        //设置是否返回地址信息（默认返回地址信息）
//        mLocationOption.setNeedAddress(true);
//        //设置定位回调监听
//        mLocationClient.setLocationListener(mLocationListener);
//        //给定位客户端对象设置定位参数
//        mLocationClient.setLocationOption(mLocationOption);
//        //启动定位
//        mLocationClient.startLocation();

        aMap = mv_gmap.getMap();
        MyLocationStyle style=new MyLocationStyle();
        style.showMyLocation(true);
        style.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);
        style.interval(2000);
        aMap.setMyLocationStyle(style);
        aMap.setMyLocationEnabled(true);
        aMap.setOnMyLocationChangeListener(this);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_gmap;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mv_gmap.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mv_gmap.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mv_gmap.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mv_gmap.onDestroy();
    }

    @Override
    public void onMyLocationChange(Location location) {
        Log.i(TAG, "onMyLocationChange: "+location.getProvider());
    }
}
