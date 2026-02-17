package com.yin_bo_.shortlink.admin.common.biz.user;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.yin_bo_.shortlink.admin.common.convention.Result;
import com.yin_bo_.shortlink.admin.common.enums.UserErrorCodeEnum;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

import static com.yin_bo_.shortlink.admin.common.constant.RedisCacheConstant.LOGIN;

/**
 * 用户信息传输过滤器（兼具登录校验功能）
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Order(-100)
public class UserTransmitFilter implements Filter {

    private final StringRedisTemplate stringRedisTemplate;

    @Getter
    private final List<String> IGNORE_URI_PREFIX = Lists.newArrayList(
            "/api/shortlink/v1/admin/user/login",
            "/api/shortlink/v1/admin/isOccupied",
            "/api/shortlink/v1/admin/user/register"
    );

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestPath = request.getRequestURI();

        //放行IGNORE_URI的接口
        //这里代表的是任何具有这些前缀的路径都被匹配，然后放行
        if (IGNORE_URI_PREFIX.stream().anyMatch(requestPath::startsWith)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String username = request.getHeader("username");
            String token = request.getHeader("token");

            // 3. 未传 username 或 token → 未登录
            if (CharSequenceUtil.isBlank(username) || CharSequenceUtil.isBlank(token)) {
                returnUnauthorized(response);
                return;
            }



            // 4. 查询值在Redis 中是否存在
            String redisKey = LOGIN + username;
            Object userInfoObj;
            try {
                userInfoObj = stringRedisTemplate.opsForHash().get(redisKey, token);
            } catch (Exception e) {
                log.error("Redis 查询用户信息异常, username: {}, token: {}", username, token, e);
                returnUnauthorized(response);
                return;
            }

            // 5. token 不存在或过期
            if (userInfoObj == null) {
                returnUnauthorized(response);
                return;
            }

            // 6. 反序列化并设置上下文
            try {
                UserInfoDTO userInfoDTO = JSONUtil.toBean(userInfoObj.toString(), UserInfoDTO.class);
                UserContext.setUser(userInfoDTO);
            } catch (Exception e) {
                log.error("用户信息反序列化失败: {}", userInfoObj, e);
                returnUnauthorized(response);
                return;
            }

            // 7. 校验通过，放行
            filterChain.doFilter(request, response);

        } finally {
            UserContext.removeUser();
        }
    }

    /**
     * 返回未登录错误（401 + 自定义格式）
     */
    private void returnUnauthorized(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");
        Result<Void> result = new Result<Void>()
                .setCode(UserErrorCodeEnum.USER_NOT_LOGINED.code())
                .setMessage(UserErrorCodeEnum.USER_NOT_LOGINED.message());
        response.getWriter().write(JSONUtil.toJsonStr(result));
    }

}