package com.yifeistudio.jungle.adapter.impl

import com.yifeistudio.jungle.adapter.CacheManager
import jakarta.annotation.Resource
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

/**
 * 使用Redis实现节点及用户关系注册中心
 */
@Component
internal class RedisCacheManager : CacheManager {

    @Resource
    private lateinit var redisTemplate: RedisTemplate<String, String>

    /**
     * 存活伙伴集合
     */
    private val activePeerCacheKey: String = "ACTIVE_PEER_CACHE_KEY"

    /**
     * 渠道信息缓存
     */
    private val userChannelCacheKey: String = "USER_CHANNEL_CACHE_KEY"

}
///~