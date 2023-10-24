package com.example.study.server;

/**
 * @author: maifuwa
 * @date: 2023/9/27 下午1:35
 * @description: 获取用户信息服务
 */
public interface GetUserInfoServer {

    String[] getDeviceAndBrowser(String userAgentStr);

    String getUserAddress(String ip);
}
