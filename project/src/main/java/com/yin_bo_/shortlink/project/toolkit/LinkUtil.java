package com.yin_bo_.shortlink.project.toolkit;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.yin_bo_.shortlink.project.common.constant.RedisCacheConstant.DEFAULT_SHORT_LINK_CACHE_TTL;

/**
 * 短链接工具类
 */
public class LinkUtil {


    /**
     * 短链接缓存有效期工具
     * @param validDate 短链接有效期
     * @return 缓存 TTL
     */
    public static long getLinkCacheValidDate(LocalDateTime validDate){
        //这里使用 stream 流，有有效期的短链接的缓存TTL就是他的有效期 ，而永久短链接的缓存TTL设置成一个月
        return Optional.ofNullable(validDate)
                .map(each -> Math.max(0L, Duration.between(LocalDateTime.now(), each).toMillis()))
                .orElse(DEFAULT_SHORT_LINK_CACHE_TTL);
    }
}
