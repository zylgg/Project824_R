package com.example.mr_zyl.project;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.mr_zyl.project.pro.attention.view.Attention;
import com.example.mr_zyl.project.pro.base.view.MyFragmentTabHost;
import com.example.mr_zyl.project.pro.essence.view.essence;
import com.example.mr_zyl.project.pro.mine.view.Mine;
import com.example.mr_zyl.project.pro.newpost.view.Newpost;
import com.example.mr_zyl.project.pro.publish.view.Publish;
import com.example.mr_zyl.project.pro.publish.view.self.MoreWindow;
import com.example.mr_zyl.project.utils.SystemAppUtils;
import com.example.mr_zyl.project.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TabHost.OnTabChangeListener, View.OnClickListener {

    private List<Tabitem> tablists;
    private MyFragmentTabHost fragmenttabhost;
    private View tv_bottomnavigation_view;
    private long lasttime;
    private MoreWindow mMoreWindow;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_bottomnavigation_view =findViewById(R.id.tv_bottomnavigation_view);
        //设置不填充到虚拟按键之下
        setNoFitBottomStatus();

        //先初始化每个tab对象的数据
        initFragmentTabData();
        //再初始化TabHost控件
        initTabHost();
        addBadge();
    }

    private int bottomStatusHeight;

    private void setNoFitBottomStatus() {
        bottomStatusHeight = SystemAppUtils.getBottomStatusHeight(this);
        if (bottomStatusHeight > 0) {
            tv_bottomnavigation_view.getLayoutParams().height = bottomStatusHeight;
        }
    }

    private void initFragmentTabData() {
        this.tablists = new ArrayList<Tabitem>();
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

    private void initTabHost() {
        fragmenttabhost = (MyFragmentTabHost) findViewById(android.R.id.tabhost);
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
            }
            if (tabItem.getTitleid()==0){//只对非fragment跳转的tab 设置自定义监听
                fragmenttabhost.getTabWidget().getChildTabViewAt(i).setOnClickListener(this);
            }
        }
    }

    private void addBadge() {
        for (int i = 0; i < tablists.size(); i++) {
            Tabitem ti = tablists.get(i);
            String titles = ti.getTitleString();
            if (titles.equals("我")) {
                View view = ti.getview();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - lasttime < 2000) {
            ((BaseApp) this.getApplication()).exit(0);
        } else {
            ToastUtil.showToast(this, "再点击一次退出" + getString(R.string.app_name) + "！");
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
        switch (v.getId()) {
            case 0:
                showMoreWindow(v);
                break;
        }
    }

    /**
     * 显示更多窗口
     *
     * @param view
     */
    private void showMoreWindow(View view) {
        if (null == mMoreWindow) {
            mMoreWindow = new MoreWindow(this);
            mMoreWindow.init();
        }
        mMoreWindow.showMoreWindow(view, bottomStatusHeight);
    }

    class Tabitem {
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
}
