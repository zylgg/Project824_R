package com.example.mr_zyl.project824.pro.publish.view;

import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.mr_zyl.project824.R;
import com.example.mr_zyl.project824.pro.base.view.BaseActivity;


/**
 * Created by Mr_Zyl on 2016/8/25.
 */
public class PlayActivity extends BaseActivity {

    @Override
    protected int initLayoutId() {
        return R.layout.activity_test_video_play;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MediaController controller = new MediaController(this);
        VideoView vv_testmediaplay = (VideoView) findViewById(R.id.vv_testmediaplay);

        String videopath = "http://svideo.spriteapp.com/video/2015/0611/557992d8a522e_wpd.mp4";
        vv_testmediaplay.setVideoPath(videopath);
        vv_testmediaplay.setMediaController(controller);

        controller.setMediaPlayer(vv_testmediaplay);


        vv_testmediaplay.requestFocus();
    }


}
