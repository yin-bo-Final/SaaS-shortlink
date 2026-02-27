package com.yin_bo_.shortlink.admin.remote;

import cn.hutool.core.lang.TypeReference;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yin_bo_.shortlink.admin.common.convention.Result;
import com.yin_bo_.shortlink.admin.remote.dto.req.ShortLinkCountQueryReqDTO;
import com.yin_bo_.shortlink.admin.remote.dto.req.ShortLinkCreateReqDTO;
import com.yin_bo_.shortlink.admin.remote.dto.req.ShortLinkPageReqDTO;
import com.yin_bo_.shortlink.admin.remote.dto.resp.ShortLinkCountQueryRespDTO;
import com.yin_bo_.shortlink.admin.remote.dto.resp.ShortLinkCreateRespDTO;
import com.yin_bo_.shortlink.admin.remote.dto.resp.ShortLinkPageRespDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 中台短链接远程调用
 */
@Service
@RequiredArgsConstructor
public class ShortLinkRemoteService {

    //中台 URL
    @Value("${remote.project.base-url}")
    private String projectBaseUrl;

    private final HttpClient httpClient;

    /**
     * 短链接创建
     */
    public Result<ShortLinkCreateRespDTO> createShortLink(ShortLinkCreateReqDTO requestParam) {
        String url = projectBaseUrl + "/link/create";
        return httpClient.post(url, requestParam, new TypeReference<>() {
        });
    }


    /**
     *  短链接分页
     */
    public Result<Page<ShortLinkPageRespDTO>> pageShortLink(ShortLinkPageReqDTO requestParam) {
        String url = projectBaseUrl + "/link/page";
        return httpClient.post(url, requestParam, new TypeReference<>() {
        });
    }

    public Result<List<ShortLinkCountQueryRespDTO>> listGroupShortLinkCount(ShortLinkCountQueryReqDTO requestParam) {
        String url = projectBaseUrl + "/link/count";
        return httpClient.post(url, requestParam, new TypeReference<>() {
        });
    }
}
