package com.yin_bo_.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yin_bo_.shortlink.admin.common.biz.user.UserContext;
import com.yin_bo_.shortlink.admin.common.convention.exception.ClientException;
import com.yin_bo_.shortlink.admin.common.enums.GroupErrorCodeEnum;
import com.yin_bo_.shortlink.admin.dao.entity.GroupDO;
import com.yin_bo_.shortlink.admin.dao.mapper.GroupMapper;
import com.yin_bo_.shortlink.admin.dto.resp.GroupRespDTO;
import com.yin_bo_.shortlink.admin.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 短链接分组接口实现层
 */
@Service
@RequiredArgsConstructor
public class GroupServiceImpl extends ServiceImpl<GroupMapper,GroupDO> implements GroupService {


    @Override
    public void saveGroup(String groupName) {
        String username = UserContext.getUsername();
        int MaxTry = 3;
        for (int attempt = 1; attempt <= MaxTry; attempt++) {
            try {
                GroupDO groupDO = new GroupDO();
                String gid = RandomUtil.randomStringUpper(8);
                groupDO.setGid(gid);
                groupDO.setSortOrder(0);
                groupDO.setName(groupName);
                groupDO.setUsername(username);
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

    @Override
    public List<GroupRespDTO> listGroup() {
        Page<GroupDO> page = query()
                .eq("username", UserContext.getUsername())
                .eq("del_flag", 0)
                .orderByAsc("sort_order")
                .page(new Page<>(1, 10));

        return BeanUtil.copyToList(page.getRecords(), GroupRespDTO.class);
    }

    @Override
    public void updateGroup(String groupName, String gid) {
        update()
                .eq("username", UserContext.getUsername())
                .eq("del_flag", 0)
                .eq("gid", gid)
                .set("name", groupName)
                .update();
    }

}
