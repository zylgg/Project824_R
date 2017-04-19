package com.example.zylsmallvideolibrary;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import de.greenrobot.event.EventBus;

/**
 * 自定义JC播放器 优化
 *
 * @see <a href="https://github.com/lipangit/jiecaovideoplayer">JiecaoVideoplayer Github</a>
 */
public class JCVideoPlayer extends FrameLayout implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, SurfaceHolder.Callback, View.OnTouchListener {

    //控件
    public ImageView iv_start;
    /**
     * 视频加载过程中的进度圈
     */
    private ProgressBar pb_loading;
    private ProgressBar pb_main_progressbar;
    private ImageView iv_fullscreen;
    private SeekBar sb_progress;
    private TextView tv_currentTime, tv_totalTime;
    private ResizeSurfaceView sv_surfaceView;
    private SurfaceHolder surfaceHolder;
    private TextView tv_title;
    private ImageView iv_back;
    private ImageView iv_mp4cover;
    private RelativeLayout rl_playmainview;
    private LinearLayout ll_title_container, ll_bottom_control;
    private ImageView iv_mp3cover;

    //属性
    private String url;
    private String url2;
    private String title;
    private boolean ifFullScreen = false;
    public String uuid;//区别相同地址,包括全屏和不全屏，和都不全屏时的相同地址
    public boolean ifShowTitle = false;
    private boolean ifMp3 = false;

    private int enlargRecId = 0;//放大的记录的id
    private int shrinkRecId = 0;//缩小的记录的id

    public static Skin globleSkin;
    private Skin skin;

    // 为了保证全屏和退出全屏之后的状态和之前一样,需要记录状态
    public int CURRENT_STATE = -1;//-1相当于null
    public static final int CURRENT_STATE_PREPAREING = 0;
    public static final int CURRENT_STATE_PAUSE = 1;
    public static final int CURRENT_STATE_PLAYING = 2;
    public static final int CURRENT_STATE_OVER = 3;//这个状态可能不需要，播放完毕就进入normal状态
    public static final int CURRENT_STATE_NORMAL = 4;//刚初始化之后
    private OnTouchListener mSeekbarOnTouchListener;
    private static Timer mDismissControlViewTimer;
    private static Timer mUpdateProgressTimer;
    private static long clickfullscreentime;//点击全屏按钮的时间
    private static final int FULL_SCREEN_NORMAL_DELAY = 5000;

    // 一些临时表示状态的变量
    private boolean touchingProgressBar = false;
    private static boolean isFromFullScreenBackHere = false;//如果是true表示这个正在不是全屏，并且全屏刚推出，总之进入过全屏
    public static boolean isClickFullscreen = false;
    private String TAG = "JCVideoPlayer";

