package com.example.mr_zyl.project824.pro.mine.view.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.mr_zyl.project824.R;
import com.example.mr_zyl.project824.pro.base.view.BaseActivity;
import com.example.mr_zyl.project824.utils.BitmapUtil;
import com.example.mr_zyl.project824.utils.DisplayUtil;
import com.example.mr_zyl.project824.utils.ToastUtil;
import com.google.zxing.WriterException;

public class QRImageActivity extends BaseActivity {

    private EditText et_qrimage_text;
    private Button b_arimage_makear;
    private ImageView iv_arimage_qrpic;
    private Context context;
    private Bitmap qrbitmap;


    @Override
    protected int initLayoutId() {
        return R.layout.activity_qrimage;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;

        et_qrimage_text = (EditText) findViewById(R.id.et_qrimage_text);
        b_arimage_makear = (Button) findViewById(R.id.b_arimage_makear);
        iv_arimage_qrpic = (ImageView) findViewById(R.id.iv_arimage_qrpic);

        b_arimage_makear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = et_qrimage_text.getText().toString();
                if (TextUtils.isEmpty(text)) {
                    ToastUtil.showToast(context, "文本为空！");
                    return;
                }
                try {
                    //普通二维码
//                    qrbitmap= BitmapUtil.makeQRImage(text, DisplayUtil.dip2px(context,250),DisplayUtil.dip2px(context,250));

                    //加色彩二维码
                    qrbitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                    qrbitmap = BitmapUtil.makeQRImage(qrbitmap, text, DisplayUtil.dip2px(context, 250), DisplayUtil.dip2px(context, 250));

                    //加背景
//                    Bitmap bgbitmap=BitmapFactory.decodeResource(getResources(),R.drawable.f);
//                    qrbitmap=BitmapUtil.addBackground(qrbitmap,bgbitmap);
                    //加水印
                    Bitmap waterbitmap = BitmapFactory.decodeResource(getResources(), R.drawable.button_weixin_press);
                    qrbitmap = BitmapUtil.composeWatermark(qrbitmap, waterbitmap);

                    if (qrbitmap != null) {
                        iv_arimage_qrpic.setImageBitmap(qrbitmap);
                    }
//                    qrbitmap.recycle();
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
