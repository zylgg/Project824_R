package com.example.mr_zyl.project824.bean;

/**
 * Created by TFHR02 on 2017/9/14.
 */
public class BuilderItemEntity {
    public int left_resid;
    public String text;
    public Class classname;
    public int requestcode;

    public BuilderItemEntity(int left_resid, String text, Class classname, int requestcode) {
        this.left_resid = left_resid;
        this.text = text;
        this.classname = classname;
        this.requestcode = requestcode;
    }
}
