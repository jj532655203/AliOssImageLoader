package com.fronttcapital.aliossimageloader.mvp.ui.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.blankj.utilcode.util.ToastUtils;
import com.fronttcapital.aliossimageloader.R;
import com.fronttcapital.aliossimageloader.adapter.SetHomeworkPageAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();

    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkWriteStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int checkNetPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
            //如果没有被授予
            if (checkWriteStoragePermission != PackageManager.PERMISSION_GRANTED || checkNetPermission != PackageManager.PERMISSION_GRANTED) {
                //请求权限,此处可以同时申请多个权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
            } else {
                setUpViewPager2();
            }
        } else {
            setUpViewPager2();
        }
    }

    private void setUpViewPager2() {
        ViewPager2 viewPager2 = findViewById(R.id.view_pager_2);
        int capacity = 34;
        List<String> adapterData = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; i++) {
            adapterData.add("book/2020/1/26/1582726292660.jpg");
            adapterData.add("book/2020/1/26/1582726292811.jpg");
            adapterData.add("book/2020/1/26/1582726292861.jpg");
            adapterData.add("book/2020/1/26/1582726292915.jpg");
            adapterData.add("book/2020/1/26/1582726292951.jpg");
            adapterData.add("book/2020/1/26/1582726292987.jpg");
            adapterData.add("book/2020/1/26/1582726293033.jpg");
            adapterData.add("book/2020/1/26/1582726293083.jpg");
            adapterData.add("book/2020/1/26/1582726293127.jpg");
            adapterData.add("book/2020/1/26/1582726293225.jpg");
            adapterData.add("book/2020/1/26/1582726293263.jpg");
            adapterData.add("book/2020/1/26/1582726293297.jpg");
            adapterData.add("book/2020/1/26/1582726293333.jpg");
            adapterData.add("book/2020/1/26/1582726293396.jpg");
            adapterData.add("book/2020/1/26/1582726293471.jpg");
            adapterData.add("book/2020/1/26/1582726293512.jpg");
            adapterData.add("book/2020/1/26/1582726293576.jpg");
            adapterData.add("book/2020/1/26/1582726293619.jpg");
            adapterData.add("book/2020/1/26/1582726293661.jpg");
            adapterData.add("book/2020/1/26/1582726293696.jpg");
            adapterData.add("book/2020/1/26/1582726293696.jpg");
            adapterData.add("book/2020/1/26/1582726293769.jpg");
            adapterData.add("book/2020/1/26/1582726293801.jpg");
            adapterData.add("book/2020/1/26/1582726293844.jpg");
            adapterData.add("book/2020/1/26/1582726293877.jpg");
            adapterData.add("book/2020/1/26/1582726293905.jpg");
            adapterData.add("book/2020/1/26/1582726293938.jpg");
            adapterData.add("book/2020/1/26/1582726293973.jpg");
            adapterData.add("book/2020/1/26/1582726294010.jpg");
            adapterData.add("book/2020/1/26/1582726294048.jpg");
            adapterData.add("book/2020/1/26/1582726294084.jpg");
            adapterData.add("book/2020/1/26/1582726294144.jpg");
            adapterData.add("book/2020/1/26/1582726294187.jpg");
        }
        viewPager2.setAdapter(new SetHomeworkPageAdapter(R.layout.layout_item_set_homework_page, adapterData));
        viewPager2.setOffscreenPageLimit(1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, final String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    checkPermission();

                } else {
                    ToastUtils.showShort("获取权限失败!");
                }
                break;
        }
    }

}