    public JCVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        uuid = UUID.randomUUID().toString();
        init(context);
        initlistener();
    }

    private void init(Context context) {
        View.inflate(context, R.layout.video_control_view, this);
        iv_start = (ImageView) findViewById(R.id.iv_start);
        pb_loading = (ProgressBar) findViewById(R.id.pb_loading);
        pb_main_progressbar = (ProgressBar) findViewById(R.id.pb_main_progressbar);
        iv_fullscreen = (ImageView) findViewById(R.id.iv_fullscreen);
        sb_progress = (SeekBar) findViewById(R.id.sb_progress);
        tv_currentTime = (TextView) findViewById(R.id.tv_currentTime);
        tv_totalTime = (TextView) findViewById(R.id.tv_totalTime);
        sv_surfaceView = (ResizeSurfaceView) findViewById(R.id.sv_surfaceView);
        ll_bottom_control = (LinearLayout) findViewById(R.id.ll_bottom_control);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_mp4cover = (ImageView) findViewById(R.id.iv_mp4cover);
        rl_playmainview = (RelativeLayout) findViewById(R.id.rl_playmainview);
        ll_title_container = (LinearLayout) findViewById(R.id.ll_title_container);
        iv_mp3cover = (ImageView) findViewById(R.id.iv_mp3cover);
    }

    private void initlistener() {
//        sv_surfaceView.setZOrderOnTop(true);
//        sv_surfaceView.setBackgroundColor(R.color.black_a10_color);
        surfaceHolder = sv_surfaceView.getHolder();
        iv_start.setOnClickListener(this);
        iv_mp4cover.setOnClickListener(this);
        iv_fullscreen.setOnClickListener(this);
        sb_progress.setOnSeekBarChangeListener(this);
        surfaceHolder.addCallback(this);
        sv_surfaceView.setOnClickListener(this);
        ll_bottom_control.setOnClickListener(this);
        rl_playmainview.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        sb_progress.setOnTouchListener(this);
    }

    /**
     * <p>配置要播放的内容</p>
     *
     * @param url   视频地址 | Video address
     * @param url2  缩略图地址 | Thumbnail address
     * @param title 标题 | title
     */
    public void setUp(String url, String url2, String title) {
        setUp(url, url2, title, true);
    }

    /**
     * <p>配置要播放的内容</p>
     *
     * @param url         视频地址 | Video address
     * @param url2        缩略图地址 | Thumbnail address
     * @param title       标题 | title
     * @param ifShowTitle 是否在非全屏下显示标题 | The title is displayed in full-screen under
     */
    public void setUp(String url, String url2, String title, boolean ifShowTitle) {
        setSkin();//设置皮肤
        setIfShowTitle(ifShowTitle);//处理是否显示 标题
        if ((System.currentTimeMillis() - clickfullscreentime) < FULL_SCREEN_NORMAL_DELAY) {//全屏正常的延时 小于5s时，返回
            return;
        }
        this.url = url;
        this.url2 = url2;
        this.title = title;
        this.ifFullScreen = false;
        if (ifFullScreen) {
            iv_fullscreen.setImageResource(enlargRecId == 0 ? R.drawable.shrink_video : enlargRecId);
        } else {//不是全屏 设置全屏的图标
            iv_fullscreen.setImageResource(shrinkRecId == 0 ? R.drawable.enlarge_video : shrinkRecId);
        }

        tv_title.setText(title);//设置标题文字
        iv_start.setVisibility(View.VISIBLE);//设置 控制播放 按钮打开
        ll_bottom_control.setVisibility(View.INVISIBLE);//默认隐藏底部控制按钮
        pb_main_progressbar.setVisibility(View.VISIBLE);//显示主要的进度条
        CURRENT_STATE = CURRENT_STATE_NORMAL;
        setTitleVisibility(View.VISIBLE);

        if (uuid.equals(JCMediaManager.intance().uuid)) {
            JCMediaManager.intance().mediaPlayer.stop();
        }
        if (!TextUtils.isEmpty(url) && url.contains(".mp3")) {
            ifMp3 = true;
            iv_mp3cover.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(url2, iv_mp3cover, Utils.getDefaultDisplayImageOption());
        } else if (!TextUtils.isEmpty(url) && url.contains(".mp4")) {
            iv_mp4cover.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(url2, iv_mp4cover, Utils.getDefaultDisplayImageOption());
        }
    }

    /**
     * <p>只在全全屏中调用的方法</p>
     *
     * @param url   视频地址 | Video address
     * @param url2  缩略图地址 | Thumbnail address
     * @param title 标题 | title
     */
    public void setUpForFullscreen(String url, String url2, String title) {
        setSkin();
        this.url = url;
        this.url2 = url2;
        this.title = title;
        this.ifFullScreen = true;
        if (ifFullScreen) {
            iv_fullscreen.setImageResource(shrinkRecId == 0 ? R.drawable.shrink_video : shrinkRecId);
        } else {
            iv_fullscreen.setImageResource(enlargRecId == 0 ? R.drawable.enlarge_video : enlargRecId);
        }
        tv_title.setText(title);
        iv_start.setVisibility(View.VISIBLE);
        ll_bottom_control.setVisibility(View.INVISIBLE);
        pb_main_progressbar.setVisibility(View.VISIBLE);
        CURRENT_STATE = CURRENT_STATE_NORMAL;
        setTitleVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(url) && url.contains(".mp3")) {//如果是播放mp3
            ifMp3 = true;
            iv_mp3cover.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(url2, iv_mp3cover, Utils.getDefaultDisplayImageOption());
        } else if (!TextUtils.isEmpty(url) && url.contains(".mp4")) {//如果是播放mp4
            iv_mp4cover.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(url2, iv_mp4cover, Utils.getDefaultDisplayImageOption());
        }
    }

    /**
     * <p>只在全屏中调用的方法</p>
     *
     * @param state int state
     */
    public void setState(int state) {
        this.CURRENT_STATE = state;
        //全屏或取消全屏时继续原来的状态
        if (CURRENT_STATE == CURRENT_STATE_PREPAREING) {
            iv_start.setVisibility(View.INVISIBLE);
            iv_mp4cover.setVisibility(View.INVISIBLE);
            pb_loading.setVisibility(View.VISIBLE);
            iv_mp3cover.setVisibility(View.VISIBLE);
            setProgressAndTime(0, 0, 0);
            setProgressBuffered(0);
        } else if (CURRENT_STATE == CURRENT_STATE_PLAYING) {
            updateStartImage();
            iv_start.setVisibility(View.VISIBLE);
            sv_surfaceView.setVisibility(View.VISIBLE);//显示surfaceview
            ll_bottom_control.setVisibility(View.VISIBLE);
            pb_main_progressbar.setVisibility(View.INVISIBLE);
            setTitleVisibility(View.VISIBLE);
            iv_mp4cover.setVisibility(View.INVISIBLE);
            if (!ifMp3) {
                iv_mp3cover.setVisibility(View.INVISIBLE);
            }
            pb_loading.setVisibility(View.INVISIBLE);
        } else if (CURRENT_STATE == CURRENT_STATE_PAUSE) {
            updateStartImage();
            iv_start.setVisibility(View.VISIBLE);
            sv_surfaceView.setVisibility(View.VISIBLE);//显示surfaceview
            sv_surfaceView.setVisibility(View.INVISIBLE);
            ll_bottom_control.setVisibility(View.VISIBLE);
            pb_main_progressbar.setVisibility(View.INVISIBLE);
            setTitleVisibility(View.VISIBLE);
            iv_mp4cover.setVisibility(View.INVISIBLE);
            if (!ifMp3) {
                iv_mp3cover.setVisibility(View.INVISIBLE);
            }
        } else if (CURRENT_STATE == CURRENT_STATE_NORMAL) {
            if (uuid.equals(JCMediaManager.intance().uuid)) {
                JCMediaManager.intance().mediaPlayer.stop();
            }
            iv_start.setVisibility(View.VISIBLE);
            iv_mp4cover.setVisibility(View.VISIBLE);
            ll_bottom_control.setVisibility(View.INVISIBLE);
            pb_main_progressbar.setVisibility(View.VISIBLE);
            iv_mp3cover.setVisibility(View.VISIBLE);
            setTitleVisibility(View.VISIBLE);
            updateStartImage();
            cancelDismissControlViewTimer();
            cancelProgressTimer();
        }
    }

    public void onEventMainThread(VideoEvents videoEvents) {
        if (videoEvents.type == VideoEvents.VE_MEDIAPLAYER_FINISH_COMPLETE) {
//            if (CURRENT_STATE != CURRENT_STATE_PREPAREING) {
            cancelProgressTimer();
            iv_start.setImageResource(R.drawable.click_video_play_selector);
            iv_mp4cover.setVisibility(View.VISIBLE);
            iv_start.setVisibility(View.VISIBLE);
//            JCMediaPlayer.intance().mediaPlayer.setDisplay(null);
            //TODO 这里要将背景置黑，
//            sv_surfaceView.setBackgroundColor(R.color.black_a10_color);
            CURRENT_STATE = CURRENT_STATE_NORMAL;
            setKeepScreenOn(false);//控制屏幕灭屏
            sendPointEvent(ifFullScreen ? VideoEvents.POINT_AUTO_COMPLETE_FULLSCREEN : VideoEvents.POINT_AUTO_COMPLETE);
        }
        if (!JCMediaManager.intance().uuid.equals(uuid)) {
            if (videoEvents.type == VideoEvents.VE_START) {
                if (CURRENT_STATE != CURRENT_STATE_NORMAL) {
                    setState(CURRENT_STATE_NORMAL);
                }
            }
            return;
        }
        if (videoEvents.type == VideoEvents.VE_PREPARED) {
            if (CURRENT_STATE != CURRENT_STATE_PREPAREING) {//如果状态不是“正在准备”就退出
                return;
            }
            sv_surfaceView.setVisibility(View.VISIBLE);//显示surfaceview
            JCMediaManager.intance().mediaPlayer.setDisplay(surfaceHolder);//设置显示方式
            JCMediaManager.intance().mediaPlayer.start();//开始或恢复播放
            pb_loading.setVisibility(View.INVISIBLE);//隐藏加载进度圈
            if (!ifMp3){
                iv_mp3cover.setVisibility(View.INVISIBLE);//隐藏mp3缩略图
            }
            iv_mp4cover.setVisibility(View.INVISIBLE);//隐藏mp4缩略图
            ll_bottom_control.setVisibility(View.VISIBLE);//显示底部控制区域
            pb_main_progressbar.setVisibility(View.INVISIBLE);//隐藏主要的进度条
            CURRENT_STATE = CURRENT_STATE_PLAYING;//设置状态为正在播放

            startDismissControlViewTimer();//开启倒计时 隐藏“表面的控件”
            startProgressTimer();//控制每隔0.3s更新播放进度
        } else if (videoEvents.type == VideoEvents.VE_MEDIAPLAYER_UPDATE_BUFFER) {
            if (CURRENT_STATE != CURRENT_STATE_NORMAL || CURRENT_STATE != CURRENT_STATE_PREPAREING) {//不是初始化，或者不是正在准备的状态
                int percent = Integer.valueOf(videoEvents.obj.toString());
                setProgressBuffered(percent);//设置缓冲进度
            }
        } else if (videoEvents.type == VideoEvents.VE_MEDIAPLAYER_UPDATE_PROGRESS) {
            if (CURRENT_STATE != CURRENT_STATE_NORMAL || CURRENT_STATE != CURRENT_STATE_PREPAREING) {//不是初始化，或者不是正在准备的状态
                setProgressAndTimeFromTimer();//从计时器设置进度和时间
            }
        } else if (videoEvents.type == VideoEvents.VE_SURFACEHOLDER_FINISH_FULLSCREEN) {//关闭全屏时
            if (isClickFullscreen) {//如果是点击全屏的
                isFromFullScreenBackHere = true;//表示离开全屏
                isClickFullscreen = false;
                int prev_state = Integer.valueOf(videoEvents.obj.toString());
                setState(prev_state);
            }
        } else if (videoEvents.type == VideoEvents.VE_SURFACEHOLDER_CREATED) {
            if (isFromFullScreenBackHere) {
                JCMediaManager.intance().mediaPlayer.setDisplay(surfaceHolder);
                stopToFullscreenOrQuitFullscreenShowDisplay();
                isFromFullScreenBackHere = false;
                startDismissControlViewTimer();
            }
        } else if (videoEvents.type == VideoEvents.VE_MEDIAPLAYER_RESIZE) {
            int mVideoWidth = JCMediaManager.intance().currentVideoWidth;
            int mVideoHeight = JCMediaManager.intance().currentVideoHeight;
            if (mVideoWidth != 0 && mVideoHeight != 0) {
                surfaceHolder.setFixedSize(mVideoWidth, mVideoHeight);
                sv_surfaceView.requestLayout();
            }
        } else if (videoEvents.type == VideoEvents.VE_MEDIAPLAYER_SEEKCOMPLETE) {
            pb_loading.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 目前认为详细的判断和重复的设置是有相当必要的,也可以包装成方法
     */
    @Override
    public void onClick(View v) {
        int i = v.getId();

        if (i == R.id.iv_start) {
            if (TextUtils.isEmpty(url)) {
                Toast.makeText(getContext(), "视频地址为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if (CURRENT_STATE == CURRENT_STATE_NORMAL) {//如果为 初始化状态
                JCMediaManager.intance().clearWidthAndHeight();

                CURRENT_STATE = CURRENT_STATE_PREPAREING;//设置为 正在准备状态
                iv_start.setVisibility(View.INVISIBLE);//播放状态按钮 设置隐藏
                iv_mp4cover.setVisibility(View.VISIBLE);//音频缩略图 设置显示
                iv_mp3cover.setVisibility(View.VISIBLE);//音乐缩略图 设置显示
                pb_loading.setVisibility(View.VISIBLE);//加载进度圈 设置显示

                setProgressAndTime(0, 0, 0);//初始化进度
                setProgressBuffered(0);//初始化缓存进度
                JCMediaManager.intance().prepareToPlay(getContext(), url);//准备去播放
                JCMediaManager.intance().setUuid(uuid);//设置uuid
                Log.i(TAG, "play video");

                VideoEvents videoEvents = new VideoEvents().setType(VideoEvents.VE_START);
                videoEvents.obj = uuid;
                EventBus.getDefault().post(videoEvents);

                sv_surfaceView.requestLayout();
                setKeepScreenOn(true);//控制屏幕常亮
                sendPointEvent(i == R.id.iv_start ? VideoEvents.POINT_START_ICON : VideoEvents.POINT_START_THUMB);
            } else if (CURRENT_STATE == CURRENT_STATE_PLAYING) {
                CURRENT_STATE = CURRENT_STATE_PAUSE;
                iv_mp4cover.setVisibility(View.INVISIBLE);
                if (!ifMp3) {
                    iv_mp3cover.setVisibility(View.INVISIBLE);
                }
                JCMediaManager.intance().mediaPlayer.pause();
                Log.i(TAG, "pause video");

                updateStartImage();
                setKeepScreenOn(false);//控制屏幕灭屏
                cancelDismissControlViewTimer();
                sendPointEvent(ifFullScreen ? VideoEvents.POINT_STOP_FULLSCREEN : VideoEvents.POINT_STOP);
            } else if (CURRENT_STATE == CURRENT_STATE_PAUSE) {
                CURRENT_STATE = CURRENT_STATE_PLAYING;
                iv_mp4cover.setVisibility(View.INVISIBLE);
                if (!ifMp3) {
                    iv_mp3cover.setVisibility(View.INVISIBLE);
                }
                JCMediaManager.intance().mediaPlayer.start();
                Log.i(TAG, "go on video");

                updateStartImage();
                setKeepScreenOn(true);//控制屏幕常亮
                startDismissControlViewTimer();
                sendPointEvent(ifFullScreen ? VideoEvents.POINT_RESUME_FULLSCREEN : VideoEvents.POINT_RESUME);
            }

        } else if (i == R.id.iv_fullscreen) {
            if (ifFullScreen) {//如果是全屏
                quitFullScreen();
            } else {//打开全屏
                FullScreenActivity.skin = skin;
                JCMediaManager.intance().mediaPlayer.pause();
                JCMediaManager.intance().mediaPlayer.setDisplay(null);
                JCMediaManager.intance().backUpUuid();
                isClickFullscreen = true;
                FullScreenActivity.toActivityFromNormal(getContext(), CURRENT_STATE, url, url2, title);
                sendPointEvent(VideoEvents.POINT_ENTER_FULLSCREEN);
            }
            clickfullscreentime = System.currentTimeMillis();
        } else if (i == R.id.sv_surfaceView || i == R.id.rl_playmainview) {
            onClickToggleClear();
            startDismissControlViewTimer();
            sendPointEvent(ifFullScreen ? VideoEvents.POINT_CLICK_BLANK_FULLSCREEN : VideoEvents.POINT_CLICK_BLANK);
        } else if (i == R.id.ll_bottom_control) {
            //JCMediaPlayer.intance().mediaPlayer.setDisplay(surfaceHolder);
        } else if (i == R.id.iv_back) {
            quitFullScreen();
        }
    }

    private void startDismissControlViewTimer() {
        cancelDismissControlViewTimer();
        mDismissControlViewTimer = new Timer();
        mDismissControlViewTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (uuid.equals(JCMediaManager.intance().uuid)) {
                    if (getContext() != null && getContext() instanceof Activity) {
                        ((Activity) getContext()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dismissControlView();
                            }
                        });
                    }
                }
            }
        }, 2500);
    }

    //只是onClickToggleClear这个方法中逻辑的一部分
    private void dismissControlView() {
        ll_bottom_control.setVisibility(View.INVISIBLE);//隐藏底部控制区域
        pb_main_progressbar.setVisibility(View.VISIBLE);//显示主要的进度条
        setTitleVisibility(View.INVISIBLE);//隐藏标题
        iv_start.setVisibility(View.INVISIBLE);//隐藏播放控制按钮
    }

    private void cancelDismissControlViewTimer() {
        if (mDismissControlViewTimer != null) {
            mDismissControlViewTimer.cancel();
        }
    }

    private void onClickToggleClear() {
        if (CURRENT_STATE == CURRENT_STATE_PREPAREING) {
            if (ll_bottom_control.getVisibility() == View.VISIBLE) {
                ll_bottom_control.setVisibility(View.INVISIBLE);
                pb_main_progressbar.setVisibility(View.VISIBLE);
                setTitleVisibility(View.INVISIBLE);
            } else {
                ll_bottom_control.setVisibility(View.VISIBLE);
                pb_main_progressbar.setVisibility(View.INVISIBLE);
                setTitleVisibility(View.VISIBLE);
            }
            iv_start.setVisibility(View.INVISIBLE);
            pb_loading.setVisibility(View.VISIBLE);
        } else if (CURRENT_STATE == CURRENT_STATE_PLAYING) {
            if (ll_bottom_control.getVisibility() == View.VISIBLE) {
                ll_bottom_control.setVisibility(View.INVISIBLE);
                pb_main_progressbar.setVisibility(View.VISIBLE);
                setTitleVisibility(View.INVISIBLE);
                iv_start.setVisibility(View.INVISIBLE);
            } else {
                updateStartImage();
                iv_start.setVisibility(View.VISIBLE);
                ll_bottom_control.setVisibility(View.VISIBLE);
                pb_main_progressbar.setVisibility(View.INVISIBLE);
                setTitleVisibility(View.VISIBLE);
            }
            pb_loading.setVisibility(View.INVISIBLE);
        } else if (CURRENT_STATE == CURRENT_STATE_PAUSE) {
            if (ll_bottom_control.getVisibility() == View.VISIBLE) {
                ll_bottom_control.setVisibility(View.INVISIBLE);
                pb_main_progressbar.setVisibility(View.VISIBLE);
                setTitleVisibility(View.INVISIBLE);
                iv_start.setVisibility(View.INVISIBLE);
            } else {
                updateStartImage();
                iv_start.setVisibility(View.VISIBLE);
                ll_bottom_control.setVisibility(View.VISIBLE);
                pb_main_progressbar.setVisibility(View.INVISIBLE);
                setTitleVisibility(View.VISIBLE);
            }
            pb_loading.setVisibility(View.INVISIBLE);
        }
    }

    private void startProgressTimer() {
        cancelProgressTimer();
        mUpdateProgressTimer = new Timer();
        mUpdateProgressTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (getContext() != null && getContext() instanceof Activity) {
                    ((Activity) getContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            VideoEvents videoEvents = new VideoEvents().setType(VideoEvents.VE_MEDIAPLAYER_UPDATE_PROGRESS);
                            EventBus.getDefault().post(videoEvents);
                        }
                    });
                }
            }
        }, 0, 300);
    }

    private void cancelProgressTimer() {
        if (uuid.equals(JCMediaManager.intance().uuid)) {
            if (mUpdateProgressTimer != null) {
                mUpdateProgressTimer.cancel();
            }
        }
    }

    public void setIfShowTitle(boolean ifShowTitle) {
        this.ifShowTitle = ifShowTitle;
    }

    /**
     * 设置标题显示与隐藏
     *
     * @param visable
     */
    private void setTitleVisibility(int visable) {
        if (ifShowTitle) {
            ll_title_container.setVisibility(visable);
        } else {
            if (ifFullScreen) {
                ll_title_container.setVisibility(visable);
            } else {
                ll_title_container.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 更新播放图标
     */
    private void updateStartImage() {
        if (CURRENT_STATE == CURRENT_STATE_PLAYING) {
            iv_start.setImageResource(R.drawable.click_video_pause_selector);
        } else {
            iv_start.setImageResource(R.drawable.click_video_play_selector);
        }
    }

    /**
     * 设置 “缓冲进度”
     *
     * @param secProgress
     */
    private void setProgressBuffered(int secProgress) {
        if (secProgress >= 0) {
            sb_progress.setSecondaryProgress(secProgress);
            pb_main_progressbar.setSecondaryProgress(secProgress);
        }
    }

    /**
     * 从计时器设置进度和时间
     */
    private void setProgressAndTimeFromTimer() {
        int position = JCMediaManager.intance().mediaPlayer.getCurrentPosition();
        int duration = JCMediaManager.intance().mediaPlayer.getDuration();
        int progress = position * 100 / duration;
        setProgressAndTime(progress, position, duration);
    }

    /**
     * 初始化进度，当前时间，总时间
     *
     * @param progress
     * @param currentTime
     * @param totalTime
     */
    private void setProgressAndTime(int progress, int currentTime, int totalTime) {
        if (!touchingProgressBar) {//如果没有正在触摸进度条seekbar
            sb_progress.setProgress(progress);
            pb_main_progressbar.setProgress(progress);
        }
        tv_currentTime.setText(Utils.stringForTime(currentTime));
        tv_totalTime.setText(Utils.stringForTime(totalTime));
    }

    public void release() {
        if ((System.currentTimeMillis() - clickfullscreentime) < FULL_SCREEN_NORMAL_DELAY) return;
        setState(CURRENT_STATE_NORMAL);
        //回收surfaceview
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            int time = progress * JCMediaManager.intance().mediaPlayer.getDuration() / 100;
            JCMediaManager.intance().mediaPlayer.seekTo(time);
            pb_loading.setVisibility(View.VISIBLE);
            iv_start.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    /**
     * 视图从界面分离时
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getDefault().unregister(this);
//        cancelDismissControlViewTimer();
        if (uuid.equals(JCMediaManager.intance().uuid)) {
            JCMediaManager.intance().mediaPlayer.stop();
        }
    }

    /**
     * 试图加载到界面时
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    /**
     *
     */
    public void quitFullScreen() {//关闭全屏
        FullScreenActivity.manualQuit = true;
        clickfullscreentime = System.currentTimeMillis();
        JCMediaManager.intance().mediaPlayer.pause();
        JCMediaManager.intance().mediaPlayer.setDisplay(null);
        JCMediaManager.intance().revertUuid();
        VideoEvents videoEvents = new VideoEvents().setType(VideoEvents.VE_SURFACEHOLDER_FINISH_FULLSCREEN);
        videoEvents.obj = CURRENT_STATE;
        EventBus.getDefault().post(videoEvents);
        sendPointEvent(VideoEvents.POINT_QUIT_FULLSCREEN);
    }

    /**
     * 停止全屏或退出全屏显示 展开
     */
    private void stopToFullscreenOrQuitFullscreenShowDisplay() {
        if (CURRENT_STATE == CURRENT_STATE_PAUSE) {
            JCMediaManager.intance().mediaPlayer.start();
            CURRENT_STATE = CURRENT_STATE_PLAYING;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ((Activity) getContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JCMediaManager.intance().mediaPlayer.pause();
                            CURRENT_STATE = CURRENT_STATE_PAUSE;
                        }
                    });
                }
            }).start();
            sv_surfaceView.requestLayout();
        } else if (CURRENT_STATE == CURRENT_STATE_PLAYING) {
            JCMediaManager.intance().mediaPlayer.start();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        EventBus.getDefault().post(new VideoEvents().setType(VideoEvents.VE_SURFACEHOLDER_CREATED));
        if (ifFullScreen) {
            JCMediaManager.intance().mediaPlayer.setDisplay(surfaceHolder);
            stopToFullscreenOrQuitFullscreenShowDisplay();
        }
        if (CURRENT_STATE != CURRENT_STATE_NORMAL) {
            startDismissControlViewTimer();
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    /**
     * <p>停止所有音频的播放</p>
     */
    public static void releaseAllVideos() {
        if (!isClickFullscreen) {
            JCMediaManager.intance().mediaPlayer.stop();
            JCMediaManager.intance().setUuid("");
            JCMediaManager.intance().setUuid("");
            EventBus.getDefault().post(new VideoEvents().setType(VideoEvents.VE_MEDIAPLAYER_FINISH_COMPLETE));
        }
    }

    /**
     * <p>有特殊需要的客户端</p>
     *
     * @param onClickListener 开始按钮点击的回调函数 | Click the Start button callback function
     */
    @Deprecated
    public void setStartListener(OnClickListener onClickListener) {
        if (onClickListener != null) {
            iv_start.setOnClickListener(onClickListener);
            iv_mp4cover.setOnClickListener(onClickListener);
        } else {
            iv_start.setOnClickListener(this);
            iv_mp4cover.setOnClickListener(this);
        }
    }

    private void sendPointEvent(int type) {
        VideoEvents videoEvents = new VideoEvents();
        videoEvents.setType(type);
        videoEvents.obj = title;
        videoEvents.obj1 = url;
        EventBus.getDefault().post(videoEvents);
    }

    public void setSeekbarOnTouchListener(OnTouchListener listener) {
        mSeekbarOnTouchListener = listener;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchingProgressBar = true;
                cancelDismissControlViewTimer();
                cancelProgressTimer();
                break;
            case MotionEvent.ACTION_UP:
                touchingProgressBar = false;
                startDismissControlViewTimer();
                startProgressTimer();
                sendPointEvent(ifFullScreen ? VideoEvents.POINT_CLICK_SEEKBAR_FULLSCREEN : VideoEvents.POINT_CLICK_SEEKBAR);
                break;
        }

        if (mSeekbarOnTouchListener != null) {
            mSeekbarOnTouchListener.onTouch(v, event);
        }
        return false;
    }

    /**
     * <p>只设置这一个播放器的皮肤<br>
     * 这个需要在setUp播放器的属性之前调用，因为enlarge图标的原因<br>
     * 所有参数如果不需要修改的设为0</p>
     *
     * @param titleColor              标题颜色 | title color
     * @param timeColor               时间颜色 | time color
     * @param seekDrawable            滑动条颜色 | seekbar color
     * @param bottomControlBackground 低栏背景 | background color
     * @param enlargRecId             全屏背景 | FullScreen background
     * @param shrinkRecId             退出全屏背景 | quit FullScreen background quit FullScreen
     */
    public void setSkin(int titleColor, int timeColor, int seekDrawable, int bottomControlBackground,
                        int enlargRecId, int shrinkRecId) {
        skin = new Skin(titleColor, timeColor, seekDrawable, bottomControlBackground,
                enlargRecId, shrinkRecId);
    }

    /**
     * <p>设置应用内所有播放器的皮肤</p>
     */
    public static void setGlobleSkin(int titleColor, int timeColor, int seekDrawable, int bottomControlBackground,
                                     int enlargRecId, int shrinkRecId) {
        globleSkin = new Skin(titleColor, timeColor, seekDrawable, bottomControlBackground,
                enlargRecId, shrinkRecId);
    }

    public static void toFullscreenActivity(Context context, String url, String url2, String title) {
        FullScreenActivity.toActivity(context, url, url2, title);
    }

    public void setSkin() {
        if (skin != null) {
            setSkin(skin);
        } else {
            if (globleSkin != null) {
                setSkin(globleSkin);
            }
        }
    }

    /**
     * 设置背景（控件的背景色）
     *
     * @param skin
     */
    private void setSkin(Skin skin) {
        Resources resource = getContext().getResources();
        if (skin.titleColor != 0) {
            ColorStateList titleCsl = resource.getColorStateList(skin.titleColor);
            if (titleCsl != null) {
                tv_title.setTextColor(titleCsl);
            }
        }
        if (skin.timeColor != 0) {
            ColorStateList timeCsl = resource.getColorStateList(skin.timeColor);
            if (timeCsl != null) {
                tv_currentTime.setTextColor(timeCsl);
                tv_totalTime.setTextColor(timeCsl);
            }
        }
        if (skin.seekDrawable != 0) {
            Drawable bg = resource.getDrawable(skin.seekDrawable);
            Rect bounds = sb_progress.getProgressDrawable().getBounds();
            sb_progress.setProgressDrawable(bg);
            sb_progress.getProgressDrawable().setBounds(bounds);
            pb_main_progressbar.setProgressDrawable(resource.getDrawable(skin.seekDrawable));
        }
        if (skin.bottomControlBackground != 0) {
            ll_bottom_control.setBackgroundColor(resource.getColor(skin.bottomControlBackground));
        }
        this.enlargRecId = skin.enlargRecId;
        this.shrinkRecId = skin.shrinkRecId;
    }
}
