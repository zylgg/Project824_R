package com.example.mr_zyl.project824.pro.newpost.bean;

/**
 * Created by TFHR02 on 2017/9/14.
 */
public class Tree {
    private int id;
    private int pid;

    private int level;//当前的级别
    private Tree parent;//父节点

    private String url;
    private String text;

    public Tree(){}
    public Tree(int id, int pid, String  url, String text) {
        this.id = id;
        this.pid = pid;
        this.url = url;
        this.text = text;
    }

    public Tree getParent() {
        return parent;
    }

    public void setParent(Tree parent) {
        this.parent = parent;
    }

    public int getLevel() {
        return parent == null ? 0 :parent.getLevel() + 1 ;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
