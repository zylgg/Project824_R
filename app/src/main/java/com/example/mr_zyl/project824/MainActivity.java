package com.example.mr_zyl.project824;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.graphics.ColorUtils;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.example.mr_zyl.project824.bean.TabItem;
import com.example.mr_zyl.project824.bean.UserBean;
import com.example.mr_zyl.project824.pro.attention.view.Attention;
import com.example.mr_zyl.project824.pro.base.view.BaseActivity;
import com.example.mr_zyl.project824.pro.base.view.MyFragmentTabHost;
import com.example.mr_zyl.project824.pro.base.view.residemenu.ResideMenu;
import com.example.mr_zyl.project824.pro.base.view.residemenu.EventEntity.ResideTouch;
import com.example.mr_zyl.project824.pro.base.view.residemenu.TouchDisableView;
import com.example.mr_zyl.project824.pro.essence.view.activity.SimpleCameraActivity;
import com.example.mr_zyl.project824.pro.essence.view.essence;
import com.example.mr_zyl.project824.pro.mine.view.Mine;
import com.example.mr_zyl.project824.pro.mine.view.selfview.MySnackBarUtils;
import com.example.mr_zyl.project824.pro.newpost.view.Newpost;
import com.example.mr_zyl.project824.pro.publish.view.Publish;
import com.example.mr_zyl.project824.pro.publish.view.SimpleTakePhotoActivity;
import com.example.mr_zyl.project824.pro.publish.view.self.MoreWindow;
import com.example.mr_zyl.project824.utils.DateUtils;
import com.example.mr_zyl.project824.utils.DisplayUtil;
import com.example.mr_zyl.project824.utils.StatusBarUtils;
import com.example.mr_zyl.project824.view.AutoRotateDayView;
import com.example.mr_zyl.project824.view.AutoRotateHoursView;
import com.example.mr_zyl.project824.view.AutoRotateMinuteView;
import com.example.mr_zyl.project824.view.AutoRotateMonthView;
import com.example.mr_zyl.project824.view.AutoRotateSecondView;
import com.example.mr_zyl.project824.view.AutoRotateWeekView;
import com.lqr.imagepicker.ImagePicker;
import com.lqr.imagepicker.bean.ImageItem;
import com.lqr.imagepicker.ui.ImageGridActivity;
import com.lqr.imagepicker.ui.ImagePreviewActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import io.objectbox.Box;

import static com.example.mr_zyl.project824.BaseApplication.myObjectBox;


public class MainActivity extends BaseActivity implements TabHost.OnTabChangeListener, View.OnClickListener {

    private static final String TAG = "MainActivity";
    @BindView(R.id.ll_main_content)
    LinearLayout ll_main_content;

    @BindView(android.R.id.tabhost)
    MyFragmentTabHost fragmenttabhost;
    @BindView(android.R.id.tabs)
    TabWidget tabs;

    private List<TabItem> tablists;
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
        if (Build.VERSION.SDK_INT >= 21) {
            // 开启硬件加速
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        }
//        setContentView(initLayoutId());
        StatusBarUtils.setTransparent(this);
        EventBus.getDefault().register(this);
//        ButterKnife.bind(this);
        //先初始化每个tab对象的数据
        initFragmentTabData();
        //再初始化TabHost控件
        initTabHost();

        //初始化侧边布局
        View view = LayoutInflater.from(this).inflate(R.layout.main_left_layout, null);
        initLeftMenu(view);

