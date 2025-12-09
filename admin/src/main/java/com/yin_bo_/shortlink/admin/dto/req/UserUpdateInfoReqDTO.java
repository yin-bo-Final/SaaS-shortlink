package com.yin_bo_.shortlink.admin.dto.req;

import lombok.Data;


/**
 * 用户根据用户id修改其他信息请求DO
 */
@Data
public class UserUpdateInfoReqDTO {



    /**
     * id
     */
    private Long id;

    /**
     * 密码
     */
    private String password;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String mail;


}
