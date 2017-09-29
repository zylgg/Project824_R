package com.example.mr_zyl.project.pro.essence.view.adapter;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mr_zyl.project.R;
import com.example.mr_zyl.project.bean.PostsListBean;
import com.example.mr_zyl.project.pro.base.view.refreshview.recyclerview.BaseRecyclerAdapter;
import com.example.mr_zyl.project.pro.essence.view.activity.BrowerActivity;
import com.example.mr_zyl.project.pro.essence.view.selfview.ExpandableTextView;
import com.example.mr_zyl.project.pro.essence.view.selfview.PlayVideoIconView;
import com.example.mr_zyl.project.pro.essence.view.selfview.RingView;
import com.example.mr_zyl.project.utils.MyDateUtils;
import com.example.mr_zyl.project.utils.DisplayUtil;
import com.example.zylsmallvideolibrary.JCVideoPlayer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.xiaopan.sketch.SketchImageView;
import me.xiaopan.sketch.display.DefaultImageDisplayer;
import me.xiaopan.sketch.display.FadeInImageDisplayer;
import me.xiaopan.sketch.request.DownloadProgressListener;

/**
 * Created by TFHR02 on 2016/11/15.
 */
public class EssenceRecycleAdapter extends BaseRecyclerAdapter<EssenceRecycleAdapter.EssenceViewHolders> {
    private List<PostsListBean.PostList> lists;
    private Context context;
    private int itemtype = 0;//0,pic;1,bigpic;2,gif;3,视频;4,音乐
    private float view_w = 0.0f;
    private SparseBooleanArray sparseBooleanArray;

    public EssenceRecycleAdapter(Context context, List<PostsListBean.PostList> lists) {
        this.context = context;
        this.lists = lists;
        this.sparseBooleanArray=new SparseBooleanArray();
    }

    /**
     * 重新定制数据
     *
     * @param lists
     */
    public void RefreshData(List<PostsListBean.PostList> lists) {
        for (PostsListBean.PostList postList : lists) {
            float img_w = Float.parseFloat(postList.getWidth());
            float img_h = Float.parseFloat(postList.getHeight());
            view_w = DisplayUtil.Width(context) - DisplayUtil.dip2px(context, 6 * 2);
            float view_h = DisplayUtil.Height(context);

            float scale = view_w / img_w;
            //如果图片缩放后的尺寸大于屏幕高度=大图
            if (postList.getIs_gif() != null && postList.getIs_gif().equals("0") && img_h > view_h) {
                //不是gif图片，而是长图（大图），
                postList.setLargeimg_zoom(scale);
                postList.setIs_largepic(true);
                postList.setIs_showOnClickBrowerView(View.VISIBLE);
                postList.setView_maxheight(DisplayUtil.dip2px(context, 300));
            } else {//普通图片
                postList.setIs_showOnClickBrowerView(View.GONE);
                postList.setLargeimg_zoom(scale);
                postList.setIs_largepic(false);
                postList.setView_maxheight((int) (img_h * scale));
                if (postList.getIs_gif() != null && postList.getIs_gif().equals("1")) {//如果是gif图
                    postList.setIs_largepic(false);
                    postList.setView_maxheight((int) (img_h * scale));
                }
            }
            if (postList.getVideouri() != null && postList.getVideouri().endsWith("mp4")) {//如果是视频
                postList.setIs_video(true);
                postList.setIs_showvideotag(View.VISIBLE);
                if (img_h * scale > DisplayUtil.dip2px(context, 300)) {//以300为基准
                    postList.setView_maxheight(DisplayUtil.dip2px(context, 300));
                } else {
                    postList.setView_maxheight((int) (img_h * scale));
                }
            } else if (postList.getVoiceuri() != null && postList.getVoiceuri().endsWith("mp3")) {//如果是mp3
                postList.setIs_showvideotag(View.GONE);
                postList.setIs_mp3(true);
                if (img_h * scale <= img_w) {//如果缩放后高<宽
                    postList.setView_maxheight((int) (img_h * scale));
                } else {
                    postList.setView_maxheight(DisplayUtil.dip2px(context, 300));
                }
            } else {
                postList.setIs_video(false);
                postList.setIs_mp3(false);
            }
        }
        this.lists = lists;

        notifyDataSetChanged();
    }

