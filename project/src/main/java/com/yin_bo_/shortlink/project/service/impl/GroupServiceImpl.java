package com.yin_bo_.shortlink.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yin_bo_.shortlink.project.dao.entity.GroupDO;
import com.yin_bo_.shortlink.project.dao.mapper.GroupMapper;
import com.yin_bo_.shortlink.project.service.GroupService;
import org.springframework.stereotype.Service;

/**
 * 短链接分组接口实现层
 */
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, GroupDO> implements GroupService {


    @Override
    public boolean existsByGidAndUsername(String gid, String username) {
        if (gid == null || username == null) {
            return true;
        }
        return !lambdaQuery()
                .eq(GroupDO::getGid, gid)
                .eq(GroupDO::getUsername, username)
                .eq(GroupDO::getDelFlag, 0)
                .exists();
    }
}
