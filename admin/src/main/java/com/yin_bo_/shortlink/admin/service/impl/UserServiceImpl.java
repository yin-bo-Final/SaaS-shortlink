package com.yin_bo_.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yin_bo_.shortlink.admin.common.convention.exception.ClientException;
import com.yin_bo_.shortlink.admin.common.enums.UserErrorCodeEnum;
import com.yin_bo_.shortlink.admin.dao.entity.UserDO;
import com.yin_bo_.shortlink.admin.dao.mapper.UserMapper;
import com.yin_bo_.shortlink.admin.dto.resp.UserRespDTO;
import com.yin_bo_.shortlink.admin.service.UserService;
import org.springframework.stereotype.Service;


/**
 * 用户接口实现层
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements  UserService {

    @Override
    public UserRespDTO getUserByName(String username) {
        UserDO userDo = lambdaQuery()
                .eq(UserDO::getUsername, username)
                .one();
        // one()方法：查到多条会抛出异常，查不到返回null

        if (userDo == null) {
            throw new ClientException(UserErrorCodeEnum.USER_NOT_EXIST);
        }
        //转换，使用BeanUtil的copyProperties
        return BeanUtil.copyProperties(userDo,UserRespDTO.class);
    }
}
