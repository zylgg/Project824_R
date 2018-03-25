package com.example.mr_zyl.project824.pro.mine.view.SlidingLib;

/**
 * Created by TFHR02 on 2017/10/11.
 */
public class listviewbean {
    private String name;
    private String status;

    public listviewbean(String name, String status) {
        this.name = name;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

