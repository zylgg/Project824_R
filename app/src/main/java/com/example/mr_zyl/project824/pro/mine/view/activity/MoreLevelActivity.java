package com.example.mr_zyl.project824.pro.mine.view.activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.mr_zyl.project824.MainActivity;
import com.example.mr_zyl.project824.R;
import com.example.mr_zyl.project824.pro.base.view.BaseActivity;
import com.example.mr_zyl.project824.pro.mine.bean.City;
import com.example.mr_zyl.project824.pro.mine.bean.Node;
import com.example.mr_zyl.project824.pro.mine.bean.Province;
import com.example.mr_zyl.project824.pro.mine.view.impl.XmlUtils;
import com.example.mr_zyl.project824.pro.mine.view.selfview.MoreLevelView;

import java.util.ArrayList;
import java.util.List;

public class MoreLevelActivity extends BaseActivity {
    public List<Province> Prov_lists = new ArrayList<Province>();//省市级连数据表
    public List<Node> mDatas = new ArrayList<Node>();
    public List<Node> mDatasA = new ArrayList<Node>();//第一级
    public List<Node> mDatasB = new ArrayList<Node>();//第二级
    public List<Node> mDatasC = new ArrayList<Node>();//第三级


    @Override
    protected int initLayoutId() {
        return R.layout.activity_more_level;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        InitData();
        initTestdata();
        MoreLevelView mlv = (MoreLevelView) findViewById(R.id.mlv);
//        mlv.setdata(mDatas,Prov_lists);
        mlv.setdata(mDatas,null);
        mlv.setonThreenViewCllickListenner(new MoreLevelView.itemonclickCallBack() {
            @Override
            public void setonItemClick(String address) {
                Intent intent=new Intent(MoreLevelActivity.this, MainActivity.class);
                intent.putExtra("address",address);
                setResult(112,intent);
                finish();
            }
        });
    }

    private void InitData() {
        try {
            Prov_lists = new XmlUtils().getxmlDATA(getAssets().open("Area.xml"));
            Node node = null;
            int AAA = 0, BBB = 0;

            for (int i = 0; i < Prov_lists.size(); i++) {
                AAA = mDatasA.size() + mDatasB.size() + mDatasC.size() + 1;
                Province pro = Prov_lists.get(i);
                node = new Node(AAA, 0, pro.getProvince());
                mDatasA.add(node);

                List<City> city_lists = pro.getCitylist();
                for (int j = 0; j < city_lists.size(); j++) {
                    BBB = mDatasA.size() + mDatasB.size() + mDatasC.size() + 1;
                    City city = city_lists.get(j);
                    node = new Node(BBB, AAA, city.getCityname());
                    mDatasB.add(node);
                    //建议点击父view时加载子view，(写法如下)
//                    List<Country> country_lists = city.getCountrylist();
//                    for (int k = 0;k < country_lists.size();k++) {
//                        Country country = country_lists.get(k);
//                        node = new Node(mDatasA.size()+ mDatasB.size()+mDatasC.size() + 1, BBB, country.getCountryname());
//                        mDatasC.add(node);
//                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mDatas.addAll(mDatasA);
        mDatas.addAll(mDatasB);
        mDatas.addAll(mDatasC);
    }

    private void initTestdata() {
        // id , pid , label , 其他属性
        mDatas.add(new Node(1, 0, "游戏"));
        mDatas.add(new Node(2, 0, "文档"));
        mDatas.add(new Node(3, 0, "程序"));
        mDatas.add(new Node(4, 0, "视频"));
        mDatas.add(new Node(5, 0, "音乐"));
        mDatas.add(new Node(6, 0, "照片"));
        mDatas.add(new Node(7, 0, "学习"));
        mDatas.add(new Node(8, 0, "娱乐"));
        mDatas.add(new Node(9, 0, "美食"));
        mDatas.add(new Node(10, 0, "备忘录"));

        mDatas.add(new Node(11, 1, "DOTA"));
        mDatas.add(new Node(14, 11, "剑圣"));
        mDatas.add(new Node(15, 11, "敌法"));
        mDatas.add(new Node(16, 11, "影魔"));
        mDatas.add(new Node(12, 1, "LOL"));
        mDatas.add(new Node(17, 12, "德玛西亚"));
        mDatas.add(new Node(18, 12, "潘森"));
        mDatas.add(new Node(19, 12, "蛮族之王"));
        mDatas.add(new Node(13, 1, "war3"));
        mDatas.add(new Node(20, 13, "人族"));
        mDatas.add(new Node(21, 13, "兽族"));
        mDatas.add(new Node(22, 13, "不死族"));

        mDatas.add(new Node(23, 2, "需求文档"));
        mDatas.add(new Node(24, 2, "原型设计"));
        mDatas.add(new Node(25, 2, "详细设计文档"));

        mDatas.add(new Node(26, 23, "需求调研"));
        mDatas.add(new Node(27, 23, "需求规格说明书"));
        mDatas.add(new Node(28, 23, "需求报告"));

        mDatas.add(new Node(29, 24, "QQ原型"));
        mDatas.add(new Node(30, 24, "微信原型"));

        mDatas.add(new Node(31, 25, "刀塔传奇详细设计"));
        mDatas.add(new Node(32, 25, "羽禾直播设计"));
        mDatas.add(new Node(33, 25, "YNedut设计"));
        mDatas.add(new Node(34, 25, "微信详细设计"));

        mDatas.add(new Node(35, 3, "面向对象"));
        mDatas.add(new Node(36, 3, "非面向对象"));

        mDatas.add(new Node(37, 35, "C++"));
        mDatas.add(new Node(38, 35, "JAVA"));
        mDatas.add(new Node(39, 36, "Javascript"));
        mDatas.add(new Node(40, 36, "C"));

        mDatas.add(new Node(41, 4, "电视剧"));
        mDatas.add(new Node(42, 4, "电影"));
        mDatas.add(new Node(43, 4, "综艺"));
        mDatas.add(new Node(44, 4, "动画"));

        mDatas.add(new Node(45, 41, "花千骨"));
        mDatas.add(new Node(46, 41, "三国演义"));
        mDatas.add(new Node(47, 41, "匆匆那年"));
        mDatas.add(new Node(48, 41, "亮剑"));

        mDatas.add(new Node(49, 42, "金刚狼"));
        mDatas.add(new Node(50, 42, "复仇者联盟"));
        mDatas.add(new Node(51, 42, "碟中谍"));
        mDatas.add(new Node(52, 42, "谍影重重"));

        mDatas.add(new Node(53, 43, "极限挑战"));
        mDatas.add(new Node(54, 43, "奔跑吧兄弟"));
        mDatas.add(new Node(55, 43, "我去上学啦"));
        mDatas.add(new Node(56, 43, "中国好声音"));

        mDatas.add(new Node(57, 44, "火影忍者"));
        mDatas.add(new Node(58, 44, "海贼王"));
        mDatas.add(new Node(59, 44, "哆啦A梦"));
        mDatas.add(new Node(60, 44, "蜡笔小新"));

    }
}
