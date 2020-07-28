package com.example.mr_zyl.project824.http.impl;



import com.example.mr_zyl.project824.http.IHttpCommand;
import com.example.mr_zyl.project824.http.IRequestParam;
import com.example.mr_zyl.project824.http.utils.HttpUtils;

import java.util.HashMap;

public class SystemHttpCommand implements IHttpCommand<HashMap<String,Object>> {

    @Override
    public String execute(String url, IRequestParam<HashMap<String, Object>> requestParam) {
        try {
            return HttpUtils.get(url,requestParam.getRequestParam());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
