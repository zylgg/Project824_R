package com.example.mr_zyl.project824.pro.mine.view.activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.RotateAnimation;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.example.mr_zyl.project824.R;
import com.example.mr_zyl.project824.pro.base.view.BaseActivity;
import com.example.mr_zyl.project824.pro.mine.view.selfview.MapContainer;
import com.example.mr_zyl.project824.utils.DensityUtil;
import com.example.mr_zyl.project824.utils.ToastUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.bgabanner.BGABanner;

public class GMapActivity extends BaseActivity implements AMap.OnMyLocationChangeListener, PoiSearch.OnPoiSearchListener, GeocodeSearch.OnGeocodeSearchListener {

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

    @BindView(R.id.rv_poi_searchList)
    RecyclerView rv_poi_searchList;


    //创建覆盖物
    private ArrayList<MarkerOptions> markerOptionsList = new ArrayList<>();
    private GeocodeSearch geocoderSearch;
    @Override
    protected int initLayoutId() {
        return R.layout.activity_gmap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tmv_current_oilStation.onCreate(savedInstanceState);
        Log.i(TAG, "当前编译的APK的SHA1: " + sHA1(this));
        rv_poi_searchList.setVisibility(View.GONE);
        rv_poi_searchList.setLayoutManager(new LinearLayoutManager(this));
        rv_poi_searchList.addItemDecoration(new DividerItemDecoration(this,LinearLayout.VERTICAL));
        rv_poi_searchList.setAdapter(mPoiSearchAdaper);

        //获取地图对象
        aMap = tmv_current_oilStation.getMap();
        //处理滑动冲突
        mc_gmap_container.setScrollView(appbarHome);
        //设置定位属性
        MyLocationStyle style = new MyLocationStyle();
        style.showMyLocation(true);
        style.strokeColor(Color.TRANSPARENT);//蓝点精度圆圈的边框
        style.radiusFillColor(Color.TRANSPARENT);//蓝点精度圆圈的填充
        style.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);

        //设置定位样式并开始定位
        aMap.setMyLocationStyle(style);
        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);//关闭放大 缩小ui
        uiSettings.setMyLocationButtonEnabled(false);// 关闭默认定位按钮ui
        uiSettings.setCompassEnabled(false);//关闭指南针
        uiSettings.setScaleControlsEnabled(false);//控制比例尺控件是否显示
        //设置地理编码
        geocoderSearch=new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);

        isNetworkRequest = false;
        needLocationLatlngWhenMapLoaded = new LatLng(39.906901, 116.397972);//默认北京
        aMap.setMyLocationEnabled(true);
        aMap.setOnMyLocationChangeListener(this);

        //设置infowindow
        aMap.setInfoWindowAdapter(new AMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(final Marker marker) {
                View view = LayoutInflater.from(GMapActivity.this).inflate(R.layout.activity_amap_infowindow, null);
                TextView Title = view.findViewById(R.id.Title);
                Title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ToastUtil.showToast(GMapActivity.this, marker.getTitle());
                    }
                });
                TextView Content = view.findViewById(R.id.Content);
                Content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ToastUtil.showToast(GMapActivity.this, marker.getSnippet());
                    }
                });
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
        cv_oilstation_topdescMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rv_poi_searchList.setVisibility(rv_poi_searchList.isShown()?View.GONE: View.VISIBLE);
            }
        });
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
                isNetworkRequest = true;//标记，等 setOnMapLoadedCallback 中回调完执行
                needLocationLatlngWhenMapLoaded = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                if (isMapLoaded) {
                    //到此如果地图已加载完成，则执行定位缩放显示操作
                    setLocation(needLocationLatlngWhenMapLoaded);
                }
                if (marker.isInfoWindowShown()) {
                    marker.hideInfoWindow();
                } else {
                    marker.showInfoWindow();
                }
                return false;
            }
        });
        //设置地图加载完成后的监听
        aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                isMapLoaded = true;
                if (isNetworkRequest) {
                    setLocation(needLocationLatlngWhenMapLoaded);
                }
            }
        });
        //设置地图改变完成后的监听
