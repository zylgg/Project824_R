package com.example.mr_zyl.project.pro.essence;

/**
 * Created by TFHR02 on 2017/9/21.
 */
public class refreshEvent {
    /**
     * 因为在AppbarLayout没有完全展开时，不能刷新。
     */
    private boolean isCan;
    /**
     * 点击tab菜单，刷新当前分类的数据
     */
    private boolean is_RefreshCurrent;

    public boolean isCan() {
        return isCan;
    }

    public void setCan(boolean can) {
        isCan = can;
    }

    public boolean is_RefreshCurrent() {
        return is_RefreshCurrent;
    }

    public void setIs_RefreshCurrent(boolean is_RefreshCurrent) {
        this.is_RefreshCurrent = is_RefreshCurrent;
    }
}
