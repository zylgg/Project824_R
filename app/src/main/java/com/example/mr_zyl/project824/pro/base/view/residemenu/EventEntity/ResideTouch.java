package com.example.mr_zyl.project824.pro.base.view.residemenu.EventEntity;

/**
 * Created by TFHR02 on 2018/2/2.
 * 侧滑是否处于可滑动的边界 event回调实体
 */
public class ResideTouch {
    public static final String HandleTypeTagLeftBorder="LeftBorder";
    public static final String HandleTypeTagToggle="Toggle";

    private boolean is_Left=true;
    private String handleType;

    public ResideTouch() {
    }

    public ResideTouch(boolean is_Left, String handleType) {
        this.is_Left = is_Left;
        this.handleType = handleType;
    }

    public boolean is_Left() {
        return is_Left;
    }

    public void setIs_Left(boolean is_Left) {
        this.is_Left = is_Left;
    }

    public String getHandleType() {
        return handleType;
    }

    public void setHandleType(String handleType) {
        this.handleType = handleType;
    }
}
