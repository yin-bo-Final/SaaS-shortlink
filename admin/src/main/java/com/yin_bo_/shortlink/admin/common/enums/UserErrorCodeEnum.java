package com.yin_bo_.shortlink.admin.common.enums;

import com.yin_bo_.shortlink.admin.common.convention.errorcode.IErrorCode;

public enum UserErrorCodeEnum implements IErrorCode {
    USERNAME_EXIST_ERROR("A000201","用户名被占用"),
    USER_NOT_EXIST("A000200","用户不存在"),
    USER_SAVE_ERROR("B000111","用户信息保存失败");


    private final String code;

    private final String message;

    UserErrorCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
