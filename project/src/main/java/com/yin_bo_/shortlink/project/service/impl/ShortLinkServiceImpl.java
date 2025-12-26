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

        //将原始url哈希成短链接
        String shortlink = HashUtil.hashToBase62(requestParam.getOriginUrl());

        //将请求参数转化成 shortLinkDO 实体类
        ShortLinkDO shortLinkDO = BeanUtil.toBean(requestParam, ShortLinkDO.class);

        //设置实体类短链接和 完整url
        shortLinkDO.setShortUri(shortlink);
        shortLinkDO.setFullShortUrl(requestParam.getDomain() + "/" + shortlink);

        //将实体类存储到数据库
        save(shortLinkDO);

        //新建相应参数的实体类
        ShortLinkCreateRespDTO respParam = new ShortLinkCreateRespDTO();
        //给相应实体类设置 gid 完整url 原始url
        respParam.setGid(shortLinkDO.getGid());
        respParam.setFullShortUrl(shortLinkDO.getFullShortUrl());
        respParam.setOriginUrl(shortLinkDO.getOriginUrl());
        return respParam;
    }
}