        resideMenu = new ResideMenu(this, view);
        resideMenu.setScaleValue(0.8f);
        resideMenu.attachToActivity(this, ll_main_content);
        resideMenu.addIgnoredView(tabs);
        resideMenu.setMenuListener(new ResideMenu.SimpleOnMenuListener() {
            @Override
            public void transProgressRadio(float ratio) {

                //修改主页表面背景蒙层
                int color1 = getResources().getColor(R.color.transparent);
                int color2 = getResources().getColor(R.color.transparent55);
                ImageView iv_main_background = ll_main_content.findViewById(R.id.iv_main_background);
                iv_main_background.setBackgroundColor(ColorUtils.blendARGB(color1, color2, ratio));
            }

            @Override
            public void closeMenu() {
                super.closeMenu();
                if (essence_vp_resideTouch!=null&&currenttabtag.equals(getString(R.string.main_essence_text))){
                    resideMenu.setIsLeftBorder(essence_vp_resideTouch.is_Left());
                }
            }
        });
        if (savedInstanceState!=null){
            Log.i(TAG, "onCreate: "+savedInstanceState.getString(ON_SAVE_KEY));
        }else{
            Log.i(TAG, "onCreate: "+savedInstanceState);
        }

    }

    private static final String ON_SAVE_KEY="OnSaveKey";
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(ON_SAVE_KEY,"onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState!=null){
            Log.i(TAG, "onRestore: "+savedInstanceState.getString(ON_SAVE_KEY));
        }else{
            Log.i(TAG, "onRestore: "+savedInstanceState);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (NotchInScreenUtils.hasNotchInScreen(this)){//如果有刘海屏
//            resideMenu.getLeftMenuView().setPadding(0,SystemAppUtils.getStatusHeight(this),0,0);
//        }
    }

    private AutoRotateSecondView timeView_second;
    private AutoRotateMinuteView timeView_minute;
    private AutoRotateHoursView timeView_hours;
    private AutoRotateWeekView timeView_week;
    private AutoRotateDayView timeView_day;
    private AutoRotateMonthView timeView_month;
    private TextView timeView_year;
    private View v_dividing2;

    /**
     * 初始化侧边栏布局
     *
     * @param view
     */
    private void initLeftMenu(View view) {
        view.setLayoutParams(new LayoutParams((int) (DisplayUtil.Width(this) * 0.8f), LayoutParams.MATCH_PARENT));
        TextView tv_main_loginOut = view.findViewById( R.id.tv_main_loginOut);
        tv_main_loginOut.setOnClickListener(this);
        ImageView iv_main_headImg = view.findViewById( R.id.iv_main_headImg);
        String url = "http://inews.gtimg.com/newsapp_match/0/3348583155/0";
        ImageLoader.getInstance().displayImage(url, iv_main_headImg);

        timeView_second = view.findViewById( R.id.timeView_second);
        timeView_minute = view.findViewById( R.id.timeView_minute);
        timeView_hours = view.findViewById( R.id.timeView_hours);
        timeView_week = view.findViewById( R.id.timeView_week);
        timeView_day = view.findViewById( R.id.timeView_day);
        timeView_month = view.findViewById(R.id.timeView_month);
        timeView_year = view.findViewById( R.id.timeView_year);
        v_dividing2 = view.findViewById( R.id.v_dividing2);
        initListener();
        setData();
    }

    private void initListener() {
        //设置因月份的改变，改变年和天的监听
        timeView_month.setChangeTimeListener(new AutoRotateMonthView.OnChangeTimeListener() {
            @Override
            public void changeYear(final int year) {
                timeView_year.post(new Runnable() {
                    @Override
                    public void run() {
                        timeView_year.setText("" + year);
                    }
                });
            }

            @Override
            public void changeDay(final int count) {
                timeView_day.post(new Runnable() {
                    @Override
                    public void run() {
                        timeView_day.setTime(1, count);
                    }
                });
            }
        });
        //秒钟绘制完后，设置水平渐变条的宽度
        final ViewTreeObserver viewTreeObserver = timeView_second.getViewTreeObserver();
        viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (viewTreeObserver.isAlive()) {
                    viewTreeObserver.removeOnPreDrawListener(this);
                }
                ViewGroup.LayoutParams layoutParams = v_dividing2.getLayoutParams();
                layoutParams.width = timeView_second.getViewMaxWidth();
                v_dividing2.setLayoutParams(layoutParams);
                return true;
            }
        });
    }

    private void setData() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);

//        Log.i("Calendar_years", "-" + year);
//        Log.i("Calendar_month", "-" + month);
        Log.i("Calendar_week", "-" + week);
