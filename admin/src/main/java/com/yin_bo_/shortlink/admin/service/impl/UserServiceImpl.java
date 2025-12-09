package com.yin_bo_.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yin_bo_.shortlink.admin.common.convention.exception.ClientException;
import com.yin_bo_.shortlink.admin.common.enums.UserErrorCodeEnum;
import com.yin_bo_.shortlink.admin.dao.entity.UserDO;
import com.yin_bo_.shortlink.admin.dao.mapper.UserMapper;
import com.yin_bo_.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.yin_bo_.shortlink.admin.dto.req.UserUpdateInfoReqDTO;
import com.yin_bo_.shortlink.admin.dto.req.UserUpdateUsernameReqDTO;
import com.yin_bo_.shortlink.admin.dto.resp.UserRespDTO;
import com.yin_bo_.shortlink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static com.yin_bo_.shortlink.admin.common.constant.RedisCacheConstant.LOCK_USER_REGISTER_KEY;


/**
 * 用户接口实现层
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements  UserService {
    private final RBloomFilter<String> userRegisterCachePenetrationBloomFilter;
    private final RedissonClient redissonClient;


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

    @Override
    public Boolean isUsernameOccupied(String username) {
        return userRegisterCachePenetrationBloomFilter.contains(username);
    }


    @Override
    public void register(UserRegisterReqDTO requestParam) {
        //1. 判断username是否存在
        if (isUsernameOccupied(requestParam.getUsername())) {
            throw new ClientException(UserErrorCodeEnum.USERNAME_EXIST_ERROR);
        }
        //2. 使用Redisson分布式锁，防止缓存穿透
        RLock lock = redissonClient.getLock(LOCK_USER_REGISTER_KEY + requestParam.getUsername());
        try {
            boolean isLocked = lock.tryLock(3, 10, TimeUnit.SECONDS);
            if(isLocked){
                boolean isSaved = save(BeanUtil.toBean(requestParam, UserDO.class));
                if(!isSaved){
                    throw new ClientException(UserErrorCodeEnum.USER_SAVE_ERROR);
                }
                userRegisterCachePenetrationBloomFilter.add(requestParam.getUsername());
            }
        } catch (Exception e) {
            throw new ClientException(UserErrorCodeEnum.USERNAME_EXIST_ERROR);
        } finally {
            //isHeldByCurrentThread方法只能解当前线程的锁
            //如果使用lock.islocked，就会释放其他线程的锁
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    public void updateInfo(UserUpdateInfoReqDTO requestParam) {
        //todo 这里之后改为使用用户登录态比如说redis里获取id来查询用户数据
        UserDO userDO = getById(requestParam.getId());
        if (userDO == null) {
            throw new ClientException(UserErrorCodeEnum.USER_NOT_EXIST);
        }
        UserDO updateBean = BeanUtil.toBean(requestParam, UserDO.class);
        updateById(updateBean);
    }

    @Override
    public void updateUsername(UserUpdateUsernameReqDTO requestParam) {
        if (isUsernameOccupied(requestParam.getUsername())) {
            throw new ClientException(UserErrorCodeEnum.USERNAME_EXIST_ERROR);
        }
        String newUsername = requestParam.getUsername();

        //todo 这里之后改为使用用户登录态比如说redis里获取id来查询用户数据
        UserDO userDO = getById(requestParam.getId());
        if (userDO == null) {
            throw new ClientException(UserErrorCodeEnum.USER_NOT_EXIST);
        }
        UserDO updatedBean = BeanUtil.toBean(requestParam, UserDO.class);
        updateById(updatedBean);
        userRegisterCachePenetrationBloomFilter.add(newUsername);
    }
}
