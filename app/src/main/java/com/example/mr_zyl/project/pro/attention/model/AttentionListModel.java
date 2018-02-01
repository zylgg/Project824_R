package com.example.mr_zyl.project.pro.attention.model;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.mr_zyl.project.pro.base.model.BaseModel;
import com.example.mr_zyl.project.utils.VolleyUtils;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

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

    public void getAttentionList(String requestTag, JSONObject params, final VolleyUtils.Callback callback) {

        final Response.Listener listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                callback.onSuccess(result);
            }
        };
        final Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError("网络异常！");
            }
        };
        VolleyUtils.getQueue(getContext()).cancelAll(requestTag);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, getUrl(), params, listener, errorListener) {
            @Override
            protected Response parseNetworkResponse(NetworkResponse response) {
                try {
                    String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    Log.i(TAG, "parseNetworkResponse: "+json);

                    return Response.success(json, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JsonSyntaxException e) {
                    return Response.error(new ParseError(e));
                }
            }
        };
        request.setTag(requestTag);
        VolleyUtils.getQueue(getContext()).add(request);
    }
}
