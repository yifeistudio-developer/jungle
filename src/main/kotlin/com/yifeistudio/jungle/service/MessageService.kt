package com.yifeistudio.jungle.service

import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono


interface MessageService {

    /**
     * 处理用户消息
     */
    fun handle(session: WebSocketSession): Mono<Void>



}