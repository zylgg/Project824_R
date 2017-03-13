package com.example.mr_zyl.project.pro.mine.view.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.example.mr_zyl.project.R;
import com.example.mr_zyl.project.pro.mine.giflib.GifLoader;
import com.example.mr_zyl.project.pro.mine.view.MediaService;
import com.example.mr_zyl.project.utils.ImageUtils;

import java.io.IOException;
import java.io.InputStream;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class LoadGifActivity extends AppCompatActivity {

    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private GifLoader gifLoader;
    private GifImageView giv_loadgifview;
    private Activity context;
    private NotificationManager manger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_gif);
        manger = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        context = this;
        gifLoader = GifLoader.getInstance(this);
        TextView tv_loadpic = (TextView) findViewById(R.id.tv_loadpic);
        giv_loadgifview = (GifImageView) findViewById(R.id.giv_loadgifview);
        try {
            InputStream gifis= getResources().getAssets().open("talk.gif");
            giv_loadgifview.setImageDrawable(new GifDrawable(gifis));
        } catch (IOException e) {
            e.printStackTrace();
        }
        tv_loadpic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                sendCustomerNotification(2);
                gallery();
            }
        });

    }

    private static  final int TYPE_Customer=1;
    private int CommandNext=2;
    private int CommandPlay=3;
    private int playerStatus=4;
    private int StatusStop=5;
    private int CommandClose=6;
    //command是自定义用来区分各种点击事件的
    private void sendCustomerNotification(int command){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("Notification");
        builder.setContentText("自定义通知栏示例");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        //builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.push));
        builder.setAutoCancel(true);
        builder.setOngoing(true);
        builder.setShowWhen(false);
        RemoteViews remoteViews = new RemoteViews(getPackageName(),R.layout.notification_template_customer);
        remoteViews.setTextViewText(R.id.title,"Notification");
        remoteViews.setTextViewText(R.id.text,"song");
        if(command==CommandNext){
            remoteViews.setImageViewResource(R.id.btn1,R.drawable.main_bottom_essence_normal);
        }else if(command==CommandPlay){
            if(playerStatus==StatusStop){
                remoteViews.setImageViewResource(R.id.btn1,R.drawable.main_bottom_newpost_press);
            }else{
                remoteViews.setImageViewResource(R.id.btn1,R.drawable.main_bottom_public_normal);
            }
        }
        Intent Intent1 = new Intent(this,MediaService.class);
        Intent1.putExtra("command",CommandNext+"");
        //getService(Context context, int requestCode, @NonNull Intent intent, @Flags int flags)
        //不同控件的requestCode需要区分开 getActivity broadcoast同理
        PendingIntent PIntent1 =  PendingIntent.getService(this,5,Intent1,0);
        remoteViews.setOnClickPendingIntent(R.id.btn1,PIntent1);

//        Intent Intent2 = new Intent(this,MediaService.class);
//        Intent2.putExtra("command",CommandNext);
//        PendingIntent PIntent2 =  PendingIntent.getService(this,6,Intent2,0);
//        remoteViews.setOnClickPendingIntent(R.id.btn2,PIntent2);

//        Intent Intent3 = new Intent(this,MediaService.class);
//        Intent3.putExtra("command",CommandClose);
//        PendingIntent PIntent3 =  PendingIntent.getService(this,7,Intent3,0);
//        remoteViews.setOnClickPendingIntent(R.id.btn3,PIntent3);

        builder.setContent(remoteViews);
        Notification notification = builder.build();
        manger.notify(LoadGifActivity.TYPE_Customer,notification);
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
                    gifLoader.displayImage("http://wimg.spriteapp.cn/ugc/2017/02/27/58b398b00925c_1.jpg", giv_loadgifview, true);
                }
                break;

        }
    }
}
