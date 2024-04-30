package com.yifeistudio.jungle.service.impl

import com.yifeistudio.jungle.adapter.RegistrationManager
import com.yifeistudio.jungle.mapper.EventMapper
import com.yifeistudio.jungle.model.*
import com.yifeistudio.jungle.model.database.EventDO
import com.yifeistudio.jungle.service.PeerService
import com.yifeistudio.jungle.util.Hashes
import com.yifeistudio.space.unit.util.Jsons
import jakarta.annotation.Resource
import kotlinx.coroutines.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
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
internal class PeerServiceImpl : PeerService {

    private var localMarker: String = ""

    @Resource
    private lateinit var eventMapper: EventMapper<String>

    val coroutineScope = CoroutineScope(Dispatchers.Default)

    /**
     * 管理器状态
     */
    private val state: AtomicBoolean = AtomicBoolean(false)

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 客户端配置
     */
    private val builder: WebsocketClientSpec.Builder = WebsocketClientSpec.builder().handlePing(true)

    private val client = ReactorNettyWebSocketClient(HttpClient.create(), builder)

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

    /**
     * 转发消息
     */
    override fun dispatch(userId: Long, message: Message<Any>) {
        val peerMarker: String? = localUserRelCache[userId.toString()]

    }

    /**
     * 处理伙伴消息
     */
    override fun handle(event: Event<*>) {

    }

    override fun handle(message: Message<*>) {

    }

    /**
     * 启动
     */
    override fun launch(marker: String): Mono<Void> {
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
        this.localMarker = marker
        return Mono.empty()
    }


    /**
     * 获取当前集群概览信息
     */
    override fun profile(): ClusterProfile {
        return ClusterProfile(registrationManager.peers().size)
    }

    /**
     * 断开与伙伴的连接
     * - 自我注销
     * - 取消保活协程
     * - 断开与伙伴的连接
     */
    override fun stop() {
        if (!state.get()) {
            return
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
        if (logger.isDebugEnabled) {
            logger.debug("keep alive task is running...")
        }
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
        newPeers.forEach {
            logger.info("detected new peer: $it try to connect to it")
            val trips = it.split("@")
            connect(trips[0], trips[1].toInt())
        }
    }

    /**
     * 连接伙伴
     */
    private fun connect(host: String, port: Int) {
        val url = URI("ws://$host:$port/peer-endpoint/message")
        val marker = "$host@$port"
        val httpHeaders = HttpHeaders()
        httpHeaders.add("control-type", Event::class.simpleName)
        client.execute(url, httpHeaders) { session ->
            logger.info("connected to $marker succeed!")
            onConnected(marker, session)
            Mono.never()
        }.onErrorResume {
            logger.error("connect to $marker failed.")
            Mono.empty()
        }.subscribe()
    }

    private fun onConnected(marker: String, session: WebSocketSession) {
        peerSessionCache[marker] = session
        val event = Event<String>()
        event.payload = marker
        event.sign = Hashes.md5(event.payload!!)
        eventMapper.insert(EventDO.of(event))
        val envelope = Envelope.of(event)
        // publish connect event
        session.send(Mono.just(session.textMessage(Jsons.stringify(envelope).get()))).subscribe()
        session.receive().doOnNext {
            val payloadAsText = it.payloadAsText
            val rawEvp = Jsons.parse(payloadAsText, Envelope::class.java).get()
            if (rawEvp.type == EnvelopeType.EVENT) {

            }
            if (rawEvp.type == EnvelopeType.MESSAGE) {

            }
        }.subscribe()
    }

}
///～