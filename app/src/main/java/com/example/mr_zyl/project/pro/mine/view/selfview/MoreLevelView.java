package com.example.mr_zyl.project.pro.mine.view.selfview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.mr_zyl.project.R;
import com.example.mr_zyl.project.pro.mine.bean.City;
import com.example.mr_zyl.project.pro.mine.bean.Country;
import com.example.mr_zyl.project.pro.mine.bean.Node;
import com.example.mr_zyl.project.pro.mine.bean.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr_Zyl on 2016/8/25.
 */
public class MoreLevelView extends LinearLayout {

    List<Node> mDatas;
    List<Province> ProLists;
    private Context mContext;
    LayoutParams rootLayoutParams, itemLayoutParams, dividerLayoutParams, itemLayoutParams_80;
    Drawable drawable_open, drawable_close;

    public MoreLevelView(Context context) {
        super(context);
        mContext = context;
    }
    public MoreLevelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public void setdata(List<Node> lists) {
        setdata(lists, null);
    }

    public void setdata(List<Node> lists, List<Province> ProLists) {
        this.mDatas = lists;
        this.ProLists = ProLists;
        drawable_open = getResources().getDrawable(R.drawable.open);
        drawable_open.setBounds(0, 0, drawable_open.getMinimumWidth(), drawable_open.getMinimumHeight());
        drawable_close = getResources().getDrawable(R.drawable.close);
        drawable_close.setBounds(0, 0, drawable_close.getMinimumWidth(), drawable_close.getMinimumHeight());

        rootLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        itemLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        dividerLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, 1);
        itemLayoutParams_80 = new LayoutParams(LayoutParams.MATCH_PARENT, drawable_close.getMinimumHeight());

        addView(initview());
    }

    private View initview() {
        ScrollView Sview = new ScrollView(mContext);
        Sview.setLayoutParams(rootLayoutParams);
        Sview.addView(addchildview(null, 0));
        return Sview;
    }

    //处理点击第三级菜单回调数据
    public interface itemonclickCallBack {
        void setonItemClick(String address);
    }

    private itemonclickCallBack listenner;

    public void setonThreenViewCllickListenner(itemonclickCallBack listenner) {
        this.listenner = listenner;
    }

    private View addchildview(LinearLayout ll_son, final int id) {
        LinearLayout LL_root = null;
        if (ll_son == null) {
            LL_root = new LinearLayout(mContext);
        } else {
            LL_root = ll_son;
        }
        LL_root.setLayoutParams(rootLayoutParams);
        LL_root.setOrientation(LinearLayout.VERTICAL);
        List<Node> lists_0 = getListByPid(id);//*********id如果为二级节点的id，怎么得到子view的数据?????????
        for (int i = 0; i < lists_0.size(); i++) {
            final Node nodes = lists_0.get(i);
            nodes.setParent(getNodeByid(id));
            //添加一个TextView用于存放菜单item内容
            final TextView tv_start = new TextView(mContext);
            tv_start.setLayoutParams(itemLayoutParams_80);
            tv_start.setId(nodes.getId());
            tv_start.setText(nodes.getName());
            tv_start.setTextColor(new Color().BLACK);
            tv_start.setPadding(60 * nodes.getLevel(), 0, 0, 0);
            tv_start.setCompoundDrawables(drawable_close, null, null, null);
            tv_start.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            tv_start.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    LinearLayout ll_son = (LinearLayout) v.getTag();
                    //如果点击的是第三级view---直接返回
                    if (getListByPid(nodes.getId()).size() == 0) {//没有子元素，就回调
                        //响应回调监听
                        listenner.setonItemClick(GetAllNodeName(nodes));
                        return;
                    }
                    //如果下级菜单没子菜单，则去添加生成
                    if (ll_son.getChildCount() == 0) {
                        tv_start.setCompoundDrawables(drawable_open, null, null, null);
                        addchildview(ll_son, v.getId());
                    } else if (ll_son.isShown()) {//如果子view显示了就隐藏
                        tv_start.setCompoundDrawables(drawable_close, null, null, null);
                        ll_son.setVisibility(View.GONE);
                    } else {//如果子view没显示就显示
                        tv_start.setCompoundDrawables(drawable_open, null, null, null);
                        ll_son.setVisibility(View.VISIBLE);
                    }
                }
            });
            LL_root.addView(tv_start);

            //添加分割线
            LL_root.addView(add_divder());
            //添加一个线性布局用于存放在下一级菜单
            LinearLayout ll_end = new LinearLayout(mContext);
            ll_end.setLayoutParams(itemLayoutParams);
            ll_end.setOrientation(LinearLayout.VERTICAL);
            ll_end.setGravity(View.GONE);
            tv_start.setTag(ll_end);//菜单的标记为子view

            LL_root.addView(ll_end);
        }
        return LL_root;
    }

    private StringBuffer sb = new StringBuffer();//拼接返回值

    private String GetAllNodeName(Node nodes) {
        sb.append(nodes.getName()+"-");
        if (nodes.getLevel() != 0) {//已经是顶层了
            GetAllNodeName(nodes.getParent());
        }
        return sb.toString().substring(0,sb.length()-1);
    }

    private View add_divder() {
        TextView tview = new TextView(mContext);
        tview.setLayoutParams(dividerLayoutParams);
        tview.setBackgroundColor(new Color().GRAY);
        return tview;
    }

    /**
     * 根据pid得到对象的菜单集合（获取子元素）
     *
     * @param id
     * @return
     */
    private List<Node> getListByPid(int id) {
        List<Node> resultNodes = new ArrayList<Node>();
        for (int i = 0; i < mDatas.size(); i++) {
            Node node = mDatas.get(i);
            //如果有node的pid等于点击的id，则此node为点击的对象的子view数据
            if (node.getpId() == id) {
                resultNodes.add(node);
            }
        }
        if (resultNodes.size() == 0 && ProLists != null) {
            return GetThirdViewData(id);
        }
        return resultNodes;
    }

    private List<Node> GetThirdViewData(int pid) {//涉及到省市级连的时候
        //加载第三级view
        List<Node> resultNodes = new ArrayList<Node>();
        Node node = getNodeByid(pid);
        String nodename = node.getName();
        Node parentnode = node.getParent();
        if (parentnode != null) {
            String parentnodename = parentnode.getName();
            for (Province p : ProLists) {
                if (p.getProvince().equals(parentnodename)) {//判断第一级view的名字
                    List<City> citylists = p.getCitylist();
                    for (City c : citylists) {
                        if (c.getCityname().equals(nodename)) {//判断当前view的名字
                            List<Country> countrylists = c.getCountrylist();
                            for (int i = 0; i < countrylists.size(); i++) {
                                resultNodes.add(new Node(i, pid, countrylists.get(i).getCountryname()));
                            }
                        }
                    }
                }
            }
        }
        return resultNodes;
    }

    /**
     * 根据id得到对象
     *
     * @param id
     * @return
     */
    private Node getNodeByid(int id) {
        Node resultNodes = null;
        for (int i = 0; i < mDatas.size(); i++) {
            Node node = mDatas.get(i);
            if (node.getId() == id) {
                resultNodes = node;
            }
        }
        return resultNodes;
    }

}
