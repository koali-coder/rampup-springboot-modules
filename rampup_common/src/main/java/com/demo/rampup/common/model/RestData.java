package com.demo.rampup.common.model;

import com.demo.rampup.common.enums.RestCode;
import lombok.Data;

import java.util.UUID;

/**
 * @author zhouyw
 */
@Data
public class RestData<T> {

    private T data;

    private String code;

    private String message;

    private String requestId = UUID.randomUUID().toString();

    public static <T> RestData<T> success() {

        RestData<T> rtn = new RestData<>();
        rtn.setCode(RestCode.SUCCESS.getCode());
        rtn.setMessage(RestCode.SUCCESS.getMessage());
        return rtn;
    }

    public static <T> RestData<T> success(T t) {

        RestData<T> rtn = new RestData<>();
        rtn.setData(t);
        rtn.setCode(RestCode.SUCCESS.getCode());
        rtn.setMessage(RestCode.SUCCESS.getMessage());
        return rtn;
    }

    public static <T> RestData<T> success(T t, String message) {

        RestData<T> rtn = new RestData<>();
        rtn.setData(t);
        rtn.setCode(RestCode.SUCCESS.getCode());
        rtn.setMessage(message);
        return rtn;
    }

    public static <T> RestData<T> failed() {
        RestData<T> rtn = new RestData<T>();
        rtn.setCode(RestCode.FAILED.getCode());
        rtn.setMessage(RestCode.FAILED.getMessage());
        return rtn;
    }

    public static <T> RestData<T> failed(T data) {
        RestData<T> rtn = new RestData<T>();
        rtn.setData(data);
        rtn.setCode(RestCode.FAILED.getCode());
        rtn.setMessage(RestCode.FAILED.getMessage());
        return rtn;
    }

    public static <T> RestData<T> failed(T t, String message) {
        RestData<T> rtn = new RestData<T>();
        rtn.setData(t);
        rtn.setCode(RestCode.FAILED.getCode());
        rtn.setMessage(message);
        return rtn;
    }

}
