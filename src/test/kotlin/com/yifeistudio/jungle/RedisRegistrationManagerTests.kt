package com.yifeistudio.jungle

import com.yifeistudio.jungle.adapter.impl.RedisRegistrationManager
import com.yifeistudio.space.unit.util.Jsons
import jakarta.annotation.Resource
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.RedisTemplate

/**
 * Redis 测试
 */
@SpringBootTest
class RedisRegistrationManagerTests {

    @Resource
    private lateinit var redisTemplate: RedisTemplate<String, String>

    @Resource
    private lateinit var redisRegistrationManager: RedisRegistrationManager

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @Test
    fun contextLoads() {

    }

    @Test
    fun listPeerTest() {
        redisTemplate.delete("ACTIVE_PEER_CACHE_KEY")
        val peers = redisRegistrationManager.peers()
        logger.info("test result: {}", Jsons.stringify(peers).get())
    }

    @Test
    fun registrationTest() {
        redisRegistrationManager.register("xxxxxxxxx")
    }

}