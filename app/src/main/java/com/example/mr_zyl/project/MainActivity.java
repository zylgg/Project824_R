package com.example.mr_zyl.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.mr_zyl.project.pro.attention.view.Attention;
import com.example.mr_zyl.project.pro.base.view.BaseActivity;
import com.example.mr_zyl.project.pro.base.view.MyFragmentTabHost;
import com.example.mr_zyl.project.pro.base.view.residemenu.ResideMenu;
import com.example.mr_zyl.project.pro.base.view.residemenu.resideTouch;
import com.example.mr_zyl.project.pro.essence.refreshEvent;
import com.example.mr_zyl.project.pro.essence.view.essence;
import com.example.mr_zyl.project.pro.mine.view.Mine;
import com.example.mr_zyl.project.pro.mine.view.selfview.MySnackbarUtils;
import com.example.mr_zyl.project.pro.newpost.view.Newpost;
import com.example.mr_zyl.project.pro.publish.view.Publish;
import com.example.mr_zyl.project.pro.publish.view.SimpleTakePhotoActivity;
import com.example.mr_zyl.project.pro.publish.view.self.MoreWindow;
import com.example.mr_zyl.project.utils.SystemAppUtils;
import com.lqr.imagepicker.ImagePicker;
import com.lqr.imagepicker.bean.ImageItem;
import com.lqr.imagepicker.ui.ImageGridActivity;
import com.lqr.imagepicker.ui.ImagePreviewActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import de.greenrobot.event.EventBus;

public class MainActivity extends BaseActivity implements TabHost.OnTabChangeListener, View.OnClickListener {

    private static final String TAG = "MainActivity";
    @BindView(R.id.ll_main_content)
    LinearLayout ll_main_content;

    @BindView(R.id.tv_bottomnavigation_view)
    View tv_bottomnavigation_view;
    @BindView(android.R.id.tabhost)
    MyFragmentTabHost fragmenttabhost;

