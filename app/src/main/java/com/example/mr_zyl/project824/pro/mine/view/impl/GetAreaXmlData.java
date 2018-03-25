package com.example.mr_zyl.project824.pro.mine.view.impl;

import com.example.mr_zyl.project824.pro.mine.bean.Province;

import org.xmlpull.v1.XmlPullParserException;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Mr_Zyl on 2016/8/26.
 */
public interface GetAreaXmlData {
    public List<Province> getxmlDATA(InputStream is) throws XmlPullParserException, Exception;

}
