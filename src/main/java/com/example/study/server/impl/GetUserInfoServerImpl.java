package com.example.study.server.impl;

import com.example.study.server.GetUserInfoServer;
import com.example.study.constant.ApiConst;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

/**
 * @author: maifuwa
 * @date: 2023/9/27 下午1:35
 * @description: 获取用户信息实体类
 */
@Service
public class GetUserInfoServerImpl implements GetUserInfoServer {

    @Resource
    RestTemplate restTemplate;

    @Override
    public String[] getDeviceAndBrowser(String userAgentStr) {
        UserAgent userAgent = UserAgent.parseUserAgentString(userAgentStr);
        String[] deviceAndBrowser = {userAgent.getOperatingSystem().getName(), userAgent.getBrowser().getName()};
        if ("Unknown".equals(deviceAndBrowser[0])) {
            deviceAndBrowser[0] = "未知设备";
        }
        if ("Unknown".equals(deviceAndBrowser[1])) {
            deviceAndBrowser[1] = "未知浏览器";
        }
        return deviceAndBrowser;
    }


    @Override
    public String getUserAddress(String ip) {
        IpAddress address = Optional.ofNullable(restTemplate.getForObject(ApiConst.API_IP_TO_LOCATION +ip, IpAddress.class))
                .orElse(new IpAddress("fail", null, null, null));

        if ("success".equals(address.status)) {
            return address.country + ","
                    + address.regionName() + ","
                    + address.city();
        }
        return "暂无地区信息";
    }

    private record IpAddress(String status, String country, String regionName, String city) {

    }
}
