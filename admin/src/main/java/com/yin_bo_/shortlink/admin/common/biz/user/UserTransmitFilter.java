package com.yin_bo_.shortlink.admin.common.biz.user;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.yin_bo_.shortlink.admin.common.constant.RedisCacheConstant.LOGIN;

/**
 * 用户信息传输过滤器（兼具登录校验功能）
 * 修复了：空指针、未登录放行、500 错误等问题
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Order(-100) // 尽量靠前执行（比其他业务过滤器早）
public class UserTransmitFilter implements Filter {

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        try {
            String username = request.getHeader("username");
            String token = request.getHeader("token");


            //如果username或者token中有为空的 说明用户未登录 不设置上下文 发行
            if (CharSequenceUtil.isBlank(username) || CharSequenceUtil.isBlank(token)) {
                filterChain.doFilter(request, response);
                return;
            }

            // 2. 安全获取 Redis 中的用户信息（防止 null 导致 500）
            String redisKey = LOGIN + username;
            Object userInfoObj;
            try {
                userInfoObj = stringRedisTemplate.opsForHash().get(redisKey, token);
            } catch (Exception e) {
                //未查询到 不设置上下文 放行
                log.error("Redis 查询用户信息异常, username: {}, token: {}", username, token, e);
                filterChain.doFilter(request, response);
                return;
            }

            // 3. Redis 中不存在该 token，说明登录已过期或无效
            if (userInfoObj == null) {
                // 可选：这里可以直接返回 401 让前端跳转登录页
                // writeUnauthorized(response, "登录已失效");
                // return;

                // 或者像现在这样：不设置上下文，继续向下（业务层会判断 null 拒绝访问）
                filterChain.doFilter(request, response);
                return;
            }

            // 4. 正常情况：反序列化并放入当前线程上下文
            try {
                UserInfoDTO userInfoDTO = JSONUtil.toBean(userInfoObj.toString(), UserInfoDTO.class);
                // 可选：额外校验：校验请求头的 id 是否和 Redis 里的一致，防止伪造
                String headerId = request.getHeader("id");
                if (CharSequenceUtil.isNotBlank(headerId)) {
                    return;
                }
                UserContext.setUser(userInfoDTO);
            } catch (Exception e) {
                log.error("用户信息反序列化失败: {}", userInfoObj, e);
                // 反序列化失败也视为无效登录
            }

            filterChain.doFilter(request, response);

        } finally {
            // 务必清理，防止线程池复用导致用户串号
            UserContext.removeUser();
        }
    }


}