package com.yifeistudio.jungle.service.impl

import com.yifeistudio.jungle.service.PeerMessageService
import org.springframework.stereotype.Service
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient
import reactor.core.publisher.Mono
import java.net.URI
import java.util.concurrent.ConcurrentHashMap

/**
 * 伙伴管理服务
 */
@Service
class PeerMessageServiceImpl : PeerMessageService {

    private val client = ReactorNettyWebSocketClient()

    /**
     * 伙伴关系缓存
     */
    private val peerSessionCache: MutableMap<String, WebSocketSession> = ConcurrentHashMap()

    private fun connectPeer(host: String) {

        val uri = URI("ws://$host:8080/user-endpoint/message")
        client.execute(uri, WebSocketHandler { session: WebSocketSession ->
            peerSessionCache[host] = session
            Mono.empty()
        })
    }

}