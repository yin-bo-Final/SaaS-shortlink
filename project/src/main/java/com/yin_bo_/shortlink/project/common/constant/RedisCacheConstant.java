package com.yin_bo_.shortlink.project.common.constant;


/**
 * 中台 Redis 缓存常量类
 */
public class RedisCacheConstant {

    /**
     * 短链接跳转前缀 Key
     */
    public static final String GOTO_SHORT_LINK_KEY = "short-link-goto:%s";



    /**
     * 短链接跳转分布式锁前缀 Key
     */
    public static final String LOCK_GOTO_SHORT_LINK = "short-link_goto:%s_lock";


    /**
     * 短链接 TTL 有效期  这里设置为一天
     */
    public static final int GOTO_SHORT_LINK_TTL = 86400;


    /**
     * 短链接空值缓存TTL  这里设置为60s
     */
    public static final int GOTO_SHORT_LINK_BLANK_TTL = 60;
}
