package com.yin_bo_.shortlink.admin.controller;

import com.yin_bo_.shortlink.admin.remote.ShortLinkRemoteService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RedirectController {
    private final ShortLinkRemoteService shortLinkRemoteService;


    /**
     * 短链接跳转
     * @param shortUri 短链接后缀
     * @param request  HTTP 请求
     * @param response HTTP 响应
     */
    @GetMapping("/{shortUri}")
    public void restoreUrl(@PathVariable String shortUri, HttpServletRequest request, HttpServletResponse response) {
        shortLinkRemoteService.restoreUrl(shortUri, request, response);
    }
}
