package com.yin_bo_.shortlink.admin.remote;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yin_bo_.shortlink.admin.common.convention.Result;
import com.yin_bo_.shortlink.admin.common.convention.errorcode.BaseErrorCode;
import com.yin_bo_.shortlink.admin.common.convention.exception.RemoteException;
import com.yin_bo_.shortlink.admin.remote.dto.req.ShortLinkCreateReqDTO;
import com.yin_bo_.shortlink.admin.remote.dto.req.ShortLinkPageReqDTO;
import com.yin_bo_.shortlink.admin.remote.dto.resp.ShortLinkCreateRespDTO;
import com.yin_bo_.shortlink.admin.remote.dto.resp.ShortLinkPageRespDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 中台短链接远程调用
 */
@Service
public class ShortLinkRemoteService {

    @Value("${remote.project.base-url}")
    private String projectBaseUrl;

    public Result<ShortLinkCreateRespDTO> createShortLink(ShortLinkCreateReqDTO requestParam) {
        String url = projectBaseUrl + "/link/create";
        return post(url, requestParam, new TypeReference<Result<ShortLinkCreateRespDTO>>() {});
    }

    public Result<Page<ShortLinkPageRespDTO>> pageShortLink(ShortLinkPageReqDTO requestParam) {
        String url = projectBaseUrl + "/link/page";
        return post(url, requestParam, new TypeReference<Result<Page<ShortLinkPageRespDTO>>>() {});
    }

    private <T> Result<T> post(String url, Object body, TypeReference<Result<T>> type) {
        try {
            String response = HttpRequest.post(url)
                    .header("Content-Type", "application/json")
                    .body(JSONUtil.toJsonStr(body))
                    .timeout(5000)
                    .execute()
                    .body();
            return JSONUtil.toBean(response, type, true);
        } catch (Exception ex) {
            throw new RemoteException("调用中台接口失败: " + url, ex, BaseErrorCode.REMOTE_ERROR);
        }
    }
}
