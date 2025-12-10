package com.yin_bo_.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yin_bo_.shortlink.admin.dao.entity.UserDO;
import com.yin_bo_.shortlink.admin.dto.req.UserLoginReqDTO;
import com.yin_bo_.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.yin_bo_.shortlink.admin.dto.req.UserUpdateInfoReqDTO;
import com.yin_bo_.shortlink.admin.dto.req.UserUpdateUsernameReqDTO;
import com.yin_bo_.shortlink.admin.dto.resp.UserRespDTO;
import org.springframework.web.bind.annotation.RequestBody;


/*
* 用户接口层
* */
public interface UserService extends IService<UserDO> {
    /**
     * 根据用户名查询用户信息
     * @param username 用户名
     * @return 用户返回实体
     */
    UserRespDTO getUserByName(String username);

    /**
     * 查询用户名是否存在
     * @param username 用户名
     * @return 用户名存在返回true  不存在返回false
     */
    Boolean isUsernameOccupied(String username);


    /**
     * 用户注册
     * @param requestParam 用户的请求参数
     */
    //这里使用的参数名是requestParam，因为参数是一整个对象，要是叫什么DTO就不太好理解
    void register(UserRegisterReqDTO requestParam);


    void updateInfo(@RequestBody UserUpdateInfoReqDTO requestParam);

    void updateUsername(UserUpdateUsernameReqDTO requestParam);


    String login(UserLoginReqDTO requestParam);
}
