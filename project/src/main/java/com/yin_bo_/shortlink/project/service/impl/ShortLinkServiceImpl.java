package com.yin_bo_.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yin_bo_.shortlink.project.common.convention.exception.ServiceException;
import com.yin_bo_.shortlink.project.dao.entity.ShortLinkDO;
import com.yin_bo_.shortlink.project.dao.mapper.ShortLinkMapper;
import com.yin_bo_.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import com.yin_bo_.shortlink.project.dto.req.ShortLinkPageReqDTO;
import com.yin_bo_.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import com.yin_bo_.shortlink.project.dto.resp.ShortLinkPageRespDTO;
import com.yin_bo_.shortlink.project.service.ShortLinkService;
import com.yin_bo_.shortlink.project.toolkit.HashUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 短链接接口实现层
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLinkDO> implements ShortLinkService {

    private final RBloomFilter<String> ShortUriCreateCachePenetrationBloomFilter;

    @Override
    public ShortLinkCreateRespDTO createShortLink(ShortLinkCreateReqDTO requestParam) {

        //将原始url哈希成短链接
        String shortLink = generateSuffix(requestParam);
        String fullShortLink = requestParam.getDomain() + "/" + shortLink;
        //将请求参数转化成 shortLinkDO 实体类
        ShortLinkDO shortLinkDO = BeanUtil.toBean(requestParam, ShortLinkDO.class);

        //设置实体类短链接和 完整url
        shortLinkDO.setShortUri(shortLink);
        shortLinkDO.setFullShortUrl(fullShortLink);
        try{
            //将实体类存储到数据库
            save(shortLinkDO);
        }catch (DuplicateKeyException e){
            log.warn("短链接:{} 重复入库",fullShortLink);
            throw new ServiceException("生成短链接繁忙，请稍后重试");
        }

        //新建相应参数的实体类
        ShortLinkCreateRespDTO respParam = new ShortLinkCreateRespDTO();
        //给相应实体类设置 gid 完整url 原始url
        respParam.setGid(shortLinkDO.getGid());
        respParam.setFullShortUrl(shortLinkDO.getFullShortUrl());
        respParam.setOriginUrl(shortLinkDO.getOriginUrl());
        return respParam;
    }

    @Override
    public IPage<ShortLinkPageRespDTO> pageShortLink(ShortLinkPageReqDTO requestParam) {


        Page<ShortLinkDO> page = new Page<>(
                requestParam.getCurrent(),
                requestParam.getSize()
        );

        LambdaQueryWrapper<ShortLinkDO> queryWrapper = Wrappers.lambdaQuery();
                queryWrapper.eq(ShortLinkDO::getGid, requestParam.getGid())
                .eq(ShortLinkDO::getDelFlag, 0)
                .eq(ShortLinkDO::getEnableStatus, 0);

        Page<ShortLinkDO> result = baseMapper.selectPage(page, queryWrapper);
        return result.convert(each -> {
            ShortLinkPageRespDTO dto = new ShortLinkPageRespDTO();
            BeanUtil.copyProperties(each, dto);
            return dto;
        });
    }


    private String generateSuffix(ShortLinkCreateReqDTO requestParam) {
        String originUrl = requestParam.getOriginUrl();
        String domain = requestParam.getDomain();
        int attempt = 0;
        while (attempt < 10){
            //获取当前时间
            String timeStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));

            //在哈希时加入扰动(这里是时间)，使其能生成不同的suffix
            String suffix = HashUtil.hashToBase62(originUrl + "#" + timeStr);
            String fullShortUrl = domain + "/" + suffix;

            if(!ShortUriCreateCachePenetrationBloomFilter.contains(fullShortUrl)){
                //布隆过滤器认为短链接不存在
                ShortUriCreateCachePenetrationBloomFilter.add(fullShortUrl);
                return suffix;
            }
            attempt++;
        }
        throw new ServiceException("生成短链接繁忙，请稍后重试");
    }
}
