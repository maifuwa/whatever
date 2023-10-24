package com.example.study.utils;

import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: maifuwa
 * @date: 2023/10/20 下午5:05
 * @description: 计算课程时间的工具类
 */

@Component
public class CourseTimeUtil {

    public Integer parseDay(String day) {
        return switch (day) {
            case "星期一" -> 1;
            case "星期二" -> 2;
            case "星期三" -> 3;
            case "星期四" -> 4;
            case "星期五" -> 5;
            case "星期六" -> 6;
            case "星期日" -> 7;
            default -> 0;
        };
    }

    public String getCourseNum() {
        String now = DateTimeFormatter.ofPattern("hh").format(LocalTime.now());
        return switch (now) {
            case "08" -> "1-%";
            case "09" -> "3-%";
            case "14" -> "6-%";
            case "15" -> "8-%";
            case "16" -> "9-%";
            case "19" -> "11-%";
            default -> "";
        };
    }

    public String getCourseTime(String courseNum) {
        String[] courseTime = this.parseCourseNum(courseNum);
        if (courseTime == null) {
            return "";
        }

        int start = Integer.parseInt(courseTime[0]);
        int end = Integer.parseInt(courseTime[1]);

        LocalTime startTime = LocalTime.of(8, 0);
        for (int i = 2; i <= start; i++) {
            int plus = i % 2 == 0 ? 15 : 0;
            startTime = startTime.plusMinutes(45 + plus);

            if (i == 6) {
                startTime = LocalTime.of(14, 0);
            }
            if (i == 11) {
                startTime = LocalTime.of(19, 0);
            }
        }

        LocalTime endTime = startTime;
        int i = end - start;
        if (i == 1) {
            endTime = endTime.plusMinutes(90);
        }else if (i == 2) {
            endTime = endTime.plusMinutes(140);
        }else if (i == 3) {
            endTime = endTime.plusMinutes(195);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        return formatter.format(startTime) + "-" + formatter.format(endTime);
    }

    private String[] parseCourseNum(String courseNum) {
        String regex = "(\\d+)-(\\d+)节";
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(courseNum);
        if (matcher.find()) {
            String start = matcher.group(1);
            String end = matcher.group(2);

            return new String[]{start, end};
        }

        return null;
    }


}
