package com.yin_bo_.shortlink.project.dto.req;

import lombok.Data;



/**
 * 短链接分页请求参数
 */
@Data
public class ShortLinkPageReqDTO  {

    /**
     * 分组标识
     */
    private String gid;


    /**
     * 当前页数
     * 默认为1
     */
    private Long current = 1L;


    /**
     * 分页大小
     * 默认为10
     */
    private Long size = 10L;
}
