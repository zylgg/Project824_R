package com.example.mr_zyl.project824.pro.essence.dataservice;

public class NowWeatherBean {

    private String success;
     public Result result;

    public class Result {
        private String weaid;
        private String days;
        private String week;
        private String cityno;
        private String citynm;
        private String cityid;
        private String temperature;
        private String temperature_curr;
        private String humidity;
        private String aqi;
        private String weather;
        private String weather_curr;
        private String weather_icon;
        private String weather_icon1;
        private String wind;
        private String winp;
        private String temp_high;
        private String temp_low;
        private String temp_curr;
        private String humi_high;
        private String humi_low;
        private String weatid;
        private String weatid1;
        private String windid;
        private String winpid;
        private String weather_iconid;

        @Override
        public String toString() {
            return "Result{" +
                    "weaid='" + weaid + '\'' +
                    ", days='" + days + '\'' +
                    ", week='" + week + '\'' +
                    ", cityno='" + cityno + '\'' +
                    ", citynm='" + citynm + '\'' +
                    ", cityid='" + cityid + '\'' +
                    ", temperature='" + temperature + '\'' +
                    ", temperature_curr='" + temperature_curr + '\'' +
                    ", humidity='" + humidity + '\'' +
                    ", aqi='" + aqi + '\'' +
                    ", weather='" + weather + '\'' +
                    ", weather_curr='" + weather_curr + '\'' +
                    ", weather_icon='" + weather_icon + '\'' +
                    ", weather_icon1='" + weather_icon1 + '\'' +
                    ", wind='" + wind + '\'' +
                    ", winp='" + winp + '\'' +
                    ", temp_high='" + temp_high + '\'' +
                    ", temp_low='" + temp_low + '\'' +
                    ", temp_curr='" + temp_curr + '\'' +
                    ", humi_high='" + humi_high + '\'' +
                    ", humi_low='" + humi_low + '\'' +
                    ", weatid='" + weatid + '\'' +
                    ", weatid1='" + weatid1 + '\'' +
                    ", windid='" + windid + '\'' +
                    ", winpid='" + winpid + '\'' +
                    ", weather_iconid='" + weather_iconid + '\'' +
                    '}';
        }
    }
}
