package com.example.mr_zyl.project824.pro.essence.view.activity;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mr_zyl.project824.R;
import com.example.mr_zyl.project824.mvp.presenter.impl.MvpBasePresenter;
import com.example.mr_zyl.project824.pro.base.view.BaseActivity;
import com.example.mr_zyl.project824.pro.essence.view.selfview.RingView;
import com.example.mr_zyl.project824.utils.AnimationBatchExecutor;
import com.example.mr_zyl.project824.utils.AnimationUtils;
import com.example.mr_zyl.project824.utils.SaveAssetImageAsyncTask;
import com.example.mr_zyl.project824.utils.SaveContentImageAsyncTask;
import com.example.mr_zyl.project824.utils.SaveImageAsyncTask;
import com.example.mr_zyl.project824.utils.SaveResImageAsyncTask;

import me.xiaopan.sketch.Sketch;
import me.xiaopan.sketch.SketchImageView;
import me.xiaopan.sketch.cache.DiskCache;
import me.xiaopan.sketch.display.DefaultImageDisplayer;
import me.xiaopan.sketch.feature.zoom.ImageZoomer;
import me.xiaopan.sketch.request.DownloadProgressListener;
import me.xiaopan.sketch.request.UriScheme;

public class BrowerActivity extends BaseActivity {

    private SketchImageView siv_browerpic;
    private RingView rv_loadprogress;
    private LinearLayout layout_detail_toolbar;
    private TextView button_detail_share, button_detail_applyWallpaper, button_detail_play, button_detail_save;
    private AnimationBatchExecutor animationBatchExecutor;
    private boolean show;
    private String picurl;
    private Context context;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_brower;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        hideBottomMenu();

        Bundle bundles = getIntent().getBundleExtra("pic_bundle");
        picurl = bundles.getString("picurl");
        boolean is_largepic = bundles.getBoolean("is_largepic");

        siv_browerpic = (SketchImageView) findViewById(R.id.siv_browerpic);
        rv_loadprogress = (RingView) findViewById(R.id.rv_BrowerActivity_loadprogress);
        rv_loadprogress.setVisibility(View.INVISIBLE);
        layout_detail_toolbar = (LinearLayout) findViewById(R.id.layout_detail_toolbar);
        button_detail_share = (TextView) findViewById(R.id.button_detail_share);
        button_detail_applyWallpaper = (TextView) findViewById(R.id.button_detail_applyWallpaper);
        button_detail_play = (TextView) findViewById(R.id.button_detail_play);
        button_detail_save = (TextView) findViewById(R.id.button_detail_save);
        button_detail_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (picurl == null || "".equals(picurl.trim())) {
                    Toast.makeText(context, "保存图片失败，因为当前图片的URL是空的，没法拿到图片", Toast.LENGTH_LONG).show();
                } else {
                    UriScheme uriScheme = UriScheme.valueOfUri(picurl);
                    if (uriScheme == UriScheme.NET) {
                        DiskCache.Entry imageFile3DiskCacheEntry = Sketch.with(context).getConfiguration().getDiskCache().get(picurl);
                        if (imageFile3DiskCacheEntry != null) {
                            new SaveImageAsyncTask(context, imageFile3DiskCacheEntry.getFile()).execute("");
                        } else {
                            Toast.makeText(context, "图片还没有下载好哦，再等一会儿吧！", Toast.LENGTH_LONG).show();
                        }
                    } else if (uriScheme == UriScheme.ASSET) {
                        new SaveAssetImageAsyncTask(context, UriScheme.ASSET.crop(picurl)).execute("");
                    } else if (uriScheme == UriScheme.CONTENT) {
                        new SaveContentImageAsyncTask(context, Uri.parse(picurl)).execute("");
                    } else if (uriScheme == UriScheme.DRAWABLE) {
                        new SaveResImageAsyncTask(context, Integer.valueOf(UriScheme.DRAWABLE.crop(picurl))).execute("");
                    } else if (uriScheme == UriScheme.FILE) {
                        Toast.makeText(context, "当前图片本就是本地的无需保存", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "我去，怎么会有这样的URL " + picurl, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        animationBatchExecutor = new AnimationBatchExecutor(this,
                R.anim.action_show, R.anim.action_hidden, 70,
                button_detail_share, button_detail_applyWallpaper, button_detail_play, button_detail_save);
        siv_browerpic.getOptions()
                .setImageDisplayer(new DefaultImageDisplayer())
                .setDecodeGifImage(true)//支持gif播放
                .setCacheProcessedImageInDisk(true)// 为了加快速度，将经过ImageProcessor、resize或thumbnailMode处理过或者读取时inSampleSize大于等于8的图片保存到磁盘缓存中，下次就直接读取
                .setCacheInMemoryDisabled(true);// 禁用内存缓存
        siv_browerpic.setSupportZoom(is_largepic);//大图才支持缩放
        siv_browerpic.setSupportLargeImage(true);//支持大图
//        siv_browerpic.getImageZoomer().zoom(zoomvalue);//放大n倍 （2：需要自定义）
        siv_browerpic.getImageZoomer().setReadMode(true);//开启阅读模式
        siv_browerpic.getImageZoomer().setZoomable(true);//是否禁用手势缩放
        siv_browerpic.setDownloadProgressColor(Color.GREEN);
        siv_browerpic.setShowDownloadProgress(true);

        siv_browerpic.setDownloadProgressListener(new DownloadProgressListener() {
            @Override
            public void onUpdateDownloadProgress(int totalLength, int completedLength) {
                rv_loadprogress.setVisibility(View.VISIBLE);
                float angle = (Float.valueOf(completedLength) / Float.valueOf(totalLength)) * 360;
                rv_loadprogress.setAngle(angle);
            }
        });
        siv_browerpic.displayImage(picurl);

        if (siv_browerpic.isSupportZoom()){
            siv_browerpic.getImageZoomer().setOnViewLongPressListener(new ImageZoomer.OnViewLongPressListener() {
                @Override
                public void onViewLongPress(View view, float x, float y) {
                    toggleToolbarVisibleState();
                }
            });
        }else {
            siv_browerpic.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    toggleToolbarVisibleState();
                    return false;
                }
            });
        }
    }

    /**
     * 隐藏底部虚拟按键，且全屏
     */
    private void hideBottomMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    /**
     * 切换工具栏和页码的显示状态
     */
    private void toggleToolbarVisibleState() {
        show = !show;
        animationBatchExecutor.start(show);
        if (show) {
            AnimationUtils.visibleViewByAlpha(layout_detail_toolbar);
        } else {
            AnimationUtils.goneViewByAlpha(layout_detail_toolbar);
        }
    }

    @Override
    public MvpBasePresenter bindPresenter() {
        return super.bindPresenter();
    }
}