//        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
//            @Override
//            public void onCameraChange(CameraPosition cameraPosition) {
//                if (lastPoiMarker!=null){
//                    lastPoiMarker.hideInfoWindow();
//                    lastPoiMarker.destroy();
//                }
//                if (regeocodeSearchMarker!=null){
//                    regeocodeSearchMarker.hideInfoWindow();
//                    regeocodeSearchMarker.destroy();
//                }
//            }
//
//            @Override
//            public void onCameraChangeFinish(CameraPosition cameraPosition) {
//                PoiSearch.Query poiQuery = new PoiSearch.Query("楼|街|店|区|油站|公寓", null, "北京");
////                "汽车服务|汽车销售|\n" +
////                        "  汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|\n" +
////                        "  住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|\n" +
////                        "  金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施"
//                LatLng currentPoiCenter=cameraPosition.target;
//                if (lastPoiMarker!=null){
//                    lastPoiMarker.destroy();
//                }
//                Marker marker = aMap.addMarker(new MarkerOptions().position(currentPoiCenter).title("当前位置:").snippet("定位中").icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marka)));
//                marker.showInfoWindow();
//                lastPoiMarker=marker;
//                //查询当前地址
//                getAddressByLatlng(currentPoiCenter);
//
//                //搜索附近的地点
//                poiQuery.setPageSize(10);
//                poiQuery.setDistanceSort(true);
//                setPoiSearch(poiQuery,cameraPosition.target);
//            }
//        });
    }

    /**
     * 根据经纬度得到地址
     */
    private void getAddressByLatlng(LatLng latLng) {
        //逆地理编码查询条件：逆地理编码查询的地理坐标点、查询范围、坐标类型。
        LatLonPoint latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 500f, GeocodeSearch.AMAP);
        //异步查询
        geocoderSearch.getFromLocationAsyn(query);
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == 1000) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {

                String addressName = result.getRegeocodeAddress().getFormatAddress(); // 逆转地里编码不是每次都可以得到对应地图上的opi
                Log.e(TAG,"逆地理编码回调  得到的地址：" + addressName);

            }
        }

    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    /**
     * 上一个poi定位的覆盖物
     */
    private Marker lastPoiMarker,regeocodeSearchMarker;

    private void setPoiSearch(PoiSearch.Query query,LatLng point) {
        PoiSearch poiSearch = new PoiSearch(GMapActivity.this, query);
        poiSearch.setOnPoiSearchListener(this);

        PoiSearch.SearchBound bound = new PoiSearch.SearchBound(new LatLonPoint(point.latitude, point.longitude), 1*1000);
        poiSearch.setBound(bound);
        poiSearch.searchPOIAsyn();
    }

    /**
     * 地图是否加载过
     */
    private boolean isMapLoaded = false;
    /**
     * 定位请求是否记录过
     */
    private boolean isNetworkRequest = false;
    /**
     * 当地图加载完后 需要定位的坐标
     */
    private LatLng needLocationLatlngWhenMapLoaded;

    @Override
    public void onMyLocationChange(Location location) {
        // 定位回调监听
        if (location != null) {
            Log.e("onMyLocationChange", "onMyLocationChange 定位成功， lat: " + location.getLatitude() + " lon: " + location.getLongitude());
            Bundle bundle = location.getExtras();
            if (bundle != null) {
//                aMap.setMyLocationEnabled(false);
                int errorCode = bundle.getInt(MyLocationStyle.ERROR_CODE);
                String errorInfo = bundle.getString(MyLocationStyle.ERROR_INFO);
                // 定位类型，可能为GPS WIFI等，具体可以参考官网的定位SDK介绍
                int locationType = bundle.getInt(MyLocationStyle.LOCATION_TYPE);
                final LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));

                isNetworkRequest = false;
                tmv_current_oilStation.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 10; i++) {
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(new LatLng(latLng.latitude + 0.0004 * i, latLng.longitude + 0.0004 * i))
                                    .title("北京")
                                    .draggable(true)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marka))
                                    .snippet("默认的marker" + i);
                            markerOptionsList.add(markerOptions);
                        }
                        //添加覆盖物
                        for (int i=0;i<markerOptionsList.size();i++){
                            if (i==0){
                                Marker marker = aMap.addMarker(markerOptionsList.get(0));
                                marker.showInfoWindow();
                            }else{
                                aMap.addMarker(markerOptionsList.get(i));
                            }
                        }
                        //定位第一个点
                        if (markerOptionsList.size() > 0) {
                            MarkerOptions markerOptions = markerOptionsList.get(0);
                            isNetworkRequest = true;//标记，等 setOnMapLoadedCallback 中回调完执行
                            needLocationLatlngWhenMapLoaded = markerOptions.getPosition();
                            if (isMapLoaded) {
                                setLocation(markerOptions.getPosition());
                            }
                        }

                    }
                }, 1500);
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
        setLocation(ll, false);
    }


    private void setLocation(LatLng ll, boolean isZoom) {
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
        if (isZoom) {
            aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        } else {
            aMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }

    private List pois=new ArrayList<PoiItem>();
    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        Log.i(TAG, "onPoiSearched: " + poiResult + "i:" + i);
        if (i == 1000) {
            if (poiResult != null && poiResult.getPois() != null && poiResult.getPois().size() > 0) {
//                setMyLocationMapCenter(new LatLng(poiResult.getPois().get(0).getLatLonPoint().getLatitude(),poiResult.getPois().get(0).getLatLonPoint().getLongitude()));
//                show(poiResult.getPois().get(0));
                rv_poi_searchList.setVisibility(View.VISIBLE);
                pois.clear();
                pois.addAll(poiResult.getPois());
                Log.i(TAG, "onPoiSearched: " + poiResult.getPois().get(0).getTitle() + "i:" + i);
                rv_poi_searchList.getAdapter().notifyDataSetChanged();

                if (regeocodeSearchMarker!=null){
                    regeocodeSearchMarker.destroy();
                }
                PoiItem poiItem = poiResult.getPois().get(0);
                Marker marker = aMap.addMarker(new MarkerOptions().position(lastPoiMarker.getPosition()).title("当前位置:").snippet(poiItem.getTitle())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marka)));
                marker.showInfoWindow();
                regeocodeSearchMarker=marker;

            } else {
                rv_poi_searchList.setVisibility(View.GONE);
            }
        }
    }

    RecyclerView.Adapter<GMapActivity.poiSearchHolder> mPoiSearchAdaper = new RecyclerView.Adapter<GMapActivity.poiSearchHolder>() {
        @android.support.annotation.NonNull
        @Override
        public poiSearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(GMapActivity.this).inflate(android.R.layout.simple_list_item_2, null);
            poiSearchHolder poiSearchHolder = new poiSearchHolder(view);
            return poiSearchHolder;
        }

        @Override
        public void onBindViewHolder(poiSearchHolder holder, int position) {
            PoiItem poiItem = (PoiItem) pois.get(position);
            TextView text1 = holder.itemView.findViewById(android.R.id.text1);
            int distance = poiItem.getDistance();
            String dist=distance>=1000?(distance*1.0f/1000+"km"):(distance+"m");
            text1.setText(poiItem.getTitle()+"("+dist+")");
            TextView text2 = holder.itemView.findViewById(android.R.id.text2);
            text2.setText(poiItem.getSnippet());
        }

        @Override
        public int getItemCount() {
            return pois.size();
        }

    };


    class poiSearchHolder extends RecyclerView.ViewHolder {

        public poiSearchHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

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
