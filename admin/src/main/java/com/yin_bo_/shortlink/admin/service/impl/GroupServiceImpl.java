package com.yin_bo_.shortlink.admin.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yin_bo_.shortlink.admin.common.convention.exception.ClientException;
import com.yin_bo_.shortlink.admin.common.enums.GroupErrorCodeEnum;
import com.yin_bo_.shortlink.admin.dao.entity.GroupDO;
import com.yin_bo_.shortlink.admin.dao.mapper.GroupMapper;
import com.yin_bo_.shortlink.admin.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

/**
 * 短链接分组接口实现层
 */
@Service
@RequiredArgsConstructor
public class GroupServiceImpl extends ServiceImpl<GroupMapper,GroupDO> implements GroupService {


    @Override
    public void saveGroup(String groupName) {
        //todo 从上下文获取username
        String username = null;
        int MaxTry = 3;

        for (int attempt = 1 ; attempt <= MaxTry; attempt++) {
            try {
                GroupDO groupDO = new GroupDO();
                String gid = RandomUtil.randomStringUpper(8);
                groupDO.setGid(gid);
                groupDO.setUsername(username);
                groupDO.setName(groupName);
                save(groupDO);
                return;
            } catch (DuplicateKeyException e) {
                if (attempt == MaxTry) {
                    log.error("创建分组失败");
                    throw new ClientException(GroupErrorCodeEnum.GROUP_SAVE_ERROR);
                }
                log.debug("gid重复，重试");
            }
        }
    }
}
