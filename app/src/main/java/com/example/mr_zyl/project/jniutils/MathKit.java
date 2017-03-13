package com.example.mr_zyl.project.jniutils;

/**
 * Created by Mr_Zyl on 2016/10/8.
 */
public class MathKit {
    public static native int square(int num);

    static {
        System.loadLibrary("mathkitso");
    }
}
