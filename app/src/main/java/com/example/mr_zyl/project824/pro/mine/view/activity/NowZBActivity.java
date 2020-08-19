package com.example.mr_zyl.project824.pro.mine.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mr_zyl.project824.R;
import com.tencent.intervideo.nowproxy.NowLive;

public class NowZBActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_z_b);
        Button button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NowLive.openRoom(1296933301);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        NowLive.stop();
    }
}
