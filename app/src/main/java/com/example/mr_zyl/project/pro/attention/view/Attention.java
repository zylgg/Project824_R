package com.example.mr_zyl.project.pro.attention.view;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.mr_zyl.project.R;
import com.example.mr_zyl.project.pro.attention.adapter.attentionListAdapter;
import com.example.mr_zyl.project.pro.attention.bean.attention;
import com.example.mr_zyl.project.pro.base.view.BaseFragment;
import com.example.mr_zyl.project.pro.newpost.natvigation.NewpostNavigationBuilder;
import com.example.mr_zyl.project.utils.VolleyUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mr_Zyl on 2016/8/25.
 */
public class Attention extends BaseFragment {
    private static final String TAG = "Attention";
    private String listRequestTAG = "listRequestTAG";
    @BindView(R.id.lv_attention)
    ListView lv_attention;
    ArrayList<attention> attentions=new ArrayList<>();

    @Override
    public int getContentView() {
        return R.layout.attention;
    }

    @Override
    public void initContentView(View viewContent) {
        ButterKnife.bind(this, viewContent);
        initToolBar(viewContent);
        lv_attention.setAdapter(new attentionListAdapter(Fcontext,attentions));
        requestData();
    }

    private void initToolBar(View viewContent) {
        NewpostNavigationBuilder builder = new NewpostNavigationBuilder(Fcontext);
        builder.setTitle("关注")
                .setBackground(R.drawable.toolbar_background_attention_shape)
                .createAndBind((ViewGroup) viewContent);
    }

    private void requestData() {
        JSONObject params = new JSONObject();
        try {
            params.put("a", "tag_recommend");
            params.put("action", "sub");
            params.put("c", "topic");
            params.put("type", "1");//(0,1)
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = "http://api.budejie.com/api/api_open.php";
        final Response.Listener listener = new Response.Listener<ArrayList>() {
            @Override
            public void onResponse(ArrayList list) {
                if (list==null){
                    showToast("没数据！");
                    return;
                }
                Log.i(TAG, "onResponse:" + list.size());
                attentions.clear();
                attentions.addAll(list);
                ((attentionListAdapter)lv_attention.getAdapter()).notifyDataSetChanged();
            }
        };
        final Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onError:" + error.getMessage());
            }
        };
        VolleyUtils.getQueue(Fcontext).cancelAll(listRequestTAG);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, params, listener, errorListener) {
            @Override
            protected Response parseNetworkResponse(NetworkResponse response) {
                try {
                    String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

                    List<attention> o = new Gson().fromJson(json, new TypeToken<List<attention>>() {
                    }.getType());

                    return Response.success(o, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JsonSyntaxException e) {
                    return Response.error(new ParseError(e));
                }
            }
        };
        request.setTag(listRequestTAG);
        RequestQueue queue = VolleyUtils.getQueue(Fcontext);
        queue.add(request);
    }


}
