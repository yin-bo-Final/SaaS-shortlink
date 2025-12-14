package com.yin_bo_.shortlink.admin.dto.req.UserReqDTO;

import lombok.Data;


/**
 * 用户登录接口请求实体类
 */
@Data
public class UserLoginReqDTO {
    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;
}
