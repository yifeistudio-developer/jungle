package com.yifeistudio.jungle.service.impl

import com.yifeistudio.jungle.service.UserMessageService
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Service
import org.springframework.web.reactive.socket.WebSocketSession
import java.util.concurrent.ConcurrentHashMap

/**
 * 用户消息
 */
@Slf4j
@Service
class UserMessageServiceImpl : UserMessageService {

    /**
     * 本地用户关系映射
     */
    private val localRelationMap: MutableMap<String, String> = ConcurrentHashMap()

    /**
     * 本地会话缓存
     */
    private val sessionMap: MutableMap<Long, WebSocketSession> = ConcurrentHashMap()





}