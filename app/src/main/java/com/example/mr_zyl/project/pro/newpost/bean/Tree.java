package com.example.mr_zyl.project.pro.newpost.bean;

import java.util.List;

/**
 * Created by TFHR02 on 2017/9/14.
 */
public class Tree {
    private int id;
    private int pid;

    private int level;//当前的级别
    private Tree parent;//父节点
    private List<Tree> sons;

    private int resid;
    private String text;

    public Tree(){}
    public Tree(int id, int pid, int resid, String text) {
        this.id = id;
        this.pid = pid;
        this.resid = resid;
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

    public List<Tree> getSons() {
        return sons;
    }

    public void setSons(List<Tree> sons) {
        this.sons = sons;
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

    public int getResid() {
        return resid;
    }

    public void setResid(int resid) {
        this.resid = resid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
