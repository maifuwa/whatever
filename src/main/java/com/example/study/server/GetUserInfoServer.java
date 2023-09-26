package com.example.study.server;

public interface GetUserInfoServer {

    String[] getDeviceAndBrowser(String userAgentStr);

    String getUserAddress(String ip);
}
