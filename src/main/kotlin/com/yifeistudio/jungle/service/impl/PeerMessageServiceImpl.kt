package com.yifeistudio.jungle.service.impl

import com.yifeistudio.jungle.adapter.impl.RedisRegistrationManager
import com.yifeistudio.jungle.service.PeerMessageService
import jakarta.annotation.Resource
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.reactive.socket.WebSocketSession
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient
import java.util.concurrent.ConcurrentHashMap

/**
 * 伙伴管理服务
 */
@Service
class PeerMessageServiceImpl : PeerMessageService {

    private val client = ReactorNettyWebSocketClient()

    @Resource
    private lateinit var registrationManager: RedisRegistrationManager

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 伙伴关系缓存
     */
    private val peerSessionCache: MutableMap<String, WebSocketSession> = ConcurrentHashMap()

    private fun connectPeer(host: String, port: Int) {

    }



}