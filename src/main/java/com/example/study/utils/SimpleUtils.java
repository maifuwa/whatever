package com.example.study.utils;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

/**
 * @author: maifuwa
 * @date: 2023/9/27 下午1:35
 * @description: 抽离出的一些方法
 */
@Component
@Slf4j
public class SimpleUtils {


    public static int getVerifyCode() {
        return new Random().nextInt(899999) + 100000;
    }

    public static String[] getDateTime() {
        LocalDateTime now = LocalDateTime.now();
        val formatter = DateTimeFormatter.ofPattern("EE，yyyy年MM月dd日", Locale.CHINA);
        String date = formatter.format(now);
        String time = now.toString().substring(11, 19) + "(CST)";

        return new String[]{date, time};
    }

    public static String getData() {
        return getDateTime()[0];
    }

    public static String getTime() {
        return getDateTime()[1];
    }
}
