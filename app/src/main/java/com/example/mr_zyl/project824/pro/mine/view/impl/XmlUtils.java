package com.example.mr_zyl.project824.pro.mine.view.impl;

import android.util.Xml;

import com.example.mr_zyl.project824.pro.mine.bean.City;
import com.example.mr_zyl.project824.pro.mine.bean.Country;
import com.example.mr_zyl.project824.pro.mine.bean.Province;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr_Zyl on 2016/8/26.
 */
public class XmlUtils implements GetAreaXmlData {

    @Override
    public  List<Province> getxmlDATA(InputStream is) throws Exception {
        List<Province> list = new ArrayList<Province>();
        List<City> citylist = null;
        List<Country> countrylist = null;
        Province p = null;
        City city = null;
        Country country = null;

        XmlPullParser xpp = Xml.newPullParser();
        xpp.setInput(is, "UTF-8");
        int eventtype = xpp.getEventType();
        while (eventtype != XmlPullParser.END_DOCUMENT) {
            String parsename = xpp.getName();
            switch (eventtype) {
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    if (parsename.equals("province")) {
                        p = new Province();
                        p.setProvince(xpp.getAttributeValue(null, "name"));
                        list.add(p);
                        citylist = new ArrayList<City>();

                    } else if (parsename.equals("city")) {
                        city=new City();
                        city.setCityname(xpp.getAttributeValue(null, "name"));
                        citylist.add(city);
                        p.setCitylist(citylist);
                        countrylist=new ArrayList<Country>();
                    } else if (parsename.equals("country")) {
                        country=new Country();
                        country.setCountryname(xpp.getAttributeValue(null, "name"));
                        countrylist.add(country);
                        city.setCountrylist(countrylist);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    break;
            }
            eventtype = xpp.next();

        }
        return list;
    }
}
