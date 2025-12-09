package com.yin_bo_.shortlink.admin.dto.req;


import lombok.Data;


/**
 * 根据用户id修改用户名请求DO
 */
@Data
public class UserUpdateUsernameReqDTO {

    private Long id;

    private String username;

}
