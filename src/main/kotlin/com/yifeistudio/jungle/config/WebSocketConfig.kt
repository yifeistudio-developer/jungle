package com.yifeistudio.jungle.config

import com.yifeistudio.jungle.handler.EnvelopeHandler
import com.yifeistudio.jungle.service.MessageService
import com.yifeistudio.jungle.service.PeerService
import jakarta.annotation.Resource
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter

/**
 * WebSocket配置
 */
@Configuration
internal class WebSocketConfig {

    @Resource
    private lateinit var peerService: PeerService

    @Resource
    private lateinit var messageService: MessageService

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 注册 WebSocket Handler Mapping
     */
    @Bean
    fun handlerMapping(): HandlerMapping {
        val handlerMap = mapOf(
            "/user-endpoint/message" to EnvelopeHandler(messageService),
            "/peer-endpoint/message" to EnvelopeHandler(peerService)
        )
        return SimpleUrlHandlerMapping(handlerMap, -1)
    }

    /**
     * 适配器配置
     */
    @Bean
    fun handlerAdapter() = WebSocketHandlerAdapter()

}
///～