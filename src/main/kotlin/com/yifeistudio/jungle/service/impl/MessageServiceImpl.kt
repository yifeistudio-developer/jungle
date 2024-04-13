package com.yifeistudio.jungle.service.impl

import com.yifeistudio.jungle.model.Event
import com.yifeistudio.jungle.model.Message
import com.yifeistudio.jungle.service.MessageService
import com.yifeistudio.jungle.service.PeerService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.reactive.socket.WebSocketSession
import java.util.concurrent.ConcurrentHashMap

/**
 * 用户消息
 */
@Service
internal class MessageServiceImpl : MessageService {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    private lateinit var peerService: PeerService

    /**
     * 本地用户关系映射
     */
    private val localRelationMap: MutableMap<String, String> = ConcurrentHashMap()

    /**
     * 本地会话缓存
     */
    private val sessionMap: MutableMap<Long, WebSocketSession> = ConcurrentHashMap()

    /**
     * 处理用户消息
     */
    override fun handle(event: Message<*>) {

    }

    override fun handle(event: Event<*>) {
        TODO("Not yet implemented")
    }

}