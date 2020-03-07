package com.fronttcapital.aliossimageloader.bean;

public class APadResponse<T> {

    //code==1表示成功
    private int code;

    //code!=1时 失败信息
    private String context;

    //code==1时,返回的数据
    private T data;

    private String errMsg;

    @Override
    public String toString() {
        return "APadResponse{" +
                "code=" + code +
                ", context='" + context + '\'' +
                ", data=" + data +
                ", errMsg='" + errMsg + '\'' +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
