package com.yin_bo_.shortlink.admin.dto.req.GroupReqDTO;


import lombok.Data;

@Data
public class GroupSortReqDTO {

    /**
     * 分组ID
     */
    private String gid;

    /**
     * 排序
     */
    private Integer sortOrder;
}
