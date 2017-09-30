package com.example.mr_zyl.project.pro.publish.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.mr_zyl.project.R;
import com.example.mr_zyl.project.pro.base.view.BaseActivity;
import com.lqr.imagepicker.bean.ImageItem;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import butterknife.BindView;

public class SimpleTakePhotoActivity extends BaseActivity {

    @BindView(R.id.iv_showphoto)
    ImageView iv_showphoto;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_simple_take_photo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        ArrayList<ImageItem> imagepaths = (ArrayList<ImageItem>) intent.getSerializableExtra("imagepaths");
        String path = imagepaths.get(0).path;
        ImageLoader.getInstance().displayImage("file://"+path,iv_showphoto);
    }

}
