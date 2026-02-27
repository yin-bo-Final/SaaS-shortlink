package com.yin_bo_.shortlink.project.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yin_bo_.shortlink.project.common.convention.Result;
import com.yin_bo_.shortlink.project.common.convention.Results;
import com.yin_bo_.shortlink.project.dto.req.ShortLinkCountQueryReqDTO;
import com.yin_bo_.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import com.yin_bo_.shortlink.project.dto.req.ShortLinkPageReqDTO;
import com.yin_bo_.shortlink.project.dto.req.ShortLinkUpdateReqDTO;
import com.yin_bo_.shortlink.project.dto.resp.ShortLinkCountQueryRespDTO;
import com.yin_bo_.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import com.yin_bo_.shortlink.project.dto.resp.ShortLinkPageRespDTO;
import com.yin_bo_.shortlink.project.service.ShortLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 短链接控制层
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shortlink/v1/project")
public class ShortLinkController {
    private final ShortLinkService shortLinkService;


    /**
     * 创建短连接
     * @param requestParam 请求参数
     * @return 短链接信息
     */
    @PostMapping("/link/create")
    public Result<ShortLinkCreateRespDTO> createShortLink(@RequestBody ShortLinkCreateReqDTO requestParam) {
        return Results.success(shortLinkService.createShortLink(requestParam));
    }


    /**
     * 分页查询短链接
     * @param requestParam 请求参数
     * @return 页中短链接信息
     */
    @PostMapping("/link/page")
    public Result<IPage<ShortLinkPageRespDTO>> pageShortLink(@RequestBody ShortLinkPageReqDTO requestParam) {
        return Results.success(shortLinkService.pageShortLink(requestParam));
    }


    /**
     * 查询分组内短链接数量
     * @param requestParam 请求参数
     * @return 各分组内短链数量
     */
    @PostMapping("/link/count")
    public Result<List<ShortLinkCountQueryRespDTO>> listGroupShortLinkCount(@RequestBody ShortLinkCountQueryReqDTO requestParam) {
        return Results.success(shortLinkService.listGroupShortLinkCount(requestParam));
    }

    /**
     * 修改短链接
     * @param requestParam 请求参数
     * @return 修改成功
     */
    @PostMapping("/link/update")
    public Result<Void> updateGroup(@RequestBody ShortLinkUpdateReqDTO requestParam) {
        shortLinkService.updateShortLink(requestParam);
        return Results.success();
    }

}
