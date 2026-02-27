package com.yin_bo_.shortlink.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yin_bo_.shortlink.admin.common.convention.Result;
import com.yin_bo_.shortlink.admin.remote.ShortLinkRemoteService;
import com.yin_bo_.shortlink.admin.remote.dto.req.ShortLinkCountQueryReqDTO;
import com.yin_bo_.shortlink.admin.remote.dto.req.ShortLinkCreateReqDTO;
import com.yin_bo_.shortlink.admin.remote.dto.req.ShortLinkPageReqDTO;
import com.yin_bo_.shortlink.admin.remote.dto.resp.ShortLinkCountQueryRespDTO;
import com.yin_bo_.shortlink.admin.remote.dto.resp.ShortLinkCreateRespDTO;
import com.yin_bo_.shortlink.admin.remote.dto.resp.ShortLinkPageRespDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 后管短链接控制层
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shortlink/v1/admin")
public class ShortLinkController {

    private final ShortLinkRemoteService shortLinkRemoteService;

    /**
     * 创建短链接
     */
    @PostMapping("/link/create")
    public Result<ShortLinkCreateRespDTO> createShortLink(@RequestBody ShortLinkCreateReqDTO requestParam) {
        return shortLinkRemoteService.createShortLink(requestParam);
    }

    /**
     * 分页查询短链接
     */
    @PostMapping("/link/page")
    public Result<Page<ShortLinkPageRespDTO>> pageShortLink(@RequestBody ShortLinkPageReqDTO requestParam) {
        return shortLinkRemoteService.pageShortLink(requestParam);
    }


    /**
     * 查询分组内短链接数量
     */
    @PostMapping("/link/count")
    public Result<List<ShortLinkCountQueryRespDTO>> listGroupShortLinkCount(@RequestBody ShortLinkCountQueryReqDTO requestParam) {
        return shortLinkRemoteService.listGroupShortLinkCount(requestParam);
    }

}
