package com.fronttcapital.aliossimageloader;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.blankj.utilcode.util.ScreenUtils;
import com.fronttcapital.imageloader.AliOssImageLoader;

public class SetHomeworkPageView extends ScrollView {

    private static final String TAG = "SetHomeworkPageView";
    private ImageView bgImgView;

    public SetHomeworkPageView(Context context) {
        this(context, null);
    }

    public SetHomeworkPageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SetHomeworkPageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_view_set_homework_page, this);
        bgImgView = findViewById(R.id.bg_img);
    }

    public void refreshView(String ossImgPath) {
        AliOssImageLoader.loadImageView(ossImgPath, bgImgView, Math.min(ScreenUtils.getScreenHeight(), ScreenUtils.getScreenWidth()));
    }


}
