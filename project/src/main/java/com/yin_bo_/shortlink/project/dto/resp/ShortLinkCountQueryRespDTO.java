package com.yin_bo_.shortlink.project.dto.resp;

import lombok.Data;

/**
 * 组内短链接数量查询返回参数
 */
@Data
public class ShortLinkCountQueryRespDTO {
    /**
     * 分组标示
     */
    private String gid;

    /**
     * 短链接数量
     */
    private Integer shortLinkCount;
}
