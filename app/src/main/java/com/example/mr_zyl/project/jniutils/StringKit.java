package com.example.mr_zyl.project.jniutils;

/**
 * Created by Mr_Zyl on 2016/10/8.
 */
public class StringKit {
    public static native void setNull(String str);
    static {
        System.loadLibrary("JniDemo");
    }
}
