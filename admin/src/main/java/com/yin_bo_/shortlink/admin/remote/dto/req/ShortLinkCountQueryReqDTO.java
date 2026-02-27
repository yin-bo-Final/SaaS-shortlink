package com.yin_bo_.shortlink.admin.remote.dto.req;


import lombok.Data;

import java.util.List;


/**
 * 组内短链接数量查询参数
 */
@Data
public class ShortLinkCountQueryReqDTO {
    List<String> gids;
}
