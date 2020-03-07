package com.fronttcapital.aliossimageloader;

import android.app.Application;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationToken;
import com.fronttcapital.aliossimageloader.bean.APadResponse;
import com.fronttcapital.aliossimageloader.bean.RequestOssTokenBean;
import com.fronttcapital.aliossimageloader.utils.GetPostUtils;
import com.fronttcapital.imageloader.AliOssImageLoader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class App extends Application {

    private static final String TAG = "App";

    @Override
    public void onCreate() {
        super.onCreate();

        //AliOssImageLoader初始化
        AliOssImageLoader.init(new OSSFederationCredentialProvider() {

            /**
             * 注意!:ossClient会在token失效时(不固定啥时候失效哦),调用本方法重新获取token
             * 作者公司是采用后台申请授权,所以有如下代码
             * 还有其它实例化OSSFederationToken的方法见:https://help.aliyun.com/document_detail/100624.html?spm=a2c4g.11186623.6.664.4d281c62Q8euRd
             */
            @Override
            public OSSFederationToken getFederationToken() throws ClientException {

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

                return new OSSFederationToken(ak, sk, token, expiration);
            }
        }, "heart");

    }
}
