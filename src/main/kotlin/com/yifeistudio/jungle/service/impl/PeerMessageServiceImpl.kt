package com.yifeistudio.jungle.service.impl

import com.yifeistudio.jungle.service.PeerMessageService
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Service
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient
import java.util.concurrent.ConcurrentHashMap

/**
 * 伙伴管理服务
 */
@Slf4j
@Service
class PeerMessageServiceImpl : PeerMessageService {

    /**
     * 伙伴关系缓存
     */
    private val peerClientCache: Map<String, ReactorNettyWebSocketClient> = ConcurrentHashMap()




}