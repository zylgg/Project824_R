package com.example.mr_zyl.project824.pro.newpost.view;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mr_zyl.project824.R;
import com.example.mr_zyl.project824.bean.BuilderItemEntity;
import com.example.mr_zyl.project824.pro.base.view.BaseFragment;
import com.example.mr_zyl.project824.pro.base.view.item.DefaultImpleItemBuilder;
import com.example.mr_zyl.project824.pro.newpost.natvigation.NewpostNavigationBuilder;
import com.example.mr_zyl.project824.pro.newpost.view.Activity.FontsActivity;
import com.example.mr_zyl.project824.pro.newpost.view.Activity.TestMeasureActivity;
import com.example.mr_zyl.project824.pro.newpost.view.Activity.TreeViewActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mr_Zyl on 2016/8/25.
 */
public class Newpost extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.ll_newpost_tabcontainer)
    LinearLayout ll_newpost_tabcontainer;

    @BindView(R.id.tv_toyjjy)
    TextView tv_toyjjy;
    private List<BuilderItemEntity> itemlists = new ArrayList<>();
    private DefaultImpleItemBuilder builder;
    Intent intent;

    @Override
    public int getContentView() {
        return R.layout.newpost;
    }

    @Override
    public void initContentView(View viewContent) {
        ButterKnife.bind(this,viewContent);
        initToolBar(ll_newpost_tabcontainer);

        itemlists.add(new BuilderItemEntity(R.drawable.login_unlogin_header, "滑动页面", TestMeasureActivity.class, -1));
        itemlists.add(new BuilderItemEntity(R.drawable.login_unlogin_header, "拓扑图", TreeViewActivity.class, -1));
        itemlists.add(new BuilderItemEntity(R.drawable.login_unlogin_header, "全局修改字体", FontsActivity.class, -1));
        initItemLayout(viewContent);
        tv_toyjjy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("yj://main.bjsy"));
                startActivity(intent);
            }
        });
    }

    private void initToolBar(View viewContent) {
        NewpostNavigationBuilder builder = new NewpostNavigationBuilder(Fcontext);
        builder.setTitle("新项")
                .setBackground(R.drawable.toolbar_background_newpost_shape)
                .createAndBind((ViewGroup) viewContent);
    }

    private void initItemLayout(View viewContent) {
        for (int i = 0; i < itemlists.size(); i++) {
            BuilderItemEntity entity = itemlists.get(i);
            builder = new DefaultImpleItemBuilder(getActivity());
            builder.setLeftIcons(entity.left_resid);
            builder.setTitleText(entity.text);
            builder.setOnClickListener(this).BindParentView((ViewGroup) viewContent);
        }
    }

    @Override
    public void onClick(View v) {
        requestItemClick(v);
    }

    private void requestItemClick(View v) {
        if (v.getTag() == null) {
            return;
        }
        if (intent == null) {
            intent = new Intent();
        }
        BuilderItemEntity entity = null;
        String text = v.getTag().toString();
        //根据text,获取BuilderItemEntity
        for (int i = 0; i < itemlists.size(); i++) {
            entity = itemlists.get(i);
            if (text.equals(entity.text)) {
                break;
            }
        }
        //处理跳转逻辑
        if (entity.classname != null) {
            intent.setClass(getActivity(), entity.classname);
            if (entity.requestcode == -1) {
                startActivity(intent);
            } else {
                startActivityForResult(intent, entity.requestcode);
            }
        }
    }
}