    private List<Tabitem> tablists;
    private long lasttime;
    private MoreWindow mMoreWindow;
    private int bottomStatusHeight;
    ResideMenu resideMenu;
    @Override
    protected int initLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        //设置不填充到虚拟按键之下
        setNoFitBottomStatus();
        //先初始化每个tab对象的数据
        initFragmentTabData();
        //再初始化TabHost控件
        initTabHost();
        //菜单默认不展开所以拦截掉事件
//        dl_main.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
//            @Override
//            public void onDrawerSlide(View drawerView, float slideOffset) {
//                super.onDrawerSlide(drawerView, slideOffset);
//                View contentView = dl_main.getChildAt(0);
//                float trans = drawerView.getWidth() * slideOffset;
//                //让内容跟着滑动
//                contentView.setTranslationX(trans);
//            }
//        });
        resideMenu = new ResideMenu(this,R.layout.main_left_layout,-1);
        resideMenu.setUse3D(false);
//        resideMenu.setBackground(R.drawable.left_layout_bg);
        resideMenu.attachToActivity(this,ll_main_content);
        resideMenu.setScaleValue(0.55f);
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
    }
    public void onEventMainThread(resideTouch touch){
        if (touch!=null){
            resideMenu.setIsLeftBorder(touch.is_Left());
        }
    }

    /**
     * 设置底部布局填充
     */
    private void setNoFitBottomStatus() {
        bottomStatusHeight = SystemAppUtils.getBottomStatusHeight(this);
        if (bottomStatusHeight > 0) {
            tv_bottomnavigation_view.getLayoutParams().height = bottomStatusHeight;
        }
    }

    private void initFragmentTabData() {
        this.tablists = new ArrayList<>();
        //添加精华Tab
        tablists.add(new Tabitem(R.drawable.main_bottom_essence_normal
                , R.drawable.main_bottom_essence_press, R.string.main_essence_text, essence.class));
        //添加新帖Tab
        tablists.add(new Tabitem(R.drawable.main_bottom_newpost_normal
                , R.drawable.main_bottom_newpost_press, R.string.main_newpost_text, Newpost.class));
        //添加发布Tab
        tablists.add(new Tabitem(R.drawable.main_bottom_public_normal
                , R.drawable.main_bottom_public_press, 0, Publish.class));
        //添加关注Tab
        tablists.add(new Tabitem(R.drawable.main_bottom_attention_normal
                , R.drawable.main_bottom_attention_press, R.string.main_attention_text, Attention.class));
        //添加我的Tab
        tablists.add(new Tabitem(R.drawable.main_bottom_mine_normal
                , R.drawable.main_bottom_mine_press, R.string.main_mine_text, Mine.class));
    }

    /**
     * 当前选中的tab标题
     */
    private String currenttabtag;

    private void initTabHost() {
        fragmenttabhost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        fragmenttabhost.getTabWidget().setDividerDrawable(null);
        for (int i = 0; i < tablists.size(); i++) {
            Tabitem tabItem = tablists.get(i);
            TabHost.TabSpec tabSpec = fragmenttabhost
                    .newTabSpec(tabItem.getTitleString())//添加标题，
                    .setIndicator(tabItem.getview());//添加相应的view布局
            //参数1：选项卡；参数1，选项卡绑定的碎片fragment；参数3，该fragment所携带的bundle数据
            fragmenttabhost.addTab(tabSpec, tabItem.getFragmentclass(), tabItem.getBundle());
            //给我们的Tab按钮设置背景
            fragmenttabhost.getTabWidget().getChildAt(i).setBackgroundColor(getResources().getColor(R.color.main_bottom_bg));
            fragmenttabhost.getTabWidget().getChildAt(i).setId(tabItem.getTitleid());//给每个view设置id
            //监听点击Tab
            fragmenttabhost.setOnTabChangedListener(this);
            //默认选中第一个Tab
            if (i == 0) {
                tabItem.setChecked(true);
                currenttabtag = tabItem.getTitleString();
            }

            View view = fragmenttabhost.getTabWidget().getChildTabViewAt(i);
            View ll_tab_indicator_content = view.findViewById(R.id.ll_tab_indicator_content);
            ll_tab_indicator_content.setOnClickListener(this);
            if (tabItem.getTitleid() == 0) {//只对非fragment跳转的tab 设置自定义监听
                view.setOnClickListener(this);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mMoreWindow != null && mMoreWindow.isShowing()) {
            return;
        }
        if (System.currentTimeMillis() - lasttime < 2000) {
            ((BaseApplication) this.getApplication()).exit(0);
        } else {
            new MySnackbarUtils.Builder(this)
                    .setCoverStatusBar(true)
                    .setMessage("再点击一次退出" + getString(R.string.app_name))
                    .setMessageColor(R.color.white)
                    .show();
//            ToastUtil.showToast(this, "再点击一次退出" + getString(R.string.app_name) + "！");
        }
        lasttime = System.currentTimeMillis();
    }

    @Override
    public void onTabChanged(String tabId) {
        FragmentManager Frmanager = getSupportFragmentManager();
        FragmentTransaction ft = Frmanager.beginTransaction();

        Fragment newfragment = Frmanager.findFragmentByTag("newfragment");
        Fragment selectedfragment = Frmanager.findFragmentByTag(tabId);
        if (newfragment != null && !newfragment.isDetached()) {
            ft.detach(newfragment);
        }
        if (selectedfragment != null) {
            if (selectedfragment.isHidden()) {
                ft.show(selectedfragment);
            }
        }
        ft.commit();

        if (TextUtils.isEmpty(tabId)) {
//            startActivity(new Intent(this, PlayActivity.class));
        }
        //重置Tab样式
        for (int i = 0; i < tablists.size(); i++) {
            Tabitem tabItem = tablists.get(i);
            if (tabId.equals(tabItem.getTitleString())) {
                //选中设置为选中状态
                tabItem.setChecked(true);
            } else {
                //没有选择Tab样式设置为正常
                tabItem.setChecked(false);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == 0) {
            showMoreWindow(v);
            return;
        }
        if (currenttabtag.equals(v.getTag())) {
//            Log.i("TAG", "又一次点击：currenttabtag: "+currenttabtag);
            //可执行刷新数据等操作：
            refreshEvent event = new refreshEvent();
            event.setIs_RefreshCurrent(true);
            EventBus.getDefault().post(event);
        }
        currenttabtag = v.getTag().toString();
    }

    @Override
    public void onNetChange(int netMobile) {
        super.onNetChange(netMobile);
        boolean netConnect = isNetConnect(netMobile);
        if (!netConnect)
//            ToastUtil.showToast(this,"当前网络状态不可用，请检查你的网络设置！");
            new MySnackbarUtils.Builder(this)
                    .setCoverStatusBar(true)
                    .setIcon(R.drawable.red_log)
                    .setBackgroundColor(R.color.white)
                    .setMessage("当前网络状态不可用，请检查你的网络设置！")
                    .setMessageColor(R.color.black)
                    .show();
    }

    /**
     * 图片选择器请求码
     */
    public static final int IMAGE_PICKER = 100;

    /**
     * 显示更多窗口
     *
     * @param view
     */
    private void showMoreWindow(View view) {
        if (null == mMoreWindow) {
            mMoreWindow = new MoreWindow(this, new MoreWindow.MenuOnclickListener() {
                @Override
                public void selectPhoto() {
                    Intent intent = new Intent(MainActivity.this, ImageGridActivity.class);
                    startActivityForResult(intent, IMAGE_PICKER);
                }
            });
            mMoreWindow.init();
        }
        mMoreWindow.showMoreWindow(fragmenttabhost, bottomStatusHeight);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICKER) {
            switch (resultCode) {
                case ImagePicker.RESULT_CODE_ITEMS:
                    if (data != null) {
                        //是否发送原图
                        boolean isOrig = data.getBooleanExtra(ImagePreviewActivity.ISORIGIN, false);
                        ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);

                        Log.e("CSDN_LQR", isOrig ? "发原图" : "不发原图");//若不发原图的话，需要在自己在项目中做好压缩图片算法
                        for (ImageItem imageItem : images) {
                            Log.e("CSDN_LQR", imageItem.path);
                        }
                        Intent intent = new Intent(MainActivity.this, SimpleTakePhotoActivity.class);
                        intent.putExtra("imagepaths", images);
                        startActivity(intent);
                    }
                    break;
            }
        }
    }

    public class Tabitem {
        private int imagedefault;
        private int imageselected;
        private int titleid;
        private String titleString;

        private Class<? extends Fragment> fragmentclass;
        private View view;
        private ImageView imageView;
        private TextView textView;
        private Bundle bundle;

        public Tabitem(int imagedefault, int imageselected, int titleid, Class<? extends Fragment> fragmentclass) {
//			super();
            this.imagedefault = imagedefault;
            this.imageselected = imageselected;
            this.titleid = titleid;
            this.fragmentclass = fragmentclass;
        }

        public int getImagedefault() {
            return imagedefault;
        }

        public int getImageselected() {
            return imageselected;
        }

        public int getTitleid() {
            return titleid;
        }

        public String getTitleString() {
            if (titleid == 0) {
                return "";
            }
            if (TextUtils.isEmpty(titleString)) {
                titleString = getResources().getString(titleid);
            }
            return titleString;
        }

        public Class<? extends Fragment> getFragmentclass() {
            return fragmentclass;
        }

        public Bundle getBundle() {
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putString("title", getTitleString());
            return bundle;
        }

        public void setChecked(boolean ischecked) {
            if (imageView != null) {
                if (ischecked) {//选中切换图案背景
                    imageView.setImageResource(imageselected);
                } else {
                    imageView.setImageResource(imagedefault);
                }
            }
            if (textView != null && titleid != 0) {
                if (ischecked) {//选中切换Tv颜色
                    textView.setTextColor(getResources().getColor(R.color.main_bottom_text_select));
                } else {
                    textView.setTextColor(getResources().getColor(R.color.main_bottom_text_normal));
                }
            }
        }

        public View getview() {
            if (this.view == null) {
                this.view = getLayoutInflater().inflate(R.layout.view_tab_indicator, null);
                View ll_tab_indicator_content = view.findViewById(R.id.ll_tab_indicator_content);
                ll_tab_indicator_content.setTag(getTitleString());
                this.imageView = (ImageView) this.view.findViewById(R.id.iv_tab);
                this.textView = (TextView) this.view.findViewById(R.id.tv_tab);
                if (this.titleid == 0) {//目前只针对那个（加号tab）文字隐藏，以后。。
                    this.textView.setVisibility(View.GONE);
                } else {
                    this.textView.setVisibility(View.VISIBLE);
                    this.textView.setText(getTitleString());
                }
                this.imageView.setImageResource(imagedefault);
            }
            return this.view;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
