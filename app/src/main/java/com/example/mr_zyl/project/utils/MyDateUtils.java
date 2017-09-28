package com.example.mr_zyl.project.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Dream on 16/5/28.
 */
public class MyDateUtils {

    public static String parseDate(String createTime) {
        try {
            String ret = "";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long create = sdf.parse(createTime).getTime();
            Calendar now = Calendar.getInstance();
            long ms = 1000 * (now.get(Calendar.HOUR_OF_DAY) * 3600 + now.get(Calendar.MINUTE) * 60 + now.get(Calendar.SECOND));//毫秒数
            long ms_now = now.getTimeInMillis();
            if (ms_now - create < ms) {
                ret = "今天 " + createTime.substring(createTime.indexOf(" ") + 1);
            } else if (ms_now - create < (ms + 24 * 3600 * 1000)) {
                ret = "昨天 " + createTime;
            } else if (ms_now - create < (ms + 24 * 3600 * 1000 * 2)) {
                ret = "前天 " + createTime;
            } else {
                ret = "更早 " + createTime;
            }
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 秒速转换成 分，小时，天
     * @param mss
     * @return
     */
    public static String formatDateTime(long mss) {
        String DateTimes = null;
        long days = mss / (60 * 60 * 24);
        long hours = (mss % (60 * 60 * 24)) / (60 * 60);
        long minutes = (mss % (60 * 60)) / 60;
        long seconds = mss % 60;
        if (days > 0) {
            DateTimes = days + "天" + hours + "小时" + minutes + "分钟"
                    + seconds + "秒";
        } else if (hours > 0) {
            DateTimes = hours + "小时" + minutes + "分钟"
                    + seconds + "秒";
        } else if (minutes > 0) {
            DateTimes = minutes + "分钟"
                    + seconds + "秒";
        } else {
            DateTimes = seconds + "秒";
        }

        return DateTimes;
    }

}
