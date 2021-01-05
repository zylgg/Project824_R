package com.example.mr_zyl.project824.bean;

import android.view.View;
import android.widget.ImageView;

import java.util.List;


/**
 * 基础数据结构
 */
public class PostsListBean {
    private Info info;
    private List<PostList> list;

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public List<PostList> getList() {
        return list;
    }

    public void setList(List<PostList> list) {
        this.list = list;
    }

    public class Info {
        private String vendor;
        private int count;
        private int page;
        private String maxid;
        private String maxtime;

        public String getVendor() {
            return vendor;
        }

        public void setVendor(String vendor) {
            this.vendor = vendor;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getMaxid() {
            return maxid;
        }

        public void setMaxid(String maxid) {
            this.maxid = maxid;
        }

        public String getMaxtime() {
            return maxtime;
        }

        public void setMaxtime(String maxtime) {
            this.maxtime = maxtime;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }
    }


    public class PostList {
        private String id;
        private String type;
        private String text;
        private String user_id;
        private String name;
        private String screen_name;
        private String profile_image;
        private String created_at;
        private String create_time;
        private String passtime;
        private String love;
        private String hate;
        private String comment;
        private String repost;
        private String bookmark;
        private String bimageuri;
        private String voiceuri;
        private String voicetime;
        private String voicelength;
        private String status;
        private String theme_id;
        private String theme_name;
        private String theme_type;
        private String videouri;
        private String videotime;
        private String original_pid;
        private int cache_version;
        private String playcount;
        private String cai;
        private Object top_cmt;
        private String weixin_url;
        private Object themes;
        private String image0;
        private String image2;
        private String image1;
        private String cdn_img;
        private String is_gif;
        private String gifFistFrame;
        private String width;
        private String height;
        private String tag;
        private int t;
        private String ding;
        private int favourite;

        //外加属性
        private boolean is_largepic;//是不是大图(是，View.VISIBLE;否，View.GONE)
        private int is_showOnClickBrowerView = View.GONE;//是否显示点击查看控件
        private float largeimg_zoom = 1;//如果是大图与屏幕 对比的缩放比
        private int view_maxheight;//view需要设置的最大高度
        private ImageView.ScaleType viewScaleType;//IV的缩放类型
        private boolean is_video = false;//是不是视频
        private boolean is_mp3 = false;//是不是mp3
        private int is_showvideotag = View.GONE;//是否显示视频标记

        public boolean is_mp3() {
            return is_mp3;
        }

        public void setIs_mp3(boolean is_mp3) {
            this.is_mp3 = is_mp3;
        }

        public int getIs_showvideotag() {
            return is_showvideotag;
        }

        public void setIs_showvideotag(int is_showvideotag) {
            this.is_showvideotag = is_showvideotag;
        }

        public boolean is_video() {
            return is_video;
        }

        public void setIs_video(boolean is_video) {
            this.is_video = is_video;
        }

        public ImageView.ScaleType getViewScaleType() {
            return viewScaleType;
        }

        public void setViewScaleType(ImageView.ScaleType viewScaleType) {
            this.viewScaleType = viewScaleType;
        }

        public int getView_maxheight() {
            return view_maxheight;
        }

        public void setView_maxheight(int view_maxheight) {
            this.view_maxheight = view_maxheight;
        }

        public int getIs_showOnClickBrowerView() {
            return is_showOnClickBrowerView;
        }

        public void setIs_showOnClickBrowerView(int is_showOnClickBrowerView) {
            this.is_showOnClickBrowerView = is_showOnClickBrowerView;
        }

        public boolean is_largepic() {
            return is_largepic;
        }

        public void setIs_largepic(boolean is_largepic) {
            this.is_largepic = is_largepic;
        }

        public float getLargeimg_zoom() {
            return largeimg_zoom;
        }

