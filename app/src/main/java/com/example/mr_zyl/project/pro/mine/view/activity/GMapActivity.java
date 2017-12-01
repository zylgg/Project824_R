package com.example.mr_zyl.project.pro.mine.view.activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.RotateAnimation;
import com.example.mr_zyl.project.R;
import com.example.mr_zyl.project.pro.base.view.BaseActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GMapActivity extends BaseActivity implements AMap.OnMyLocationChangeListener {

    private static final String TAG = "GMapActivity";
    @BindView(R.id.mv_gmap)
    MapView mv_gmap;
    private AMap aMap;
    private Marker marker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mv_gmap.onCreate(savedInstanceState);
        Log.i(TAG, "当前编译的APK的SHA1: " + sHA1(this));
        //获取地图对象
        aMap = mv_gmap.getMap();
        //设置定位属性
        MyLocationStyle style = new MyLocationStyle();
        style.showMyLocation(true);
        style.interval(2500);
        style.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);

         //设置定位样式并开始定位
        aMap.setMyLocationStyle(style);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);
        aMap.setOnMyLocationChangeListener(this);
        //创建覆盖物
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(39.906901, 116.397972))
                .title("北京")
                .draggable(true)
//                .infoWindowEnable(false)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marka))
                .snippet("默认的marker");
        marker = aMap.addMarker(markerOptions);
        //设置动画并开始
        Animation markerAnimation=new RotateAnimation(marker.getRotateAngle(),marker.getRotateAngle()+360);
        markerAnimation.setDuration(2000);
        markerAnimation.setInterpolator(new LinearInterpolator());
        marker.setAnimation(markerAnimation);
        marker.startAnimation();
        //自定义infowindow
        aMap.setInfoWindowAdapter(new AMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                View  view= LayoutInflater.from(GMapActivity.this).inflate(R.layout.activity_amap_infowindow,null);
                TextView Title = ButterKnife.findById(view, R.id.Title);
                TextView Content=ButterKnife.findById(view,R.id.Content);
                Title.setText(marker.getTitle());
                Content.setText(marker.getSnippet());
                return view;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });
        //设置覆盖物监听
        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.isInfoWindowShown()){
                    marker.hideInfoWindow();
                }else{
                    marker.showInfoWindow();
                }
                Log.i(TAG, "onMarkerClick: " + marker.getPosition().latitude+"——"+ marker.getPosition().longitude);
                return false;
            }
        });
        //设置地图的单击
        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (marker.isInfoWindowShown()){
                    marker.hideInfoWindow();
                }else{
                    marker.showInfoWindow();
                }
            }
        });
        //设置地图的点击事件
        aMap.setOnInfoWindowClickListener(new AMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if (marker.isInfoWindowShown()){
                    marker.hideInfoWindow();
                }else{
                    marker.showInfoWindow();
                }
            }
        });
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

    public static String sHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length() - 1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onMyLocationChange(Location location) {
        // 定位回调监听
        if (location != null) {
            Log.e("onMyLocationChange", "onMyLocationChange 定位成功， lat: " + location.getLatitude() + " lon: " + location.getLongitude());
            Bundle bundle = location.getExtras();
            if (bundle != null) {
                int errorCode = bundle.getInt(MyLocationStyle.ERROR_CODE);
                String errorInfo = bundle.getString(MyLocationStyle.ERROR_INFO);
                // 定位类型，可能为GPS WIFI等，具体可以参考官网的定位SDK介绍
                int locationType = bundle.getInt(MyLocationStyle.LOCATION_TYPE);

                /*
                errorCode
                errorInfo
                locationType
                */
                Log.e("onMyLocationChange", "定位信息， code: " + errorCode + " errorInfo: " + errorInfo + " locationType: " + locationType);
            } else {
                Log.e("onMyLocationChange", "定位信息， bundle is null ");

            }

        } else {
            Log.e("onMyLocationChange", "定位失败");
        }
    }
}
