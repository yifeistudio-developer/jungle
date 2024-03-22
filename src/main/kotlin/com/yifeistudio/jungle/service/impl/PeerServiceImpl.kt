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
import kotlinx.coroutines.*
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

    private var localMarker: String = ""

    private val client = ReactorNettyWebSocketClient()

    val coroutineScope = CoroutineScope(Dispatchers.Default)

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
    override fun connect(host: String, port: Int): Mono<Void> {
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
    override fun launch(marker: String): Mono<Void> {
        this.localMarker = marker
        coroutineScope.launch {
            while (isActive) {
                logger.info("coroutine scope launched. start executing tasks")
                try {
                    keepAlive()
                    delay(5000)
                } catch (exp: Throwable) {
                    logger.error("keep alive error. - ", exp)
                }
            }
        }
        return Mono.empty()
    }


    /**
     * 获取当前集群概览信息
     */
    override fun profile(): ClusterProfile {
        return ClusterProfile(registrationManager.listPeer().size)
    }

    /**
     * 保活协程
     */
    private fun keepAlive() {
        // 同步注册中心伙伴信息
        for (item in peerSessionCache.iterator()) {
            if (!item.value.isOpen) {
                // 清理掉断开的连接
                peerSessionCache.remove(item.key)
            }
        }
        val remotePeers = registrationManager.listPeer()
        val localPeers = peerSessionCache.keys
        val newPeers = remotePeers.filter { !localPeers.contains(it) && it != localMarker }
        // 与新伙伴建立连接
        val all: List<Mono<Void>> = newPeers.map {
            logger.info("detected new peer: $it try to connect to it")
            val trips = it.split("@")
            connect(trips[0], trips[1].toInt())
        }
        Flux.fromIterable(all).concatMap { it }.blockLast()
    }


    /**
     * 断开与伙伴的连接
     *
     * -取消保活协程
     * -断开与伙伴的连接
     */
    @PreDestroy
    fun stop() {
        if (coroutineScope.isActive) {
            coroutineScope.cancel("server is stopped.")
        }
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
}
///～