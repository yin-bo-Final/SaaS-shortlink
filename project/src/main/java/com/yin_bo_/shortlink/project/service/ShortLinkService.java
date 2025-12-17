package com.yin_bo_.shortlink.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yin_bo_.shortlink.project.dao.entity.ShortLinkDO;
import com.yin_bo_.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import com.yin_bo_.shortlink.project.dto.resp.ShortLinkCreateRespDTO;

/**
 * 短链接接口层
 */
public interface ShortLinkService extends IService<ShortLinkDO> {

    /**
     * 创建短链接
     * @param requestParam 请求参数
     * @return 短链接信息
     */
    ShortLinkCreateRespDTO createShortlink(ShortLinkCreateReqDTO requestParam);
}
