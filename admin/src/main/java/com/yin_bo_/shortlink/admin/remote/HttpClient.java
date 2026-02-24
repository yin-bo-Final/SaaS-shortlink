package com.yin_bo_.shortlink.admin.remote;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.yin_bo_.shortlink.admin.common.convention.Result;
import com.yin_bo_.shortlink.admin.common.convention.errorcode.BaseErrorCode;
import com.yin_bo_.shortlink.admin.common.convention.exception.RemoteException;
import org.springframework.stereotype.Component;

/**
 * 远程 HTTP 客户端
 */
@Component
public class HttpClient {

    public <T> Result<T> post(String url, Object body, TypeReference<Result<T>> type) {
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
