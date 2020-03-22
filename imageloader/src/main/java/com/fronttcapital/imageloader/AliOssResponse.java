package com.fronttcapital.imageloader;

import java.io.InputStream;

public class AliOssResponse {
    private int statusCode;
    private InputStream inputStream;

    //statusCode != 200时
    private String code;
    private String message;
    private String requestId;
    private String hostId;


    public AliOssResponse(int statusCode, InputStream inputStream) {
        this.statusCode = statusCode;
        this.inputStream = inputStream;
    }

    public AliOssResponse(int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        return "AliOssResponse{" +
                "statusCode=" + statusCode +
                ", inputStream=" + inputStream +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", requestId='" + requestId + '\'' +
                ", hostId='" + hostId + '\'' +
                '}';
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }
}
