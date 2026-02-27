package com.yin_bo_.shortlink.project.common.biz.user;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 用户信息传输过滤器（仅透传，不做鉴权）
 */
@Slf4j
@Component
@Order(-100)
public class UserTransmitFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        try {
            String username = request.getHeader("username");
            if (username != null) {
                UserInfoDTO userInfoDTO = UserInfoDTO.builder()
                        .username(username)
                        .token(request.getHeader("token"))
                        .build();
                UserContext.setUser(userInfoDTO);
            }
            filterChain.doFilter(request, servletResponse);
        } finally {
            UserContext.removeUser();
        }
    }
}
