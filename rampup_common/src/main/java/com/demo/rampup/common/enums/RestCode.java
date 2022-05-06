package com.demo.rampup.common.enums;

/**
 * @author zhouyw
 */

public enum RestCode {

    // 成功
    SUCCESS("0", "SUCCESS"),
    // 失败
    FAILED("1", "系统错误"),
    // 未知异常
    UNKNOW_EXCEPTION("500", "未知异常");

    /**
     * 返回码
     */
    private String code;
    /**
     * 信息
     */
    private String message;

    RestCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
