package com.example.mr_zyl.project824.pro.attention.model;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.mr_zyl.project824.http.impl.RequestParam;
import com.example.mr_zyl.project824.http.impl.SystemHttpCommand;
import com.example.mr_zyl.project824.http.utils.HttpTask;
import com.example.mr_zyl.project824.http.utils.HttpUtils;
import com.example.mr_zyl.project824.pro.base.model.BaseModel;
import com.example.mr_zyl.project824.utils.VolleyUtils;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;

/**
 * Created by TFHR02 on 2018/2/1.
 */
public class AttentionListModel extends BaseModel {

    private static final String TAG = "AttentionListModel";

    public AttentionListModel(Context context) {
        super(context);
    }

    private String getUrl() {
        return getServerUrl().concat("/api/api_open.php");
    }

    private VolleyUtils.Callback mCallback;

    public void getAttentionList(String requestTag, JSONObject params, final VolleyUtils.Callback callback) {
        this.mCallback = callback;
//        final Response.Listener listener = new Response.Listener<String>() {
//            @Override
//            public void onResponse(String result) {
//                callback.onSuccess(result);
//            }
//        };
//        final Response.ErrorListener errorListener = new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                callback.onError("网络异常！");
//            }
//        };
//        VolleyUtils.getQueue(getContext()).cancelAll(requestTag);
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, getUrl(), params, listener, errorListener) {
//            @Override
//            protected Response parseNetworkResponse(NetworkResponse response) {
//                try {
//                    String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
//                    Log.i(TAG, "parseNetworkResponse: "+json);
//
//                    return Response.success(json, HttpHeaderParser.parseCacheHeaders(response));
//                } catch (UnsupportedEncodingException e) {
//                    return Response.error(new ParseError(e));
//                } catch (JsonSyntaxException e) {
//                    return Response.error(new ParseError(e));
//                }
//            }
//        };
//        request.setTag(requestTag);
//        VolleyUtils.getQueue(getContext()).add(request);
        RequestParam mParams = new RequestParam();
        try {
            Iterator<String> keys = params.keys();
            while (keys.hasNext()) {
                String key = keys.next().toString();
                String value = params.getString(key);
                mParams.put(key,value);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //发送请求
        HttpTask task = new HttpTask(getUrl(), mParams, new SystemHttpCommand(), listener);
        task.execute();

    }

    private HttpUtils.OnHttpResultListener listener = new HttpUtils.OnHttpResultListener() {
        @Override
        public void onResult(String result) {
            if (TextUtils.isEmpty(result)){
                mCallback.onError("网络异常!");
            }else{
                mCallback.onSuccess(result);
            }
        }
    };
}
