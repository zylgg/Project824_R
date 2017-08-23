package com.example.mr_zyl.project.pro.mine.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.mr_zyl.project.R;

public class BaiduMapActivity extends AppCompatActivity {

    MapView mMapView = null;
    BaiduMap mBaiduMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_baidu_map);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);

        mBaiduMap = mMapView.getMap();
//        //普通地图
//        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        LatLng ll = new LatLng(39.92,116.46);
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
        mBaiduMap.setMapStatus(u);
//        //卫星地图
//        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
//        //空白地图, 基础地图瓦片将不会被渲染。在地图类型中设置为NONE，将不会使用流量下载基础地图瓦片图层。
//        // 使用场景：与瓦片图层一起使用，节省流量，提升自定义瓦片图下载速度。
//        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NONE);
//        //开启交通图
//        mBaiduMap.setTrafficEnabled(true);
//        //开启r热力图
//        mBaiduMap.setBaiduHeatMapEnabled(true);

        //定义Maker坐标点
        LatLng point = new LatLng(39.963175, 116.400244);
         //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_marka);
            //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)  //设置marker的位置
                .icon(bitmap)  //设置marker图标
                .zIndex(9)  //设置marker所在层级
                .draggable(true);  //设置手势拖拽
           //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);

//// 位置
//        MarkerOptions markerOptions = null;
//        // 取消放大缩小
////		mMapView.showZoomControls(false);
//        // 准备 marker 的图片
//        BitmapDescriptor bitmap = BitmapDescriptorFactory
//                .fromResource(R.drawable.mark);
//        // 准备 marker option 添加 marker 使用
//        markerOptions = new MarkerOptions().position(p).icon(bitmap).zIndex(5);
//        // 初始化view
//        // 创建标注
//        // 　　 mBaiduMap.addOverlay(marker);
//        MapStatus mMapStatus = new MapStatus.Builder().target(p).zoom(15)
//                .build();
//        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
//                .newMapStatus(mMapStatus);
//
//        mBaiduMap = mMapView.getMap();
//        mBaiduMap.setMapStatus(mMapStatusUpdate);
//        // 获取添加的 marker 这样便于后续的操作
//        marker = (Marker) mBaiduMap.addOverlay(markerOptions);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
}
