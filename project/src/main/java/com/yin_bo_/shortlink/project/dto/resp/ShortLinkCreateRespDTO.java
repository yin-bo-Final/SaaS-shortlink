package com.yin_bo_.shortlink.project.dto.resp;

import lombok.Data;

/**
 * 短链接创建响应对象
 */

@Data
public class ShortLinkCreateRespDTO {

    /**
     * 分组标识
     */
    private String gid;


    /**
     * 原始链接
     */
    private String originUrl;


    /**
     * 短链接
     */
    private String fullShortUrl;



}
