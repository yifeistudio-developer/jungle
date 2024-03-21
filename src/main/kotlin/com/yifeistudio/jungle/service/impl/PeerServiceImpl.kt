package com.yifeistudio.jungle.service.impl

import com.yifeistudio.jungle.adapter.impl.RedisRegistrationManager
import com.yifeistudio.jungle.model.ClusterProfile
import com.yifeistudio.jungle.model.Message
import com.yifeistudio.jungle.model.MessageAttr
import com.yifeistudio.jungle.service.PeerService
import com.yifeistudio.space.unit.util.Bits
import com.yifeistudio.space.unit.util.Jsons
import jakarta.annotation.PreDestroy
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

    private var localMarker: String = ""

    @Resource
    private lateinit var registrationManager: RedisRegistrationManager

    /**
     * 伙伴关系缓存
     */
    private val peerSessionCache: MutableMap<String, WebSocketSession> = ConcurrentHashMap()

    /**
     * 本地用户关系缓存
     */
    private val localUserRelCache: MutableMap<String, String> = ConcurrentHashMap<String, String>()


    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 连接伙伴
     */
    override fun connect(host: String, port: Int):Mono<Void> {
        val url = URI("ws://$host:$port/peer-endpoint/message")
        return client.execute(url) { session ->
            val connectMessage = Message.connectMessage(marker = localMarker)
            session.send(Mono.just(session.textMessage(Jsons.stringify(connectMessage).get())))
                .doOnSuccess {
                    val marker = "$host@$port"
                    logger.info("peer: $marker connected.")
                    peerSessionCache[marker] = session
                }
        }
    }

    /**
     * 转发消息
     */
    override fun dispatch(userId: Long, message: Message<Any>) {

    }

    /**
     * 处理伙伴消息
     */
    override fun handle(session: WebSocketSession): Mono<Void> {
        val handshakeInfo = session.handshakeInfo
        logger.info("handle message from ${handshakeInfo.remoteAddress?.address}")
        return session.receive().doOnNext { msg ->
            logger.info("handle message ${msg.type}, ${msg.payloadAsText}")
            val message = Jsons.parse(msg.payloadAsText, Message::class.java).get()
            if (Bits.hasMask(message.attr!!, MessageAttr.connect)) {
                // 连接消息
                val marker = message.content as String
                peerSessionCache[marker] = session
                logger.info("connected with : $marker")
            }
        }.then()
    }

    /**
     * 启动
     */
    override fun start(marker: String): Mono<Void> {
        this.localMarker = marker
        val markers = registrationManager.listPeer()

        return Mono.empty()
    }

    /**
     * 端开与伙伴的连接
     */
    @PreDestroy
    fun stop() {
        val transform: (WebSocketSession) -> Mono<Void> = { webSocketSession ->
            if (webSocketSession.isOpen) {
                logger.warn("detect active websocket-session, it's being closed.")
                webSocketSession.close()
            } else {
                Mono.empty()
            }
        }
        val all: List<Mono<Void>> = peerSessionCache.values.map(transform)
        Flux.fromIterable(all).concatMap { it }.doOnNext {
            peerSessionCache.clear()
        }.blockLast()
    }

    /**
     * 获取当前集群概览信息
     */
    override fun profile(): ClusterProfile {
        return ClusterProfile(registrationManager.listPeer().size)
    }
}
///～