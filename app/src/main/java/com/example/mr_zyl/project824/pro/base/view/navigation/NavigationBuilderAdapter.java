package com.example.mr_zyl.project824.pro.base.view.navigation;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mr_zyl.project824.R;
import com.example.mr_zyl.project824.utils.SystemAppUtils;

public abstract class NavigationBuilderAdapter implements NavigationBuilder {

    private Context context;
    private String title;
    private int backgroundIconRes;

    private int leftIconRes;
    private int rightIconRes;
    private int titleIconRes;
    private int TitleMeasureHeigth;
    private View contentView;
    private LinearLayout ll_toolbar_essence_contentlayou;
    private View.OnClickListener leftIconOnClickListener;
    private View.OnClickListener rightIconOnClickListener;

    public NavigationBuilderAdapter(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public NavigationBuilder setBackground(int backgroundIconRes) {
        this.backgroundIconRes = backgroundIconRes;
        return this;
    }

    public int getTitleMeasureHeigth() {
        return TitleMeasureHeigth;
    }

    public void setTitleMeasureHeigth(int titleMeasureHeigth) {
        TitleMeasureHeigth = titleMeasureHeigth;
    }

    @Override
    public NavigationBuilderAdapter setTitle(String title) {
        this.title = title;
        return this;
    }

    @Override
    public NavigationBuilderAdapter setTitle(int title) {
        this.title = getContext().getResources().getString(title);
        return this;
    }

    @Override
    public NavigationBuilderAdapter setTitleIcon(int iconRes) {
        this.titleIconRes = iconRes;
        return this;
    }

    @Override
    public NavigationBuilderAdapter setLeftIcon(int iconRes) {
        this.leftIconRes = iconRes;
        return this;
    }

    @Override
    public NavigationBuilderAdapter setRightIcon(int iconRes) {
        this.rightIconRes = iconRes;
        return this;
    }

    @Override
    public NavigationBuilderAdapter setLeftIconOnClickListener(View.OnClickListener onClickListener) {
        this.leftIconOnClickListener = onClickListener;
        return this;
    }

    @Override
    public NavigationBuilderAdapter setRightIconOnClickListener(View.OnClickListener onClickListener) {
        this.rightIconOnClickListener = onClickListener;
        return this;
    }

    @Override
    public void createAndBind(ViewGroup parent) {
        contentView = LayoutInflater.from(getContext()).inflate(getLayoutId(), null, false);
        ll_toolbar_essence_contentlayou = (LinearLayout) contentView.findViewById(R.id.ll_toolbar_essence_contentlayou);
        ViewGroup viewGroup = (ViewGroup) contentView.getParent();
        if (viewGroup != null) {
            viewGroup.removeView(contentView);
        }
        parent.addView(contentView, 0);
        setStatusBarView(contentView);
    }

    public void setImageViewStyle(int viewId, int imageRes, View.OnClickListener onClickListener) {
        ImageView imageView = (ImageView) getContentView().findViewById(viewId);
        if (imageRes == 0) {
            imageView.setVisibility(View.GONE);
        } else {
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageResource(imageRes);
            imageView.setOnClickListener(onClickListener);
        }
    }

    public void setTextViewStyle(int viewId) {
        TextView textview = (TextView) getContentView().findViewById(viewId);
        if (TextUtils.isEmpty(title)) {
            textview.setText("");
            textview.setVisibility(View.GONE);
        } else {
            textview.setText(title);
            textview.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 处理沉浸式状态栏
     * @param content
     */
    public void setStatusBarView(View content) {
        if (Build.VERSION.SDK_INT >= 19) {
            content.measure(0,0);
            setTitleMeasureHeigth(content.getMeasuredHeight());
            content.getLayoutParams().height = content.getMeasuredHeight() + SystemAppUtils.getStatusHeight(context);//25dp是状态栏高度

            content.setPadding(0, SystemAppUtils.getStatusHeight(context), 0, 0);
        }
    }

    public View findViewById(int id) {
        return getContentView().findViewById(id);
    }

    public abstract int getLayoutId();

    public View getContentView() {
        return contentView;
    }

    public LinearLayout getLl_toolbar_essence_contentlayou() {
        return ll_toolbar_essence_contentlayou;
    }

    public int getBackgroundIconRes() {
        return backgroundIconRes;
    }

    public int getLeftIconRes() {
        return leftIconRes;
    }

    public int getTitleIconRes() {
        return titleIconRes;
    }

    public int getRightIconRes() {
        return rightIconRes;
    }

    public View.OnClickListener getLeftIconOnClickListener() {
        return leftIconOnClickListener;
    }

    public View.OnClickListener getRightIconOnClickListener() {
        return rightIconOnClickListener;
    }

    public String getTitle() {
        return title;
    }
}