    @Override
    public EssenceViewHolders getViewHolder(View view) {

        return new EssenceViewHolders(view, false);
    }

    @Override
    public int getItemViewType(int position) {
        if (lists != null && lists.size() > 0 && lists.size() > position) {
            PostsListBean.PostList postLists = lists.get(position);
            if (postLists != null) {
                if (postLists.is_largepic()) {
                    return 1;
                } else if (postLists.getIs_gif() != null && postLists.getIs_gif().equals("1")) {
                    return 2;
                } else if (postLists.is_video()) {
                    return 3;
                } else if (postLists.is_mp3()) {
                    return 4;
                } else if (postLists.getIs_gif() != null && !postLists.is_largepic() && postLists.getIs_gif().equals("0")) {
                    return 0;
                }
            }
        }
        return super.getItemViewType(position);
    }

    @Override
    public EssenceViewHolders onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        LayoutInflater inflates = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
//        View views = LayoutInflater.from(context).inflate(R.layout.item_essence_video_layout, null, false);
        View views = inflates.inflate(R.layout.item_essence_video_layout, parent, false);

        ImageView iv_pic = (ImageView) views.findViewById(R.id.siv_pic);
        SketchImageView siv_largepic = (SketchImageView) views.findViewById(R.id.siv_largepic);
        SketchImageView siv_gifpic = (SketchImageView) views.findViewById(R.id.siv_gifpic);
        JCVideoPlayer jcv_videopic = (JCVideoPlayer) views.findViewById(R.id.jcv_videopic);
        if (viewType == 0) {
            iv_pic.setVisibility(View.VISIBLE);
            siv_largepic.setVisibility(View.GONE);
            siv_gifpic.setVisibility(View.GONE);
            jcv_videopic.setVisibility(View.GONE);
        } else if (viewType == 1) {
            iv_pic.setVisibility(View.GONE);
            siv_largepic.setVisibility(View.VISIBLE);
            siv_gifpic.setVisibility(View.GONE);
            jcv_videopic.setVisibility(View.GONE);
        } else if (viewType == 2) {
            iv_pic.setVisibility(View.GONE);
            siv_largepic.setVisibility(View.GONE);
            siv_gifpic.setVisibility(View.VISIBLE);
            jcv_videopic.setVisibility(View.GONE);
        } else if (viewType == 3) {
            iv_pic.setVisibility(View.GONE);
            siv_largepic.setVisibility(View.GONE);
            siv_gifpic.setVisibility(View.GONE);
            jcv_videopic.setVisibility(View.VISIBLE);
        } else if (viewType == 4) {
            iv_pic.setVisibility(View.GONE);
            siv_largepic.setVisibility(View.GONE);
            siv_gifpic.setVisibility(View.GONE);
            jcv_videopic.setVisibility(View.VISIBLE);
        }
        EssenceViewHolders holders = new EssenceViewHolders(views, true);
        holders.itemtype = viewType;
        return holders;
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final EssenceViewHolders holder, int position, boolean isItem) {
        final PostsListBean.PostList postList = this.lists.get(position);
        ImageLoader.getInstance().displayImage(postList.getProfile_image(), holder.iv_header);

        holder.tv_name.setText(postList.getName());
        holder.tv_time.setText(MyDateUtils.parseDate(postList.getCreate_time()));
        //设置内容
        holder.etv_content.setText(postList.getText(),sparseBooleanArray,position);
        //是否显示数据详情标记(针对gif的)
        holder.tv_commondata_detail.setVisibility(postList.getIs_showOnClickBrowerView());
        //是否显示视频标记
//        holder.pviv_video_detail.setVisibility(postList.getIs_showvideotag());
        //默认加载进度圈隐藏
        holder.rv_loadprogress.setVisibility(View.GONE);
        //默认不显示视频播放控件
        holder.jcv_videopic.setVisibility(View.GONE);
        // 普通图
        if (holder.itemtype == 0) {
            holder.iv_pic.getLayoutParams().height = postList.getView_maxheight();
            ImageLoader.getInstance().displayImage(postList.getCdn_img(), holder.iv_pic, options, null, new ImageLoadingProgressListener() {
                @Override
                public void onProgressUpdate(String imageUri, View view, int current, int total) {
                    holder.rv_loadprogress.setVisibility(View.VISIBLE);
                    float angle = (Float.valueOf(current) / Float.valueOf(total)) * 360;
                    holder.rv_loadprogress.setAngle(angle);
                }
            });
            holder.iv_pic.setOnClickListener(new pic_onclicklistener(postList.getCdn_img(), postList.is_largepic(), postList.getVideouri()));
        }
        //大图
        if (holder.itemtype == 1) {
            holder.siv_largepic.getOptions()
                    .setImageDisplayer(new DefaultImageDisplayer())
//                    .setCacheProcessedImageInDisk(true)// 为了加快速度，将经过ImageProcessor、resize或thumbnailMode处理过或者读取时inSampleSize大于等于8的图片保存到磁盘缓存中，下次就直接读取
                    .setCacheInMemoryDisabled(true);// 禁用内存缓存
            holder.siv_largepic.setSupportZoom(true);//大图才支持缩放
            holder.siv_largepic.setSupportLargeImage(true);//支持大图
            holder.siv_largepic.getImageZoomer().zoom(postList.getLargeimg_zoom());//放大n倍 （2：需要自定义）
            holder.siv_largepic.getImageZoomer().setReadMode(true);//开启阅读模式
            holder.siv_largepic.getImageZoomer().setZoomable(false);//禁用手势缩放

            holder.siv_largepic.setDownloadProgressColor(Color.TRANSPARENT);
            holder.siv_largepic.setShowDownloadProgress(true);

            holder.siv_largepic.setDownloadProgressListener(new DownloadProgressListener() {
                @Override
                public void onUpdateDownloadProgress(int totalLength, int completedLength) {
                    holder.rv_loadprogress.setVisibility(View.VISIBLE);
                    float angle = (Float.valueOf(completedLength) / Float.valueOf(totalLength)) * 360;
                    holder.rv_loadprogress.setAngle(angle);
                }
            });

            holder.siv_largepic.displayImage(postList.getCdn_img());
            holder.siv_largepic.setOnClickListener(new pic_onclicklistener(postList.getCdn_img(), postList.is_largepic(), postList.getVideouri()));
        }
        //gif图片
        if (holder.itemtype == 2) {
            holder.siv_gifpic.getLayoutParams().width = (int) view_w;
            holder.siv_gifpic.getLayoutParams().height = postList.getView_maxheight();
            holder.siv_gifpic.getOptions()
                    .setImageDisplayer(new FadeInImageDisplayer())
                    .setDecodeGifImage(true);//支持加载gif图

            holder.siv_gifpic.setDownloadProgressColor(Color.TRANSPARENT);
            holder.siv_gifpic.setShowDownloadProgress(true);
            holder.siv_gifpic.setDownloadProgressListener(new DownloadProgressListener() {
                @Override
                public void onUpdateDownloadProgress(int totalLength, int completedLength) {
                    holder.rv_loadprogress.setVisibility(View.VISIBLE);
                    float angle = (Float.valueOf(completedLength) / Float.valueOf(totalLength)) * 360;
                    holder.rv_loadprogress.setAngle(angle);
                }
            });

            holder.siv_gifpic.displayImage(postList.getCdn_img());
            holder.siv_gifpic.setOnClickListener(new pic_onclicklistener(postList.getCdn_img(), postList.is_largepic(), postList.getVideouri()));
        }
        //video视频
        if (holder.itemtype == 3) {
            holder.jcv_videopic.setVisibility(View.VISIBLE);
            holder.jcv_videopic.getLayoutParams().width = (int) view_w;
            holder.jcv_videopic.getLayoutParams().height = postList.getView_maxheight();
            holder.jcv_videopic.setUp(postList.getVideouri(), postList.getBimageuri(), postList.getText(), false,postList.getPlaycount(),Integer.valueOf(postList.getVideotime()));
        }
        //voice声音
        if (holder.itemtype == 4) {
            holder.jcv_videopic.setVisibility(View.VISIBLE);
            holder.jcv_videopic.getLayoutParams().width = (int) view_w;
            holder.jcv_videopic.getLayoutParams().height = postList.getView_maxheight();
            holder.jcv_videopic.setUp(postList.getVoiceuri(), postList.getBimageuri(), postList.getText(), false,postList.getPlaycount(),Integer.valueOf(postList.getVoicetime()));
        }
        holder.tv_like.setText(postList.getDing());
        holder.tv_dislike.setText(postList.getCai());
        holder.tv_forword.setText(postList.getRepost());
        holder.tv_comment.setText(postList.getComment());
    }

    private class pic_onclicklistener implements View.OnClickListener {
        private String picurl = "";
        private boolean is_largepic = false;
        private String videouri = "";

        public pic_onclicklistener(String picurl, boolean is_largepic, String videouri) {
            this.picurl = picurl;
            this.is_largepic = is_largepic;
            this.videouri = videouri;
        }

        @Override
        public void onClick(View v) {
            if (!TextUtils.isEmpty(picurl) && videouri.isEmpty()) {
                Intent browerAIntent = new Intent();
                browerAIntent.setClass(context, BrowerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("picurl", picurl);
                bundle.putBoolean("is_largepic", is_largepic);

                browerAIntent.putExtra("pic_bundle", bundle);
                context.startActivity(browerAIntent);
            } else if (!TextUtils.isEmpty(videouri)) {
                Uri uri = Uri.parse(videouri);
                // 调用系统自带的播放器来播放流媒体视频
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "video/mp4");
                context.startActivity(intent);
            }

        }
    }

    private DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.item_essence_video_bg)//正在加载时的图片
            .showImageOnFail(R.drawable.item_essence_video_bg)//加载失败时的图片
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .build();

    @Override
    public int getAdapterItemCount() {
        return lists.size();
    }

    class EssenceViewHolders extends RecyclerView.ViewHolder {
        public CircleImageView iv_header;
        public TextView tv_name;
        public TextView tv_time;
        public ExpandableTextView etv_content;
        public ImageView iv_pic;
        public SketchImageView siv_largepic, siv_gifpic;
        public TextView tv_commondata_detail;

        public LinearLayout ll_like;
        public TextView tv_like;

        public LinearLayout ll_dislike;
        public TextView tv_dislike;

        public LinearLayout ll_forword;
        public TextView tv_forword;

        public LinearLayout ll_comment;
        public TextView tv_comment;
        public RingView rv_loadprogress;
        public int itemtype;
        public PlayVideoIconView pviv_video_detail;
        public JCVideoPlayer jcv_videopic;

        public EssenceViewHolders(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                iv_header = (CircleImageView) itemView.findViewById(R.id.iv_header);
                tv_name = (TextView) itemView.findViewById(R.id.tv_name);
                tv_time = (TextView) itemView.findViewById(R.id.tv_time);
                rv_loadprogress = (RingView) itemView.findViewById(R.id.rv_loadprogress);
                etv_content = (ExpandableTextView) itemView.findViewById(R.id.etv_content);
                iv_pic = (ImageView) itemView.findViewById(R.id.siv_pic);
                siv_largepic = (SketchImageView) itemView.findViewById(R.id.siv_largepic);
                siv_gifpic = (SketchImageView) itemView.findViewById(R.id.siv_gifpic);
                jcv_videopic = (JCVideoPlayer) itemView.findViewById(R.id.jcv_videopic);
                tv_commondata_detail = (TextView) itemView.findViewById(R.id.tv_commondata_detail);

                ll_like = (LinearLayout) itemView.findViewById(R.id.ll_like);
                tv_like = (TextView) itemView.findViewById(R.id.tv_like);

                ll_dislike = (LinearLayout) itemView.findViewById(R.id.ll_dislike);
                tv_dislike = (TextView) itemView.findViewById(R.id.tv_dislike);

                ll_forword = (LinearLayout) itemView.findViewById(R.id.ll_forword);
                tv_forword = (TextView) itemView.findViewById(R.id.tv_forword);

                ll_comment = (LinearLayout) itemView.findViewById(R.id.ll_comment);
                tv_comment = (TextView) itemView.findViewById(R.id.tv_comment);

                pviv_video_detail = (PlayVideoIconView) itemView.findViewById(R.id.pviv_video_detail);
            }
        }


    }


}
