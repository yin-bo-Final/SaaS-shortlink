package com.yin_bo_.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yin_bo_.shortlink.project.dao.entity.ShortLinkDO;
import com.yin_bo_.shortlink.project.dao.mapper.ShortLinkMapper;
import com.yin_bo_.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import com.yin_bo_.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import com.yin_bo_.shortlink.project.service.ShortLinkService;
import com.yin_bo_.shortlink.project.toolkit.HashUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 短链接接口实现层
 */
@Slf4j
@Service
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLinkDO> implements ShortLinkService {
    @Override
    public ShortLinkCreateRespDTO createShortlink(ShortLinkCreateReqDTO requestParam) {

        String shortlink = HashUtil.hashToBase62(requestParam.getOriginUrl());
        ShortLinkDO shortLinkDO = BeanUtil.toBean(requestParam, ShortLinkDO.class);
        shortLinkDO.setShortUri(shortlink);
        shortLinkDO.setFullShortUrl(requestParam.getDomain() + "/" + shortlink);
        save(shortLinkDO);
        ShortLinkCreateRespDTO respParam = new ShortLinkCreateRespDTO();
        respParam.setGid(shortLinkDO.getGid());
        respParam.setFullShortUrl(shortLinkDO.getFullShortUrl());
        respParam.setOriginUrl(shortLinkDO.getOriginUrl());
        return respParam;
    }
}
