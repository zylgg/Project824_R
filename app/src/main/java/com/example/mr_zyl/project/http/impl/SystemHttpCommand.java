package com.example.mr_zyl.project.http.impl;



import com.example.mr_zyl.project.http.IHttpCommand;
import com.example.mr_zyl.project.http.IRequestParam;
import com.example.mr_zyl.project.http.utils.HttpUtils;

import java.util.HashMap;

public class SystemHttpCommand implements IHttpCommand<HashMap<String,Object>> {

    @Override
    public String execute(String url, IRequestParam<HashMap<String, Object>> requestParam) {
        try {
            return HttpUtils.post(url,requestParam.getRequestParam());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
