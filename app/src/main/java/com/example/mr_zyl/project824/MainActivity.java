package com.example.mr_zyl.project824;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.example.mr_zyl.project824.pro.attention.view.Attention;
import com.example.mr_zyl.project824.pro.base.view.MyFragmentTabHost;
import com.example.mr_zyl.project824.pro.base.view.residemenu.ResideDispatch;
import com.example.mr_zyl.project824.pro.base.view.residemenu.ResideMenu;
import com.example.mr_zyl.project824.pro.base.view.residemenu.ResideTouch;
import com.example.mr_zyl.project824.pro.essence.refreshEvent;
import com.example.mr_zyl.project824.pro.essence.view.activity.SimpleCameraActivity;
import com.example.mr_zyl.project824.pro.essence.view.essence;
import com.example.mr_zyl.project824.pro.mine.view.Mine;
import com.example.mr_zyl.project824.pro.mine.view.selfview.MySnackbarUtils;
import com.example.mr_zyl.project824.pro.newpost.view.Newpost;
import com.example.mr_zyl.project824.pro.publish.view.Publish;
import com.example.mr_zyl.project824.pro.publish.view.SimpleTakePhotoActivity;
import com.example.mr_zyl.project824.pro.publish.view.self.MoreWindow;
import com.example.mr_zyl.project824.utils.DisplayUtil;
import com.example.mr_zyl.project824.utils.StatusBarUtils;
import com.example.mr_zyl.project824.utils.SystemAppUtils;
import com.lqr.imagepicker.ImagePicker;
import com.lqr.imagepicker.bean.ImageItem;
import com.lqr.imagepicker.ui.ImageGridActivity;
import com.lqr.imagepicker.ui.ImagePreviewActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity implements TabHost.OnTabChangeListener, View.OnClickListener {

    private static final String TAG = "MainActivity";
    @BindView(R.id.ll_main_content)
    LinearLayout ll_main_content;

    @BindView(android.R.id.tabhost)
    MyFragmentTabHost fragmenttabhost;
    @BindView(android.R.id.tabs)
    TabWidget tabs;

    private List<Tabitem> tablists;
    private long lasttime;
    private MoreWindow mMoreWindow;
    ResideMenu resideMenu;
    /**
     * 精华的vp是否在边界
     */
    private ResideTouch essence_vp_resideTouch;

    protected int initLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initLayoutId());
        StatusBarUtils.setTransparent(this);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        //先初始化每个tab对象的数据
        initFragmentTabData();
        //再初始化TabHost控件
        initTabHost();

        View view = LayoutInflater.from(this).inflate(R.layout.main_left_layout, null);
        ListView lv_main_leftmenu = (ListView) view.findViewById(R.id.lv_main_leftmenu);
        List<String> datas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            datas.add("item:" + i);
        }
        lv_main_leftmenu.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, datas));

        view.setLayoutParams(new LinearLayout.LayoutParams((int) (DisplayUtil.Width(this) * 0.8f), LinearLayout.LayoutParams.MATCH_PARENT));

        resideMenu = new ResideMenu(this, view);
        resideMenu.setUse3D(false);
        resideMenu.setScaleValue(0.8f);
        resideMenu.attachToActivity(this, ll_main_content);
        resideMenu.addIgnoredView(tabs);
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
        resideMenu.setMenuListener(new ResideMenu.OnMenuListener() {
            @Override
            public void openMenu() {

            }

            @Override
            public void closeMenu() {

            }

            @Override
            public void transProgressRadio(float ratio) {
                Log.i(TAG, "transProgressRadio: "+ratio);
                EventBus.getDefault().post(new ResideDispatch(ratio));
            }
        });
    }
    @Override
    protected void onResume() {
        /**
         * 设置为横屏
         * android:configChanges="orientation|screenSize" 切屏不重走oncreate（）方法
         */
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        //自动调节输入法区域
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onResume();
    }

    /**
     * 侧滑菜单的回调功能
     *
     * @param touch
     */
    public void onEventMainThread(ResideTouch touch) {
        if (touch != null) {
            switch (touch.getHandleType()) {
                case ResideTouch.HandleTypeTagLeftBorder:
                    essence_vp_resideTouch = touch;
                    resideMenu.setIsLeftBorder(essence_vp_resideTouch.is_Left());
                    break;
                case ResideTouch.HandleTypeTagToggle:
                    resideMenu.toggleMenu();
                    break;
            }
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
        }
        lasttime = System.currentTimeMillis();
    }

    @Override
    public void onTabChanged(String tabId) {
        //关掉tab中新的fragment
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

        if (TextUtils.isEmpty(tabId)) {//选中了空fragment选项
//            startActivity(new Intent(this, PlayActivity.class));
        }
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        if (tabId.equals(getString(R.string.main_essence_text)) && essence_vp_resideTouch != null) {
            resideMenu.setIsLeftBorder(essence_vp_resideTouch.is_Left());
        } else {
            if (tabId.equals(getString(R.string.main_mine_text))){
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
            resideMenu.setIsLeftBorder(true);
        }
        //重置Tab样式
        for (int i = 0; i < tablists.size(); i++) {
            Tabitem tabItem = tablists.get(i);
            String titleString = tabItem.getTitleString();
            //只有选中精华时判断是不是在边界，
            if (tabId.equals(titleString)) {
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

                @Override
                public void selectCamera() {
                    startActivity(new Intent(MainActivity.this, SimpleCameraActivity.class));
                }
            });
            mMoreWindow.init();
        }
        mMoreWindow.showMoreWindow(fragmenttabhost, 0);
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
