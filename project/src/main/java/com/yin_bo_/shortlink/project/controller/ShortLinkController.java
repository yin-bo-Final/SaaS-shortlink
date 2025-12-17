package com.yin_bo_.shortlink.project.controller;


import com.yin_bo_.shortlink.project.common.convention.Result;
import com.yin_bo_.shortlink.project.common.convention.Results;
import com.yin_bo_.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import com.yin_bo_.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import com.yin_bo_.shortlink.project.service.ShortLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return Results.success(shortLinkService.createShortlink(requestParam));
    }



}
