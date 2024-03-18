package com.yifeistudio.jungle.config

import com.yifeistudio.jungle.handler.SystemMessageHandler
import com.yifeistudio.jungle.handler.UserMessageHandler
import lombok.extern.slf4j.Slf4j
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter

@Slf4j
@Configuration
class WebSocketConfig  {

    @Bean
    fun handlerMapping(): HandlerMapping {
        val handlerMap = mapOf(
            "/user-endpoint/message" to UserMessageHandler(),
            "/system-endpoint/message" to SystemMessageHandler()
        )
        return SimpleUrlHandlerMapping(handlerMap, -1)
    }

    @Bean
    fun handlerAdapter() = WebSocketHandlerAdapter()


}