        public void setLargeimg_zoom(float largeimg_zoom) {
            this.largeimg_zoom = largeimg_zoom;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getScreen_name() {
            return screen_name;
        }

        public void setScreen_name(String screen_name) {
            this.screen_name = screen_name;
        }

        public String getProfile_image() {
            return profile_image;
        }

        public void setProfile_image(String profile_image) {
            this.profile_image = profile_image;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getPasstime() {
            return passtime;
        }

        public void setPasstime(String passtime) {
            this.passtime = passtime;
        }

        public String getLove() {
            return love;
        }

        public void setLove(String love) {
            this.love = love;
        }

        public String getHate() {
            return hate;
        }

        public void setHate(String hate) {
            this.hate = hate;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getRepost() {
            return repost;
        }

        public void setRepost(String repost) {
            this.repost = repost;
        }

        public String getBookmark() {
            return bookmark;
        }

        public void setBookmark(String bookmark) {
            this.bookmark = bookmark;
        }

        public String getBimageuri() {
            return bimageuri;
        }

        public void setBimageuri(String bimageuri) {
            this.bimageuri = bimageuri;
        }

        public String getVoiceuri() {
            return voiceuri;
        }

        public void setVoiceuri(String voiceuri) {
            this.voiceuri = voiceuri;
        }

        public String getVoicetime() {
            return voicetime;
        }

        public void setVoicetime(String voicetime) {
            this.voicetime = voicetime;
        }

        public String getVoicelength() {
            return voicelength;
        }

        public void setVoicelength(String voicelength) {
            this.voicelength = voicelength;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTheme_id() {
            return theme_id;
        }

        public void setTheme_id(String theme_id) {
            this.theme_id = theme_id;
        }

        public String getTheme_name() {
            return theme_name;
        }

        public void setTheme_name(String theme_name) {
            this.theme_name = theme_name;
        }

        public String getTheme_type() {
            return theme_type;
        }

        public void setTheme_type(String theme_type) {
            this.theme_type = theme_type;
        }

        public String getVideouri() {
            return videouri;
        }

        public void setVideouri(String videouri) {
            this.videouri = videouri;
        }

        public String getVideotime() {
            return videotime;
        }

        public void setVideotime(String videotime) {
            this.videotime = videotime;
        }

        public String getOriginal_pid() {
            return original_pid;
        }

        public void setOriginal_pid(String original_pid) {
            this.original_pid = original_pid;
        }

        public int getCache_version() {
            return cache_version;
        }

        public void setCache_version(int cache_version) {
            this.cache_version = cache_version;
        }

        public String getCai() {
            return cai;
        }

        public void setCai(String cai) {
            this.cai = cai;
        }

        public String getWeixin_url() {
            return weixin_url;
        }

        public void setWeixin_url(String weixin_url) {
            this.weixin_url = weixin_url;
        }

        public String getImage0() {
            return image0;
        }

        public void setImage0(String image0) {
            this.image0 = image0;
        }

        public String getImage2() {
            return image2;
        }

        public void setImage2(String image2) {
            this.image2 = image2;
        }

        public String getImage1() {
            return image1;
        }

        public void setImage1(String image1) {
            this.image1 = image1;
        }

        public String getCdn_img() {
            return cdn_img;
        }

        public void setCdn_img(String cdn_img) {
            this.cdn_img = cdn_img;
        }

        public String getIs_gif() {
            return is_gif;
        }

        public void setIs_gif(String is_gif) {
            this.is_gif = is_gif;
        }

        public String getGifFistFrame() {
            return gifFistFrame;
        }

        public void setGifFistFrame(String gifFistFrame) {
            this.gifFistFrame = gifFistFrame;
        }

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public int getT() {
            return t;
        }

        public void setT(int t) {
            this.t = t;
        }

        public String getDing() {
            return ding;
        }

        public void setDing(String ding) {
            this.ding = ding;
        }

        public int getFavourite() {
            return favourite;
        }

        public void setFavourite(int favourite) {
            this.favourite = favourite;
        }

        public Object getTop_cmt() {
            return top_cmt;
        }

        public void setTop_cmt(Object top_cmt) {
            this.top_cmt = top_cmt;
        }

        public Object getThemes() {
            return themes;
        }

        public void setThemes(Object themes) {
            this.themes = themes;
        }

        public String getPlaycount() {
            return playcount;
        }

        public void setPlaycount(String playcount) {
            this.playcount = playcount;
        }
    }


}
