package com.example.mr_zyl.project.pro.mine.view.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mr_zyl.project.R;
import com.example.mr_zyl.project.pro.base.view.BaseActivity;

import net.robinx.lib.blur.StackBlur;
import net.robinx.lib.blur.utils.BlurUtils;
import net.robinx.lib.blur.widget.BlurDrawable;
import net.robinx.lib.blur.widget.BlurMaskRelativeLayout;
import net.robinx.lib.blur.widget.BlurMode;
import net.robinx.lib.blur.widget.BlurRelativeLayout;

//import net.qiujuer.genius.blur.StackBlur;


public class FastBlurActivity extends BaseActivity {

    private static final int SCALE_FACTOR = 4;
    private boolean mCompress;
    private TextView mTime, tv_blurdrawable;
    private Bitmap mBitmap, mCompressBitmap, img_bgbitmap,mCompressbgbitmap;
    private ImageView iv_blue_1, mImageJava, mImageJniPixels, mImageJniBitmap, iv_blue_5, iv_bgiv;
    private BlurMaskRelativeLayout bmrl_blurmask_rl;
    private BlurRelativeLayout brl_blur_rl;
    private Toolbar tb_fast_blur_title;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_fast_blur;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tb_fast_blur_title= (Toolbar) findViewById(R.id.tb_fast_blur_title);
        setSupportActionBar(tb_fast_blur_title);

        setTitle("各种高斯模糊");
        //快速模糊jar，提供的布局
        bmrl_blurmask_rl = (BlurMaskRelativeLayout) findViewById(R.id.bmrl_blurmask_rl);
        bmrl_blurmask_rl.blurMode(BlurMode.RENDER_SCRIPT).blurRadius(10);
        brl_blur_rl = (BlurRelativeLayout) findViewById(R.id.brl_blur_rl);
        brl_blur_rl.blurMode(BlurMode.RENDER_SCRIPT).blurRadius(10);


        iv_blue_1 = (ImageView) findViewById(R.id.iv_blue_1);
        mImageJava = (ImageView) findViewById(R.id.iv_blue_2);
        mImageJniPixels = (ImageView) findViewById(R.id.iv_blue_3);
        mImageJniBitmap = (ImageView) findViewById(R.id.iv_blue_4);
        iv_blue_5 = (ImageView) findViewById(R.id.iv_blue_5);
        iv_bgiv = (ImageView) findViewById(R.id.iv_bgiv);
        tv_blurdrawable = (TextView) findViewById(R.id.tv_blurdrawable);
        initBlur();

    }

    @Override
    protected void onResume() {
        super.onResume();
        applyBlur();
    }

    private void initBlur() {
        BlurDrawable blurDrawable = new BlurDrawable(FastBlurActivity.this);
        //设置当前视图和模糊试图之间的偏移量
        blurDrawable.setDrawOffset(tv_blurdrawable.getLeft(), tv_blurdrawable.getTop() + BlurUtils.getStatusBarHeight(FastBlurActivity.this));
        blurDrawable.setCornerRadius(50);
        blurDrawable.setBlurRadius(10);
        blurDrawable.setOverlayColor(Color.parseColor("#64ffffff"));
        tv_blurdrawable.setBackgroundDrawable(blurDrawable);
        // 找到图片
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.img_bj);
        img_bgbitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mainfragmentbg);
        mTime = (TextView) findViewById(R.id.text_blur_time);

        // Init src image
        iv_blue_1.setImageBitmap(mBitmap);

        // 压缩并保存位图
        Matrix matrix = new Matrix();
        matrix.postScale(1.0f / SCALE_FACTOR, 1.0f / SCALE_FACTOR);
        // 新的压缩位图
        mCompressBitmap = Bitmap.createBitmap(mBitmap, 0, 0,
                mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
        mCompressbgbitmap = Bitmap.createBitmap(img_bgbitmap, 0, 0,
                img_bgbitmap.getWidth(), img_bgbitmap.getHeight(), matrix, true);

        // 设置变化监听
        CheckBox checkBox = (CheckBox) findViewById(R.id.checkbox_blur_isCompress);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCompress = isChecked;
                applyBlur();
            }
        });
    }

    private void clearDrawable() {
        mImageJava.setImageBitmap(null);
        mImageJniPixels.setImageBitmap(null);
        mImageJniBitmap.setImageBitmap(null);
    }

    private void applyBlur() {
        // First clear
        clearDrawable();

        // Run Thread
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("模糊耗时(ms):  ");
                    for (int i = 1; i <= 5; i++) {
                        sb.append(blur(i)).append("ms   ");
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTime.setText(sb.toString());
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private long blur(int i) {
        ImageView view = null;
        if (i == 1)
            view = mImageJava;
        else if (i == 2)
            view = mImageJniPixels;
        else if (i == 3)
            view = mImageJniBitmap;
        else if (i == 4)
            view = iv_blue_5;

        long startMs = System.currentTimeMillis();

        // Is Compress
        float radius = 20;
        Bitmap overlay = mBitmap;
        if (mCompress) {
            radius = 4;
            overlay = mCompressBitmap;
            img_bgbitmap=mCompressbgbitmap;
        }

        // Java
        if (i == 1)
//            overlay = StackBlur.blur(overlay, (int) radius, false);
//            overlay = FastBlurUtility.startBlurBackground(overlay);
            overlay = StackBlur.blurJava(overlay, (int) radius, false);
            // Bitmap JNI Native
        else if (i == 2)
            overlay = StackBlur.blurNatively(overlay, (int) radius, false);
//            overlay = StackBlur.blurNatively(overlay, (int) radius, false);
            // Pixels JNI Native
        else if (i == 3)
            overlay = StackBlur.blurNativelyPixels(overlay, (int) radius, false);
//            overlay = StackBlur.blurNativelyPixels(overlay, (int) radius, false);

        else if (i == 4) {
            overlay = StackBlur.blurRenderScript(this, overlay, (int) radius, false);
        }else if (i==5){
            img_bgbitmap = StackBlur.blurRenderScript(this, img_bgbitmap, (int) radius, false);
        }

//            StackBlur.blurRenderScript(this, overlay, (int) radius, false);


        // Show
        showDrawable(view, overlay, i);

        return System.currentTimeMillis() - startMs;
    }


    private void showDrawable(final ImageView view, final Bitmap overlays, final int i) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (i == 5) {
                    iv_bgiv.setImageBitmap(img_bgbitmap);
                }else {
                    view.setImageBitmap(overlays);
                }
            }
        });
    }

}
