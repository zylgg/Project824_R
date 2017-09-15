package com.example.mr_zyl.project.pro.newpost.view.Activity;

import android.os.Bundle;

import com.example.mr_zyl.project.R;
import com.example.mr_zyl.project.pro.base.view.BaseActivity;
import com.example.mr_zyl.project.pro.newpost.bean.Tree;
import com.example.mr_zyl.project.pro.newpost.view.selfview.TreeGroup;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;

public class TreeViewActivity extends BaseActivity {

    @BindView(R.id.tg_treeview)
    TreeGroup tg_treeview;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_tree_view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinkedList<Tree> TreeDatas = new LinkedList<>();
        TreeDatas.add(new Tree(0, -1, -2, "总站"));

        TreeDatas.add(new Tree(1, 0, -2, "电表1"));
        TreeDatas.add(new Tree(2, 0, -2, "电表2"));
        TreeDatas.add(new Tree(3, 0, -2, "电表3"));

        TreeDatas.add(new Tree(4, 1, -2, "空调"));
        TreeDatas.add(new Tree(5, 2, -2, "电视"));
        TreeDatas.add(new Tree(6, 2, -2, "插座a"));
        TreeDatas.add(new Tree(7, 3, -2, "插座b"));
        TreeDatas.add(new Tree(8, 3, -2, "电视机"));

        TreeDatas.add(new Tree(9, 6, -2, "a-洗衣机"));
        TreeDatas.add(new Tree(10, 6, -2, "b-吹风机"));

        //给每个tree设置父元素
        for (int i = 0; i < TreeDatas.size(); i++) {
            Tree tree = TreeDatas.get(i);
            int pid = tree.getPid();
            if (pid != -1) {

                for (int j = 0; j < TreeDatas.size(); j++) {
                    Tree tree2 = TreeDatas.get(j);
                    int id = tree2.getId();
                    if (id == pid) {
                        tree.setParent(tree2);

                        List<Tree> sons = tree2.getSons();
                        if (sons==null){
                            sons=new ArrayList<>();
                        }
                        sons.add(tree);

                        tree2.setSons(sons);
                    }
                }
            }
        }
        tg_treeview.setData(TreeDatas);
    }


}
