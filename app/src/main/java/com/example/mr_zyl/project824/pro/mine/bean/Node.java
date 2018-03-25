package com.example.mr_zyl.project824.pro.mine.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr_Zyl on 2016/8/25.
 */
public class Node {
    private int id;//当前节点的自身的id
    private int pId = 0;//根节点pId为0
    private String name;// 菜单的名字

    private int level;//当前的级别
    private boolean isExpand = false;//是否只展开一个只菜单
    private Node parent;//父节点
    private List<Node> children = new ArrayList<Node>();

    public Node() {
    }
    public Node(int id, int pId, String name) {
        this.id = id;
        this.pId = pId;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", pId=" + pId +
                ", name='" + name + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }
//////////////
    public int getLevel() {
        return parent == null ? 0 :parent.getLevel() + 1 ;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
        if (!isExpand){
            for (Node node : children) {
                node.setExpand(isExpand);
            }
        }
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }
}
