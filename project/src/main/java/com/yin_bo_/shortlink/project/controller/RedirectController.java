package com.yin_bo_.shortlink.project.controller;

import com.yin_bo_.shortlink.project.service.ShortLinkService;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class RedirectController {
    private final ShortLinkService shortLinkService;


    /**
     * 短链接跳转
     * @param shortUri 短链接后缀
     * @param request  HTTP 请求
     * @param response HTTP 响应
     */
    @GetMapping("/{shortUri}")
    public void restoreUrl(@PathVariable String shortUri, ServletRequest request, ServletResponse response) throws IOException {
        shortLinkService.restoreUrl(shortUri, request, response);
    }
}
