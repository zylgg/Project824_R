package com.example.mr_zyl.project824.pro.attention.bean;

/**
 * Created by TFHR02 on 2018/1/31.
 */
public class attention {
    private String theme_id;
    private String theme_name;
    private String image_list;
    private String sub_number;
    private int is_sub;
    private int is_default;
    private boolean is_onClicked;

    public boolean isIs_onClicked() {
        return is_onClicked;
    }

    public void setIs_onClicked(boolean is_onClicked) {
        this.is_onClicked = is_onClicked;
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

    public String getImage_list() {
        return image_list;
    }

    public void setImage_list(String image_list) {
        this.image_list = image_list;
    }

    public String getSub_number() {
        return sub_number;
    }

    public void setSub_number(String sub_number) {
        this.sub_number = sub_number;
    }

    public int getIs_sub() {
        return is_sub;
    }

    public void setIs_sub(int is_sub) {
        this.is_sub = is_sub;
    }

    public int getIs_default() {
        return is_default;
    }

    public void setIs_default(int is_default) {
        this.is_default = is_default;
    }
}
