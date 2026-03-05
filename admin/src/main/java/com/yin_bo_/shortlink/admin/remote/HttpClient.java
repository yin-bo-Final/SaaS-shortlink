package com.yin_bo_.shortlink.admin.remote;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.yin_bo_.shortlink.admin.common.convention.Result;
import com.yin_bo_.shortlink.admin.common.convention.errorcode.BaseErrorCode;
import com.yin_bo_.shortlink.admin.common.convention.exception.RemoteException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 远程 HTTP 客户端
 */
@Component
public class HttpClient {

    public <T> Result<T> post(String url, Object body, TypeReference<Result<T>> type) {
        try {
            HttpRequest request = HttpRequest.post(url)
                    .header("Content-Type", "application/json")
                    .body(JSONUtil.toJsonStr(body))
                    .timeout(5000);

            RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
            if (attrs instanceof ServletRequestAttributes servletAttrs) {
                HttpServletRequest servletRequest = servletAttrs.getRequest();
                String username = servletRequest.getHeader("username");
                String token = servletRequest.getHeader("token");
                if (username != null) {
                    request.header("username", username);
                }
                if (token != null) {
                    request.header("token", token);
                }
            }

            String response = request.execute().body();
            return JSONUtil.toBean(response, type, true);
        } catch (Exception ex) {
            throw new RemoteException("调用中台接口失败: " + url, ex, BaseErrorCode.REMOTE_ERROR);
        }
    }

    public void relayRedirect(String url, HttpServletRequest incomingRequest, HttpServletResponse outgoingResponse) {
        try {
            HttpRequest request = HttpRequest.get(url)
                    .timeout(5000)
                    .setFollowRedirects(false);

            if (incomingRequest != null) {
                String host = incomingRequest.getHeader("Host");
                if (host != null) {
                    request.header("Host", host);
                }
            }

            HttpResponse response = request.execute();
            outgoingResponse.setStatus(response.getStatus());

            String location = response.header("Location");
            if (location != null) {
                outgoingResponse.setHeader("Location", location);
            }
        } catch (Exception ex) {
            throw new RemoteException("调用中台接口失败: " + url, ex, BaseErrorCode.REMOTE_ERROR);
        }
    }
}
