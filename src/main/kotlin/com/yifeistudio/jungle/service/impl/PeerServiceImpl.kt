package com.yifeistudio.jungle.service.impl

import com.yifeistudio.jungle.adapter.impl.RedisRegistrationManager
import com.yifeistudio.jungle.model.ClusterProfile
import com.yifeistudio.jungle.service.PeerService
import jakarta.annotation.Resource
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.reactive.socket.WebSocketSession
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.net.URI
import java.util.concurrent.ConcurrentHashMap

/**
 * 伙伴管理服务
 */
@Service
class PeerServiceImpl : PeerService {

    private val client = ReactorNettyWebSocketClient()

    @Resource
    private lateinit var registrationManager: RedisRegistrationManager

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 伙伴关系缓存
     */
    private val peerSessionCache: MutableMap<String, WebSocketSession> = ConcurrentHashMap()




    /**
     * 连接伙伴
     */
    private fun connect(host: String, port: Int):Mono<Void> {
        val url = URI("ws://$host:$port/peer-endpoint/message")
        return client.execute(url) { session ->
            // 本地缓存
            peerSessionCache["$host@$port"] = session
            session.send(Mono.just(session.textMessage("hello world")))
                .thenMany(session.receive())
                .doOnNext { message -> println(message.payloadAsText) }
                .then()
        }
    }

    /**
     * 启动
     */
    override fun start(): Mono<Void> {
        return Mono.empty()
    }

    /**
     * 端开与伙伴的连接
     */
    override fun stop(): Mono<Void> {
        val transform: (WebSocketSession) -> Mono<Void> = { webSocketSession ->
            if (webSocketSession.isOpen) {
                webSocketSession.close()
            } else {
                Mono.empty()
            }
        }
        val all: List<Mono<Void>> = peerSessionCache.values.map(transform)
        return Flux.fromIterable(all).concatMap { it }.doOnNext {
            peerSessionCache.clear()
        }.then()
    }

    /**
     * 获取当前集群概览信息
     */
    override fun profile(): ClusterProfile {
        return ClusterProfile(registrationManager.listPeer().size)
    }
}
///～