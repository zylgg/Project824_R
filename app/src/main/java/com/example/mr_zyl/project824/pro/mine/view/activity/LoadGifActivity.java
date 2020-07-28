package com.example.mr_zyl.project824.pro.mine.view.activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.mr_zyl.project824.R;
import com.example.mr_zyl.project824.pro.base.view.BaseActivity;
import com.example.mr_zyl.project824.utils.ImageUtils;

import java.io.IOException;
import java.io.InputStream;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class LoadGifActivity extends BaseActivity {

    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private GifImageView giv_loadgifview;
    private Activity context;
    private NotificationManager manger;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_load_gif;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manger = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        context = this;
        TextView tv_loadpic =findViewById(R.id.tv_loadpic);
        giv_loadgifview =  findViewById(R.id.giv_loadgifview);
        try {
            InputStream gifis = getResources().getAssets().open("talk.gif");
            giv_loadgifview.setImageDrawable(new GifDrawable(gifis));
        } catch (IOException e) {
            e.printStackTrace();
        }
        tv_loadpic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                gallery();
            }
        });

    }

    /*
     * 从相册获取
     */
    public void gallery() {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PHOTO_REQUEST_GALLERY:
                // 从相册返回的数据
                if (data != null) {
                    // 得到图片的全路径
                    Uri uri = data.getData();
                    String image_path = ImageUtils.getImageAbsolutePath(context, uri);
                    try {
                        giv_loadgifview.setImageDrawable(new GifDrawable(image_path));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;

        }
    }
}
