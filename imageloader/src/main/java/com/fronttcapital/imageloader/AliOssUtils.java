package com.fronttcapital.imageloader;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;

/**
 * Jay
 * 阿里云对象存储oss
 */
class AliOssUtils {

    private static final String TAG = "AliOssUtils";
    private static OSSClient ossClient;


    /**
     * 下载对象
     */
    public static AliOssResponse getObjectRequest(Context context, String objectName) throws ClientException, ServiceException {
        Log.d(TAG, "getObjectRequest objectName=" + objectName);

        // 构造下载文件请求。
        GetObjectRequest get = new GetObjectRequest(AliOssImageLoader.getBucketName(), objectName);

        //同步下载
        GetObjectResult objectResult = null;
        try {
            objectResult = getOss(context.getApplicationContext()).getObject(get);
        } catch (ServiceException serviceException) {

            //oss服务器不存在该文件异常
            if (serviceException.getStatusCode() == 404 && TextUtils.equals(serviceException.getErrorCode(), Constants.AliOssErrorMsg.NO_SUCH_KEY)) {
                AliOssResponse response = new AliOssResponse(serviceException.getStatusCode());
                response.setCode(serviceException.getErrorCode());
                return response;
            } else {
                //其他异常
                throw serviceException;
            }
        } catch (ClientException e) {
            if (!TextUtils.isEmpty(e.getMessage()) && e.getMessage().contains("InterruptedException")) {
                //ignore
            } else throw e;
        }

        if (objectResult == null) {
            return null;
        }

        if (objectResult.getStatusCode() == 200) {
            return new AliOssResponse(200, objectResult.getObjectContent());
        }

        return AliOssErrorResponseXmlPullParser.parse(objectResult.getStatusCode(), objectResult.getObjectContent());
    }


    private static OSSClient getOss(Context context) {
        if (ossClient == null) {
            synchronized (AliOssUtils.class) {

                if (ossClient == null) {
                    ossClient = new OSSClient(context.getApplicationContext(), Config.OSS_ENDPOINT, AliOssImageLoader.getCredentialProvider());
                }

            }
        }
        return ossClient;
    }

}
