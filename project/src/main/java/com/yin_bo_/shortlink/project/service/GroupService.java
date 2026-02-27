package com.yin_bo_.shortlink.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yin_bo_.shortlink.project.dao.entity.GroupDO;

/**
 * 短链接分组接口层
 */
public interface GroupService extends IService<GroupDO> {

    /**
     * 判断分组是否存在且属于指定用户
     */
    boolean existsByGidAndUsername(String gid, String username);
}
