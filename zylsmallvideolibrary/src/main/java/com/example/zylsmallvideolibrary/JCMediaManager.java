package com.example.zylsmallvideolibrary;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.TextUtils;

import java.io.IOException;

import de.greenrobot.event.EventBus;


/**
 * <p>统一管理MediaPlayer的地方,只有一个mediaPlayer实例，那么不会有多个视频同时播放，也节省资源。</p>
 */
public class JCMediaManager implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnErrorListener, MediaPlayer.OnVideoSizeChangedListener {

    public MediaPlayer mediaPlayer;
    private static JCMediaManager jcMediaManager;
    public String uuid = "";//这个是正在播放中的视频控件的uuid，
    private String prev_uuid = "";
    public int currentVideoWidth = 0;
    public int currentVideoHeight = 0;

    public static JCMediaManager intance() {
        if (jcMediaManager == null) {
            jcMediaManager = new JCMediaManager();
        }
        return jcMediaManager;
    }

    public JCMediaManager() {
        mediaPlayer = new MediaPlayer();
    }

    public void prepareToPlay(Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        try {
            mediaPlayer.release();
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(context, Uri.parse(url));//设置要播放的文件
            mediaPlayer.setOnPreparedListener(this);//准备完成的时的监听
            mediaPlayer.setOnCompletionListener(this);//播放完毕的时的监听
            mediaPlayer.setOnBufferingUpdateListener(this);//当播放网络的数据流的buffer的监听
            mediaPlayer.setOnSeekCompleteListener(this);//seek定位操作完成的监听
            mediaPlayer.setOnErrorListener(this);//异步操作出现错误时的监听
            mediaPlayer.setOnVideoSizeChangedListener(this);//视频的大小第一次被知道或者发生改变时
            mediaPlayer.prepareAsync();//异步设置播放器进入prepare状态
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 准备完成的时候发出通知
     * @param mp
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        EventBus.getDefault().post(new VideoEvents().setType(VideoEvents.VE_PREPARED));
    }


    /**
     * 当播放网络的数据流的buffer发生变化的时候发出通知
     * @param mp  mp是调用这个接口的MediaPlayer对象
     * @param percent percent是数据缓存的百分比
     */
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        VideoEvents videoEvents = new VideoEvents().setType(VideoEvents.VE_MEDIAPLAYER_UPDATE_BUFFER);
        videoEvents.obj = percent;
        EventBus.getDefault().post(videoEvents);
    }

    /**
     * 当seek定位操作完成后发送通知
     * @param mp
     */
    @Override
    public void onSeekComplete(MediaPlayer mp) {
        EventBus.getDefault().post(new VideoEvents().setType(VideoEvents.VE_MEDIAPLAYER_SEEKCOMPLETE));
    }

    /**
     * 当一个媒体是播放完毕的时候发出通知
     * @param mp
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        EventBus.getDefault().post(new VideoEvents().setType(VideoEvents.VE_MEDIAPLAYER_FINISH_COMPLETE));
    }

    /**
     * 当使用异步操作出现错误时发送的通知
     * @param mp
     * @param what  错误的类型
     * @param extra 是针对what错误的额外的代码
     * @return  返回true表示有错误发生
     */
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return true;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void backUpUuid() {
        this.prev_uuid = this.uuid;
    }

    public void revertUuid() {
        this.uuid = this.prev_uuid;
        this.prev_uuid = "";
    }

    /**
     * 当视频的大小第一次被知道或者发生改变时发出通知
     * @param mp
     * @param width 视频的宽
     * @param height 视频的高
     */
    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        currentVideoWidth = mp.getVideoWidth();
        currentVideoHeight = mp.getVideoHeight();
        EventBus.getDefault().post(new VideoEvents().setType(VideoEvents.VE_MEDIAPLAYER_RESIZE));
    }

    public void clearWidthAndHeight() {
        currentVideoWidth = 0;
        currentVideoHeight = 0;
    }
}
