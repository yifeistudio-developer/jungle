package com.yifeistudio.jungle.adapter.impl

import com.yifeistudio.jungle.adapter.RegistrationManager
import jakarta.annotation.Resource
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

/**
 * 使用Redis实现节点及用户关系注册中心
 */
@Component
internal class RedisRegistrationManager : RegistrationManager {

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

    /**
     * 获取所有伙伴信息
     */
    override fun peers(): Set<String> {
        val members: MutableSet<String> = redisTemplate.opsForSet()
            .members(activePeerCacheKey) ?: return emptySet()
        return members.toSet()
    }

    /**
     * 注册
     *
     * 有新的连接建立时向注册中心注册用户关系
     */
    override fun register(marker: String) {
        redisTemplate.opsForSet().add(activePeerCacheKey, marker)
    }

    /**
     * 注销节点
     *
     * 服务下线或不可用时向注册中心注销自己
     */
    override fun deregister(marker: String) {
        redisTemplate.opsForSet().remove(activePeerCacheKey, marker)
    }

    /**
     * 获取用户关系
     */
    override fun mapUserRelation(vararg userIds: String): Map<String, String> {
        // todo 待完善
        var userRelation: MutableList<String> = redisTemplate.opsForHash<String, String>()
            .multiGet(userChannelCacheKey, userIds.toSet())
        redisTemplate.opsForHash<String, String>()
        return emptyMap()
    }
}
///~