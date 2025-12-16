package com.yin_bo_.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yin_bo_.shortlink.admin.dao.entity.GroupDO;
import com.yin_bo_.shortlink.admin.dto.req.GroupReqDTO.GroupSortReqDTO;
import com.yin_bo_.shortlink.admin.dto.resp.GroupRespDTO;

import java.util.List;

/**
 * 短链接接口层
 */
public interface GroupService extends IService<GroupDO> {


    /**
     * 新增短链接分组
     * @param groupName  分组名称
     */
    void saveGroup(String groupName);

    /**
     * 查询用户短链接分组集合
     * @return 短链接分组集合
     */
    List<GroupRespDTO> listGroup();

    /**
     * 修改短链接分组
     *
     * @param groupName 分组名称
     * @param gid  分组ID
     */
    void updateGroup(String groupName, String gid);


    /**
     * 删除短链接分组
     * @param gid 分组ID
     */
    void removeGroup(String gid);


    /**
     * 短链接分组排序
     * @param requestParam 请求参数
     */
    void sortGroup(List<GroupSortReqDTO> requestParam);


}
