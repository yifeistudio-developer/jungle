package com.yifeistudio.jungle.config

import com.yifeistudio.jungle.handler.PeerMessageHandler
import com.yifeistudio.jungle.handler.UserMessageHandler
import com.yifeistudio.jungle.service.PeerMessageService
import com.yifeistudio.jungle.service.UserMessageService
import jakarta.annotation.Resource
import lombok.extern.slf4j.Slf4j
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter

@Slf4j
@Configuration
class WebSocketConfig {

    @Resource
    private lateinit var userMessageService: UserMessageService

    @Resource
    private lateinit var peerMessageService: PeerMessageService

    /**
     * 注册 WebSocket Handler Mapping
     */
    @Bean
    fun handlerMapping(): HandlerMapping {
        val handlerMap = mapOf(
            "/user-endpoint/message" to UserMessageHandler(userMessageService),
            "/peer-endpoint/message" to PeerMessageHandler(peerMessageService)
        )
        return SimpleUrlHandlerMapping(handlerMap, -1)
    }

    @Bean
    fun handlerAdapter() = WebSocketHandlerAdapter()
}