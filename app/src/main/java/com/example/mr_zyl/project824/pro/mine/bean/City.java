package com.example.mr_zyl.project824.pro.mine.bean;

import java.util.List;

public class City {
    private String cityname;
    private List<Country> countrylist;

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public List<Country> getCountrylist() {
        return countrylist;
    }

    public void setCountrylist(List<Country> countrylist) {
        this.countrylist = countrylist;
    }

    @Override
    public String toString() {
        return "City [cityname=" + cityname + ", countrylist=" + countrylist + "]";
    }

}
