package com.fronttcapital.aliossimageloader.utils;

import android.util.Log;

import com.blankj.utilcode.util.DeviceUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;

;

/**
 * Jay
 * Android原生api的get和post请求 工具类
 */
public class GetPostUtils {

    private static final String TAG = "GetPostUtils";
    private static GetPostUtils getPost;

    private GetPostUtils() {
    }

    public static GetPostUtils getInstance() {
        if (getPost == null) {
            synchronized (GetPostUtils.class) {
                if (getPost == null) {
                    getPost = new GetPostUtils();
                }
            }
        }
        return getPost;
    }

    private static final String VERSION_KEY = "version";
    //升级版本号
    private static final String VERSION_VALUE = "1.2";
    //增加头部扩展字段，避免后面有问题，服务器可以对客户端做兼容处理
    //头部key不能用下划线，否则服务器收不到数据
    public static final String SDK_VERSION_CODE_KEY = "sdk-version-code-key";
    public static final String SDK_VERSION_NAME_KEY = "sdk-version-name-key";
    public static final String APP_IS_BETA = "app-is-beta";
    public static final String APP_IS_DEBUG = "app-is-debug";
    public static final String APP_VERSION_CODE = "app-version-code";
    public static final String APP_VERSION_NAME = "app-version-name";
    public static final String APP_PACKAGE_NAME = "app-package-name";

    /**
     * POST请求
     *
     * @param url url
     * @param map 请求参数的map集合形式
     */
    public static String post(final String url, final Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        DataOutputStream out = null;
        BufferedReader br = null;
        URLConnection conn;
        try {
            URL postUrl = new URL(url);
            conn = postUrl.openConnection();//创建连接
            conn.setDoInput(true);//post请求必须设置
            conn.setDoOutput(true);//post请求必须设置

            /*//作者公司测试环境配置需求 start*/
            conn.addRequestProperty(VERSION_KEY, VERSION_VALUE);
            conn.addRequestProperty(SDK_VERSION_CODE_KEY, String.valueOf(DeviceUtils.getSDKVersionCode()));
            conn.addRequestProperty(SDK_VERSION_NAME_KEY, DeviceUtils.getSDKVersionName());
            conn.addRequestProperty(APP_IS_BETA, String.valueOf(true));
            conn.addRequestProperty(APP_IS_DEBUG, String.valueOf(true));
            conn.addRequestProperty(APP_VERSION_CODE, String.valueOf(10429));
            conn.addRequestProperty(APP_VERSION_NAME, "1.4.29");
            conn.addRequestProperty(APP_PACKAGE_NAME, "com.fronttcapital.exercisebookteacher");
            /*//作者公司测试环境配置需求 end*/


            out = new DataOutputStream(conn.getOutputStream());//输出流
            StringBuilder request = new StringBuilder();
            for (String key : map.keySet()) {
                request.append(key + "=" + URLEncoder.encode(map.get(key), "UTF-8") + "&");
            }//连接请求参数
            out.writeBytes(request.toString());//输出流写入请求参数
            out.flush();
            out.close();
            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));//获取输入流
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            System.out.println(sb.toString());
            Log.d(TAG, "sb=" + sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {//执行流的关闭
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }


    /**
     * 发送get请求
     */
    public static String get(final String url) {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        InputStreamReader isr = null;
        URLConnection conn;
        try {
            URL geturl = new URL(url);
            conn = geturl.openConnection();//创建连接
            conn.connect();//get连接
            isr = new InputStreamReader(conn.getInputStream());//输入流
            br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);//获取输入流数据
            }
            System.out.println(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {//执行流的关闭
            if (br != null) {
                try {
                    br.close();
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
}
