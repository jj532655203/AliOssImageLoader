package com.fronttcapital.imageloader;

import android.text.TextUtils;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Jay
 * 阿里云oss下载object的状态码异常时,解析响应
 */
class AliOssErrorResponseXmlPullParser {

    private static final String TAG = "AliOssErrorResp...";

    public static AliOssResponse parse(int errorCode, InputStream inputStream) {
        Log.d(TAG, "parse errorCode=" + errorCode);

        if (errorCode == 200) return null;

        AliOssResponse aliOssResponse = null;
        try {
            XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
            xmlPullParser.setInput(inputStream, "UTF-8");
            int eventType = xmlPullParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        aliOssResponse = new AliOssResponse(errorCode);
                        break;
                    case XmlPullParser.START_TAG:
                        String name = xmlPullParser.getName();
                        if (TextUtils.equals(name, "Code")) {
                            aliOssResponse.setCode(xmlPullParser.nextText());
                        } else if (TextUtils.equals(name, "Message")) {
                            aliOssResponse.setMessage(xmlPullParser.nextText());
                        } else if (TextUtils.equals(name, "RequestId")) {
                            aliOssResponse.setRequestId(xmlPullParser.nextText());
                        } else if (TextUtils.equals(name, "HostId")) {
                            aliOssResponse.setHostId(xmlPullParser.nextText());
                        }
                        break;
                }
                eventType = xmlPullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return aliOssResponse;
    }
}
