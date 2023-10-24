package com.example.study.pojo;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;

/**
 * @author: maifuwa
 * @date: 2023/9/27 下午1:35
 * @description: 相应类
 */
public record RestBean<T>(int code, T data, String message) {

    public static <T> RestBean<T> success() {
        return RestBean.success(null);
    }
    public static <T> RestBean<T> success(T data) {
        return new RestBean<>(200, data, "请求成功");
    }
    public static <T> RestBean<T> unauthorized(String message) {
        return RestBean.failure(401, message);
    }
    public static <T> RestBean<T> forbidden(String message) {
        return RestBean.failure(403, message);
    }
    public static <T> RestBean<T> failure(int code, String message) {
        return new RestBean<>(code, null, message);
    }
    public String asJSONString() {
        return JSONObject.toJSONString(this, JSONWriter.Feature.WriteNulls);
    }

}
