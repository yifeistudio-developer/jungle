package com.yifeistudio.jungle.service.impl

import com.yifeistudio.jungle.adapter.RegistrationManager
import com.yifeistudio.jungle.model.ClusterProfile
import com.yifeistudio.jungle.model.Message
import com.yifeistudio.jungle.service.PeerService
import jakarta.annotation.Resource
import kotlinx.coroutines.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.SmartLifecycle
import org.springframework.stereotype.Service
import org.springframework.web.reactive.socket.HandshakeInfo
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.netty.http.client.HttpClient
import reactor.netty.http.client.WebsocketClientSpec
import java.net.URI
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean

/**
 * 伙伴管理服务
 */
@Service
class PeerServiceImpl : PeerService, SmartLifecycle {

    private var localMarker: String = ""

    private val state: AtomicBoolean = AtomicBoolean(false)

    private val builder: WebsocketClientSpec.Builder = WebsocketClientSpec.builder().handlePing(true).maxFramePayloadLength(1024)

    private val client = ReactorNettyWebSocketClient(HttpClient.create(), builder)

    val coroutineScope = CoroutineScope(Dispatchers.Default)

    @Resource
    private lateinit var registrationManager: RegistrationManager

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
     * 转发消息
     */
    override fun dispatch(userId: Long, message: Message<Any>) {

    }

    /**
     * 处理伙伴消息
     */
    override fun handle(session: WebSocketSession) {
        val handshakeInfo: HandshakeInfo = session.handshakeInfo
        var subProtocol: String? = handshakeInfo.subProtocol

        logger.info("handle message from ${handshakeInfo.remoteAddress?.address}")

        session.receive().doOnNext { msg ->
            // 处理PING 消息
            if (msg.type == WebSocketMessage.Type.PING) {
                val pongMessage = session.pongMessage { f -> f.wrap("PONG".toByteArray()) }
            }
            // 处理连接消息
            if (msg.type == WebSocketMessage.Type.TEXT) {
                logger.info("handle connect message. ${msg.payloadAsText}")
            }
        }.subscribe()
    }

    /**
     * 启动
     */
    override fun launch(marker: String): Mono<Void> {
        start()
        this.localMarker = marker
        registrationManager.register(localMarker)
        return Mono.empty()
    }


    /**
     * 获取当前集群概览信息
     */
    override fun profile(): ClusterProfile {
        return ClusterProfile(registrationManager.peers().size)
    }

    /**
     * 启动
     */
    override fun start() {
        state.compareAndSet(false, true)
        coroutineScope.launch {
            logger.info("coroutine scope launched. start executing tasks")
            while (isActive) {
                try {
                    keepAlive()
                    delay(5000)
                } catch (exp: Throwable) {
                    if (exp is CancellationException) {
                        logger.warn("server is stopping. the execution is canceled")
                    } else {
                        logger.error("keep alive error. - ", exp)
                    }
                }
            }
        }
    }

    /**
     * 关闭自启动
     */
    override fun isAutoStartup(): Boolean {
        return false
    }

    override fun isRunning(): Boolean {
        return state.get()
    }

    override fun getPhase(): Int {
        return 1
    }

    /**
     * 断开与伙伴的连接
     * - 自我注销
     * - 取消保活协程
     * - 断开与伙伴的连接
     */
    override fun stop() {
        // 自我注销
        if (localMarker != "") {
            registrationManager.deregister(localMarker)
        }
        // 停止保活协程
        if (coroutineScope.isActive) {
            coroutineScope.cancel()
        }
        // 断开与伙伴间的连接
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

    //------------------------------------------------------------------------------------------------------------------

    /**
     * 保活协程
     */
    private fun keepAlive() {
        // 同步注册中心伙伴信息
        for (item in peerSessionCache.iterator()) {
            if (!item.value.isOpen) {
                // 清理掉断开的连接
                logger.warn("detect active websocket-session, it's being closed. remove ${item.key}")
                peerSessionCache.remove(item.key)
            }
        }
        val remotePeers = registrationManager.peers()
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
     * 连接伙伴
     */
    private fun connect(host: String, port: Int, deregister:Boolean = false): Mono<Void> {
        val url = URI("ws://$host:$port/peer-endpoint/message")
        return client.execute(url) { session ->
            peerSessionCache["$host@$port"] = session
            Mono.empty()
        }.onErrorResume {
            logger.error("connect failed.")
            registrationManager.deregister("$host@$port")
            Mono.empty()
        }
    }
}
///～