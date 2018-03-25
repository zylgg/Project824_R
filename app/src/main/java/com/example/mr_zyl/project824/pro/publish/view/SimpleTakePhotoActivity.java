package com.example.mr_zyl.project824.pro.publish.view;

import android.content.Intent;
import android.media.ExifInterface;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mr_zyl.project824.R;
import com.example.mr_zyl.project824.pro.base.view.BaseActivity;
import com.lqr.imagepicker.bean.ImageItem;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;

public class SimpleTakePhotoActivity extends BaseActivity {

    @BindView(R.id.iv_showphoto)
    ImageView iv_showphoto;
    @BindView(R.id.tv_ImgExif)
    TextView tv_ImgExif;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_simple_take_photo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        ArrayList<ImageItem> imagepaths = (ArrayList<ImageItem>) intent.getSerializableExtra("imagepaths");
        String path = imagepaths.get(0).path;
        ImageLoader.getInstance().displayImage("file://"+path,iv_showphoto);

        try {
            StringBuffer stringBuffer=new StringBuffer();
            ExifInterface exifInterface=new ExifInterface(path);
            String orientation = exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION);//旋转角度，整形表示，在ExifInterface中有常量对应表示
            String dateTime = exifInterface.getAttribute(ExifInterface.TAG_DATETIME);//拍摄时间，取决于设备设置的时间
            String make = exifInterface.getAttribute(ExifInterface.TAG_MAKE);//设备品牌
            String model = exifInterface.getAttribute(ExifInterface.TAG_MODEL);//设备型号，整形表示，在ExifInterface中有常量对应表示
            String flash = exifInterface.getAttribute(ExifInterface.TAG_FLASH);//闪光灯

            String imageLength = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH);//图片长度
            String imageWidth = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH);//图片宽度
            String latitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE); //纬度
            String longitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE); //经度
            String latitudeRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);//纬度名（N or S）

            String longitudeRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF); //经度名（E or W）
            String exposureTime = exifInterface.getAttribute(ExifInterface.TAG_EXPOSURE_TIME);//曝光时间
            String aperture = exifInterface.getAttribute(ExifInterface.TAG_APERTURE); //光圈值
            String isoSpeedRatings = exifInterface.getAttribute(ExifInterface.TAG_ISO); //ISO感光度
            String dateTimeDigitized = exifInterface.getAttribute(ExifInterface.TAG_DATETIME_DIGITIZED);//数字化时间

            String subSecTime = exifInterface.getAttribute(ExifInterface.TAG_SUBSEC_TIME);
            String subSecTimeOrig = exifInterface.getAttribute(ExifInterface.TAG_SUBSEC_TIME_ORIG);
            String subSecTimeDig = exifInterface.getAttribute(ExifInterface.TAG_SUBSEC_TIME_DIG);

            String altitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_ALTITUDE);//海拔高度
            String altitudeRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_ALTITUDE_REF);//海拔高度
            String gpsTimeStamp = exifInterface.getAttribute(ExifInterface.TAG_GPS_TIMESTAMP);//时间戳
            String gpsDateStamp = exifInterface.getAttribute(ExifInterface.TAG_GPS_DATESTAMP);//日期戳
            String whiteBalance = exifInterface.getAttribute(ExifInterface.TAG_WHITE_BALANCE);//白平衡
            String focalLength = exifInterface.getAttribute(ExifInterface.TAG_FOCAL_LENGTH); //焦距
            String processingMethod = exifInterface.getAttribute(ExifInterface.TAG_GPS_PROCESSING_METHOD); //用于定位查找的全球定位系统处理方法。
            stringBuffer.append("旋转角度").append("：").append(orientation).append("\n");
            stringBuffer.append("拍摄时间").append("：").append(dateTime).append("\n");
            stringBuffer.append("设备品牌").append("：").append(make).append("\n");
            stringBuffer.append("设备型号").append("：").append(model).append("\n");
            stringBuffer.append("闪光灯").append("：").append(flash).append("\n");

            stringBuffer.append("图片长度").append("：").append(imageLength).append("\n");
            stringBuffer.append("图片宽度").append("：").append(imageWidth).append("\n");
            stringBuffer.append("纬度").append("：").append(latitude).append("\n");
            stringBuffer.append("经度").append("：").append(longitude).append("\n");
            stringBuffer.append("纬度名").append("：").append(latitudeRef).append("\n");

            stringBuffer.append("经度名").append("：").append(longitudeRef).append("\n");
            stringBuffer.append("曝光时间").append("：").append(exposureTime).append("\n");
            stringBuffer.append("光圈值").append("：").append(aperture).append("\n");
            stringBuffer.append("ISO感光度").append("：").append(isoSpeedRatings).append("\n");
            stringBuffer.append("数字化时间").append("：").append(dateTimeDigitized).append("\n");

            stringBuffer.append("海拔高度").append("：").append(altitude).append("\n");
            stringBuffer.append("海拔高度").append("：").append(altitudeRef).append("\n");
            stringBuffer.append("时间戳").append("：").append(gpsTimeStamp).append("\n");
            stringBuffer.append("日期戳").append("：").append(gpsDateStamp).append("\n");
            stringBuffer.append("白平衡").append("：").append(whiteBalance).append("\n");
            stringBuffer.append("焦距").append("：").append(focalLength).append("\n");
            stringBuffer.append("用于定位查找的全球定位系统处理方法").append("：").append(processingMethod).append("\n");

            tv_ImgExif.setText("数码照片拍摄信息：\n"+stringBuffer.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
