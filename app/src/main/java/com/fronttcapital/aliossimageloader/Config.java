package com.fronttcapital.aliossimageloader;

public class Config {

    private final static String BETA_HOST = "http://test-heart-api.800801.top/";

    private final static String STS_SERVER_URL = "heart/open/upLoadToken";//STS 地址

    public static String getStsServerUrl() {
        return BETA_HOST + STS_SERVER_URL;
    }
}
