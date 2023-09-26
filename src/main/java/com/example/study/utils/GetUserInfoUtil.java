package com.example.study.utils;

import com.example.study.pojo.ao.IpAddress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;

@Component
@Slf4j
public class GetUserInfoUtil {


    public static String[] getUserInfo(String userInfo) {
        String[] info = userInfo.split(" ");

        if (info.length <2) {
            return new String[]{"不知名设备", "不知名软件"};
        }else if (info.length <6){
            return new String[]{info[2], "不知名软件"};
        }

        return new String[]{info[2], info[6].split("/")[0]};
    }

    public static String[] getDateTime() {
        ZonedDateTime zdt = ZonedDateTime.now();
        String[] dateTime = DateTimeFormatter.ofPattern("EE，yyyy年MM月dd日/HH:mm:ss",  Locale.CHINA)
                .format(zdt).split("/");
        return new String[]{dateTime[0], dateTime[1]+"(CST)"};
    }

    public static int getVerifyCode() {
        return new Random().nextInt(899999) + 100000;
    }

    public static String getAddress(Optional<IpAddress> optAdd) {
        IpAddress address = optAdd.orElseGet(() -> {
            IpAddress fail = new IpAddress();
            fail.setStatus("fail");
            return fail;
        });

        if (address.getStatus().equals("success")) {
            return address.getCountry() + ","
                    + address.getRegionName() + ","
                    + address.getCity();
        }
       return "暂无地区信息";
    }
}
