package com.yin_bo_.shortlink.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yin_bo_.shortlink.project.dao.entity.ShortLinkDO;
import com.yin_bo_.shortlink.project.dto.req.ShortLinkCountQueryReqDTO;
import com.yin_bo_.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import com.yin_bo_.shortlink.project.dto.req.ShortLinkPageReqDTO;
import com.yin_bo_.shortlink.project.dto.resp.ShortLinkCountQueryRespDTO;
import com.yin_bo_.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import com.yin_bo_.shortlink.project.dto.resp.ShortLinkPageRespDTO;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 短链接接口层
 */
public interface ShortLinkService extends IService<ShortLinkDO> {

    /**
     * 创建短链接
     * @param requestParam 请求参数
     * @return 短链接信息
     */
    ShortLinkCreateRespDTO createShortLink(ShortLinkCreateReqDTO requestParam);


    /**
     * 分页查询短链接
     * @param requestParam 请求参数
     * @return 页中短链接信息
     */
    IPage<ShortLinkPageRespDTO> pageShortLink(ShortLinkPageReqDTO requestParam);



    /**
     * 查询分组内短链接数量
     * @param requestParam 请求参数
     * @return 各分组内短链数量
     */
    List<ShortLinkCountQueryRespDTO> listGroupShortLinkCount(@RequestBody ShortLinkCountQueryReqDTO requestParam);
}
