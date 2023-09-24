package com.example.study.entity;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import org.slf4j.MDC;

import java.util.Optional;

public record RestBean<T>(long id, int code, T data, String message) {

    public static <T> RestBean<T> success() {
        return RestBean.success(null);
    }
    public static <T> RestBean<T> success(T data) {
        return new RestBean<>(requestId(), 200, data, "请求成功");
    }

    public static <T> RestBean<T> failure(int code, String message) {
        return new RestBean<>(requestId(), code, null, message);
    }

    public String asJSONString() {
        return JSONObject.toJSONString(this, JSONWriter.Feature.WriteNulls);
    }

    private static long requestId(){
        String requestId = Optional.ofNullable(MDC.get("reqId")).orElse("0");
        return Long.parseLong(requestId);
    }
}