package com.yin_bo_.shortlink.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 短链接跳转实体
 */
@Data
@TableName("t_link_goto")
public class ShortLinkGotoDO {

    /**
     * id
     */
    private Long id;


    /**
     * 完整短链接
     */
    private String fullShortUrl;

    /**
     * 分组标识
     */
    private String gid;

}
