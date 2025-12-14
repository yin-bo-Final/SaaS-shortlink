package com.yin_bo_.shortlink.admin.common.enums;

import com.yin_bo_.shortlink.admin.common.convention.errorcode.IErrorCode;

public enum GroupErrorCodeEnum implements IErrorCode {
    GROUP_SAVE_ERROR("B000222","新增分组失败，请重试");

    private final String code;

    private final String message;

    GroupErrorCodeEnum(String code, String message) {
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
