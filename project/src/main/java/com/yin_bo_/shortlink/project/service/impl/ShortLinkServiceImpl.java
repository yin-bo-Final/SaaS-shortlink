package com.yin_bo_.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yin_bo_.shortlink.project.common.convention.exception.ServiceException;
import com.yin_bo_.shortlink.project.dao.entity.ShortLinkDO;
import com.yin_bo_.shortlink.project.dao.mapper.ShortLinkMapper;
import com.yin_bo_.shortlink.project.dto.req.ShortLinkCountQueryReqDTO;
import com.yin_bo_.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import com.yin_bo_.shortlink.project.dto.req.ShortLinkPageReqDTO;
import com.yin_bo_.shortlink.project.dto.resp.ShortLinkCountQueryRespDTO;
import com.yin_bo_.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import com.yin_bo_.shortlink.project.dto.resp.ShortLinkPageRespDTO;
import com.yin_bo_.shortlink.project.service.ShortLinkService;
import com.yin_bo_.shortlink.project.toolkit.HashUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * 短链接接口实现层
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLinkDO> implements ShortLinkService {

    private final RBloomFilter<String> ShortUriCreateCachePenetrationBloomFilter;
    private final GroupService groupService;


    /**
     * 创建短链接
     */
    @Override
    public ShortLinkCreateRespDTO createShortLink(ShortLinkCreateReqDTO requestParam) {
        String username = UserContext.getUsername();
        if (username == null) {
            throw new ServiceException("未获取到用户信息");
        }
        if (groupService.existsByGidAndUsername(requestParam.getGid(), username)) {
            throw new ServiceException("目标分组不存在");
        }
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


    /**
     * 短链接列表
     */
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

    @Override
    public List<ShortLinkCountQueryRespDTO> listGroupShortLinkCount(@RequestBody ShortLinkCountQueryReqDTO requestParam) {
        List<String> gids = requestParam.getGids();
        if (gids == null || gids.isEmpty()) {
            return List.of();
        }
        QueryWrapper<ShortLinkDO> queryWrapper = Wrappers.query(new ShortLinkDO())
                .select("gid", "count(1) as shortLinkCount")
                .in("gid",gids)
                .eq("enable_status",0)
                .eq("del_flag",0)
                .groupBy("gid");
        List<Map<String, Object>> shortLinkCountList = baseMapper.selectMaps(queryWrapper);
        return BeanUtil.copyToList(shortLinkCountList,ShortLinkCountQueryRespDTO.class);

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
