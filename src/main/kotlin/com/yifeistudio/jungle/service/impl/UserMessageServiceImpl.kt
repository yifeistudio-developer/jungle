package com.yifeistudio.jungle.service.impl

import com.yifeistudio.jungle.service.UserMessageService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.reactive.socket.WebSocketSession
import java.util.concurrent.ConcurrentHashMap

/**
 * 用户消息
 */
@Service
class UserMessageServiceImpl : UserMessageService {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 本地用户关系映射
     */
    private val localRelationMap: MutableMap<String, String> = ConcurrentHashMap()

    /**
     * 本地会话缓存
     */
    private val sessionMap: MutableMap<Long, WebSocketSession> = ConcurrentHashMap()

}