//        Log.i("Calendar_day", "-" + day);
//        Log.i("Calendar_hours", "-" + hours);
//        Log.i("Calendar_minutes", "-" + minutes);
//        Log.i("Calendar_seconds", "-" + seconds);

        timeView_second.setTime(seconds).start();
        timeView_minute.setTime(minutes).start();
        timeView_hours.setTime(hours).start();
        timeView_week.setTime(week).start();
        timeView_day.setTime(day, DateUtils.getDays(year, month)).start();
        timeView_month.setTime(month, DateUtils.getDays(year, month)).start();
    }

    /**
     * 侧滑菜单的回调功能
     *
     * @param touch
     */
    public void onEventMainThread(ResideTouch touch) {
        if (touch != null) {
            switch (touch.getHandleType()) {
                case ResideTouch.HandleTypeTagLeftBorder://处理是否可以侧滑
                    essence_vp_resideTouch = touch;
                    resideMenu.setIsLeftBorder(essence_vp_resideTouch.is_Left());
                    break;
                case ResideTouch.HandleTypeTagToggle://点按钮打开左边菜单
                    resideMenu.toggleMenu();
                    break;
            }
        }
    }

    private void initFragmentTabData() {
        this.tablists = new ArrayList<>();
        //添加精华Tab
        tablists.add(new TabItem(R.drawable.main_bottom_essence_normal, R.drawable.main_bottom_essence_press,
                R.string.main_essence_text, essence.class, this));
        //添加新帖Tab
        tablists.add(new TabItem(R.drawable.main_bottom_newpost_normal, R.drawable.main_bottom_newpost_press,
                R.string.main_newpost_text, Newpost.class, this));
        //添加发布Tab
        tablists.add(new TabItem(R.drawable.main_bottom_public_normal, R.drawable.main_bottom_public_press,
                0, Publish.class, this));
        //添加关注Tab
        tablists.add(new TabItem(R.drawable.main_bottom_attention_normal, R.drawable.main_bottom_attention_press,
                R.string.main_attention_text, Attention.class, this));
        //添加我的Tab
        tablists.add(new TabItem(R.drawable.main_bottom_mine_normal, R.drawable.main_bottom_mine_press,
                R.string.main_mine_text, Mine.class, this));
    }

    /**
     * 当前选中的tab标题
     */
    private String currenttabtag;

    private void initTabHost() {
        fragmenttabhost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        fragmenttabhost.getTabWidget().setDividerDrawable(null);
        for (int i = 0; i < tablists.size(); i++) {
            TabItem tabItem = tablists.get(i);
            TabHost.TabSpec tabSpec = fragmenttabhost
                    .newTabSpec(tabItem.getTitleString())//添加标题，
                    .setIndicator(tabItem.getView());//添加相应的view布局
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
            MySnackBarUtils.getBuilder(this)
                    .setMessage("再点击一次退出" + getString(R.string.app_name))
                    .show();
        }
        lasttime = System.currentTimeMillis();
    }

    @Override
    public void onTabChanged(String tabText) {
        //关掉tab中新的fragment
        FragmentManager Frmanager = getSupportFragmentManager();
        FragmentTransaction ft = Frmanager.beginTransaction();

        Fragment newfragment = Frmanager.findFragmentByTag("newfragment");
        Fragment selectedfragment = Frmanager.findFragmentByTag(tabText);
        if (newfragment != null && !newfragment.isDetached()) {
            ft.detach(newfragment);
        }
        if (selectedfragment != null) {
            if (selectedfragment.isHidden()) {
                ft.show(selectedfragment);
            }
        }
        ft.commit();


        if (TextUtils.isEmpty(tabText)) {//选中了空fragment选项
//            startActivity(new Intent(this, PlayActivity.class));
        }
        currenttabtag=tabText;
        //如果设置了透明状态栏，状态栏图标文字颜色默认设为白色
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        TouchDisableView touchDisableView = (TouchDisableView) resideMenu.getChildAt(1);
        //初始化，让子布局不自己拦截事件
        touchDisableView.setTouchDisable(TouchDisableView.touchStatusNoIntercept);

        //“精华”时判断是不是在边界，
        if (tabText.equals(getString(R.string.main_essence_text)) && essence_vp_resideTouch != null) {
            resideMenu.setIsLeftBorder(essence_vp_resideTouch.is_Left());
        } else {
            //在“我”时，设置状态栏图标文字颜色为黑色
            if (tabText.equals(getString(R.string.main_mine_text))) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
            //在“关注”时触摸事件交给父布局处理
            else if (tabText.equals(getString(R.string.main_attention_text))) {
                touchDisableView.setTouchDisable(TouchDisableView.touchStatusBySuper);
            }
            //如果不是精华页一律设置达到左边界，便于可以（默认）打开侧滑
            resideMenu.setIsLeftBorder(true);
        }
        //重置Tab样式
        for (int i = 0; i < tablists.size(); i++) {
            TabItem tabItem = tablists.get(i);
            String titleString = tabItem.getTitleString();
            tabItem.setChecked(tabText.equals(titleString));
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == 0) {//“+”菜单
            showMoreWindow();
            return;
        } else if (v.getId() == R.id.tv_main_loginOut) {//退出登录
            //清除账号信息
            Box<UserBean> userBeanBox = myObjectBox.boxFor(UserBean.class);
            userBeanBox.removeAll();

            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
//        //如果再次点击当前底部tab菜单
//        if (currenttabtag.equals(v.getTag())) {
//            //可执行刷新数据等操作：
//            refreshEvent event = new refreshEvent();
//            event.setIs_RefreshCurrent(true);
//            EventBus.getDefault().post(event);
//        }
//        currenttabtag = (String) v.getTag();
    }

    /**
     * 图片选择器请求码
     */
    public static final int IMAGE_PICKER = 100;

    /**
     * 显示更多窗口
     */
    private void showMoreWindow() {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
