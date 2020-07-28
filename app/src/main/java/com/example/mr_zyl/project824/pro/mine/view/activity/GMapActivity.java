package com.example.mr_zyl.project824.pro.mine.view.activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.RotateAnimation;
import com.example.mr_zyl.project824.R;
import com.example.mr_zyl.project824.pro.base.view.BaseActivity;
import com.example.mr_zyl.project824.pro.mine.view.selfview.MapContainer;
import com.example.mr_zyl.project824.utils.DensityUtil;
import com.example.mr_zyl.project824.utils.ToastUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.bgabanner.BGABanner;

public class GMapActivity extends BaseActivity implements AMap.OnMyLocationChangeListener {

    private static final String TAG = "GMapActivity";
    @BindView(R.id.tmv_current_oilStation)
    TextureMapView tmv_current_oilStation;
    private AMap aMap;
    /**
     * 头部区域
     */
    @BindView(R.id.ll_homeTopLayout)
    LinearLayout ll_homeTopLayout;
    /**
     * 顶部轮播图
     */
    @BindView(R.id.banner_home_top)
    BGABanner bannerHomeTop;
    /**
     * 加油地图
     */
    @BindView(R.id.rl_oilstation_mapContent)
    RelativeLayout rl_oilstation_mapContent;
    @BindView(R.id.mc_gmap_container)
    MapContainer mc_gmap_container;
    /**
     * 一键加油卡片区域
     */
    @BindView(R.id.ll_oilstatiion_contentLayout)
    LinearLayout ll_oilstatiion_contentLayout;
    /**
     * 更多网点
     */
    @BindView(R.id.cv_oilstation_topdescMore)
    CardView cv_oilstation_topdescMore;
    @BindView(R.id.appbar_home)
    AppBarLayout appbarHome;


    // 下拉刷新

    private Marker marker;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_gmap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tmv_current_oilStation.onCreate(savedInstanceState);
        Log.i(TAG, "当前编译的APK的SHA1: " + sHA1(this));

        //获取地图对象
        aMap = tmv_current_oilStation.getMap();
        //处理滑动冲突
        mc_gmap_container.setScrollView(appbarHome);
        //设置定位属性
        MyLocationStyle style = new MyLocationStyle();
        style.showMyLocation(true);
        style.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);

         //设置定位样式并开始定位
        aMap.setMyLocationStyle(style);
        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);//关闭放大 缩小ui
        uiSettings.setMyLocationButtonEnabled(false);// 关闭默认定位按钮ui
        uiSettings.setCompassEnabled(false);//关闭指南针
        uiSettings.setScaleControlsEnabled(false);//控制比例尺控件是否显示

        aMap.setMyLocationEnabled(true);
        aMap.setOnMyLocationChangeListener(this);


        //创建覆盖物
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(39.906901, 116.397972))
                .title("北京")
                .draggable(true)
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
            public View getInfoWindow(final Marker marker) {
                View  view= LayoutInflater.from(GMapActivity.this).inflate(R.layout.activity_amap_infowindow,null);
                TextView Title =view.findViewById(R.id.Title);
                Title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ToastUtil.showToast(GMapActivity.this,marker.getTitle());
                    }
                });
                TextView Content=view.findViewById(R.id.Content);
                Title.setText(marker.getTitle());
                Content.setText(marker.getSnippet());
                return view;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });
        initListener();
    }


    /**
     * 中间空出的地图高度
     */
    private int spaceMapH = 0;
    /**
     * 初始化监听
     */
    private void initListener() {
//        initAppbar();
        appbarHome.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                float offsetY = Math.abs(i * 1.0f);
                int TopStartScrollY = spaceMapH;//空出的地图高度
                if (i < -TopStartScrollY) {//例如：-151<-150
                    ll_homeTopLayout.setTranslationY(i + TopStartScrollY);
                    rl_oilstation_mapContent.setTranslationY(i + TopStartScrollY);
                } else {
                    float rate = offsetY / TopStartScrollY;//设置提示语布局渐变openGaoDeMap
                    cv_oilstation_topdescMore.setAlpha(1 - rate);

                    ll_homeTopLayout.setTranslationY(0);
                    rl_oilstation_mapContent.setTranslationY(0);
                }

            }
        });
        //计算轮播图和金刚区的总高度
        bannerHomeTop.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        //只需要获取一次高度，获取后移除监听器
                        bannerHomeTop.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        int height = bannerHomeTop.getHeight();//轮播图高度

                        //设置一键加油地图 上边距
                        CollapsingToolbarLayout.LayoutParams layoutParams = (CollapsingToolbarLayout.LayoutParams) rl_oilstation_mapContent.getLayoutParams();
                        int marginTop1 = height + DensityUtil.dp2px(68);//轮播图➕金刚区高度
                        layoutParams.topMargin = marginTop1;
                        rl_oilstation_mapContent.setLayoutParams(layoutParams);

                        //设置一键加油卡片 上边距
                        CollapsingToolbarLayout.LayoutParams layoutParams2 = (CollapsingToolbarLayout.LayoutParams) ll_oilstatiion_contentLayout.getLayoutParams();
                        spaceMapH = rl_oilstation_mapContent.getHeight() - ll_oilstatiion_contentLayout.getChildAt(0).getHeight(); //空出的地图区域(地图高-地图上卡片高)
                        layoutParams2.topMargin = marginTop1 + spaceMapH;//轮播图➕金刚区高度➕空出的地图区域(地图高-地图上卡片高)
                        ll_oilstatiion_contentLayout.setLayoutParams(layoutParams2);
                    }

                });


        //设置覆盖物监听
        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });
        //设置地图的单击
        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
//                if (marker.isInfoWindowShown()){
//                    marker.hideInfoWindow();
//                }else{
//                    marker.showInfoWindow();
//                }
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
                LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
                setLocation(latLng);
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

    /**
     * 将位置设置地图中心
     *
     * @param ll
     */
    private void setLocation(LatLng ll) {
        if (ll_oilstatiion_contentLayout == null) {//修改app后台被系统销毁时 定位后视图null不处理
            return;
        }
        //获取地图可视范围(地图的真实宽高目前等于[屏幕宽高])
        int MapHeight = DensityUtil.dp2px(340);
        int visibilityBoundH = MapHeight - ll_oilstatiion_contentLayout.getChildAt(0).getHeight(); //空出的地图区域(地图高-地图上卡片高);
        final float offsetY = (float) (MapHeight * 0.5 - visibilityBoundH * 0.5f);
        //重新定位
        Point point = aMap.getProjection().toScreenLocation(ll);
        point.y = (int) (point.y + offsetY - DensityUtil.dp2px(0));//（40dp）避免太靠上往下移一点

        LatLng latLng = aMap.getProjection().fromScreenLocation(point);
        aMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    /**
     * 防止AppBarLayout头部滑动不了，需要在数据加载出来后调用该方法
     */
    public void initAppbar() {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appbarHome.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        if (behavior != null) {
            behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
                @Override
                public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                    return true;
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        tmv_current_oilStation.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        tmv_current_oilStation.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        tmv_current_oilStation.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tmv_current_oilStation.onDestroy();
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

}
