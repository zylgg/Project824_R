package com.example.zylsmallvideolibrary;

/**
 * 设置播放器皮肤
 */
public class Skin {
    Skin(int titleColor, int timeColor, int seekDrawable, int bottomControlBackground, int enlargRecId, int shrinkRecId) {
        this.titleColor = titleColor;
        this.timeColor = timeColor;
        this.seekDrawable = seekDrawable;
        this.bottomControlBackground = bottomControlBackground;
        this.enlargRecId = enlargRecId;
        this.shrinkRecId = shrinkRecId;
    }

    int titleColor;
    int timeColor;
    int seekDrawable;
    int bottomControlBackground;
    int enlargRecId;
    int shrinkRecId;
}
