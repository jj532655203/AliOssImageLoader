package com.fronttcapital.aliossimageloader.mvp.presenter;

import android.util.Log;

import com.alibaba.sdk.android.oss.common.auth.OSSFederationToken;
import com.blankj.utilcode.util.Utils;
import com.fronttcapital.aliossimageloader.Config;
import com.fronttcapital.aliossimageloader.bean.APadResponse;
import com.fronttcapital.aliossimageloader.bean.RequestOssTokenBean;
import com.fronttcapital.aliossimageloader.mvp.contract.IMainActivityContract;
import com.fronttcapital.aliossimageloader.utils.GetPostUtils;
import com.fronttcapital.imageloader.AliOssImageLoader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class MainActivityPresenter implements IMainActivityContract.Presenter {

    private static final String TAG = "MainActivityPresenter";
    private IMainActivityContract.View view;

    public MainActivityPresenter(IMainActivityContract.View view) {

        this.view = view;
    }

    @Override
    public void getOSSAccessKey() {


        //工作线程加载credentials,并初始化AliOssImageLoader
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    // 下面是一些获取token的代码，比如从您的server获取
                    Map<String, String> map = new HashMap<>();
                    map.put("sign", "");
                    map.put("requestTime", "");
                    String jsonText = GetPostUtils.getInstance().post(Config.getStsServerUrl(), map);
                    Log.d(TAG, "jsonText = " + jsonText);
                    Type type = new TypeToken<APadResponse<RequestOssTokenBean>>() {
                    }.getType();
                    APadResponse<RequestOssTokenBean> aPadResponse = new Gson().fromJson(jsonText, type);
                    Log.d(TAG, "aPadResponse.getData()=" + aPadResponse.getData());
                    RequestOssTokenBean requestOssTokenBean = aPadResponse.getData();
                    RequestOssTokenBean.Credentials credentials = requestOssTokenBean.getCredentials();
                    String ak = credentials.getAccessKeyId();
                    String sk = credentials.getAccessKeySecret();
                    String token = credentials.getSecurityToken();
                    String expiration = credentials.getExpiration();
                    OSSFederationToken ossFederationToken = new OSSFederationToken(ak, sk, token, expiration);

                    AliOssImageLoader.init(ossFederationToken, "heart");

                    Utils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            view.getOSSAccessKeySucceed();

                        }
                    });

                } catch (Exception e) {
                    view.getOSSAccessKeyFailed("msg =" + Log.getStackTraceString(e));
                }

            }
        }).start();
    }
}
