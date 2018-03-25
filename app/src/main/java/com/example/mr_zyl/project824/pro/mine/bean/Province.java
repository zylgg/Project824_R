package com.example.mr_zyl.project824.pro.mine.bean;

import java.util.List;

public class Province {
    private String province;
    private List<City> citylist;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public List<City> getCitylist() {
        return citylist;
    }

    public void setCitylist(List<City> citylist) {
        this.citylist = citylist;
    }

    @Override
    public String toString() {
        return "Province [province=" + province + ", citylist=" + citylist + "]";
    }

}
