package com.yin_bo_.shortlink.admin.remote.dto.req;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 短链接创建请求对象
 */

@Data
public class ShortLinkUpdateReqDTO {
    /**
     * 域名
     */
    private String domain;

    /**
     * 原始链接
     */
    private String originUrl;


    /**
     * 分组标识
     */
    private String gid;


    /**
     * 新分组标识
     */
    private String newGid;

    /**
     * 完整短链接
     */
    private String fullShortUrl;



    /**
     * 网站图标
     */
    private String favicon;


    /**
     * 有效期类型 0：永久有效 1：用户自定义
     */
    private Integer validDateType;

    /**
     * 有效期
     */
    private LocalDateTime validDate;


    /**
     * 描述
     */
    private String describe;

}
