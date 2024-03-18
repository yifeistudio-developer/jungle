package com.yifeistudio.jungle.handler

import lombok.extern.slf4j.Slf4j
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

@Slf4j
class SystemMessageHandler : WebSocketHandler {
    override fun handle(session: WebSocketSession): Mono<Void> {
        println("xxxx")
        session.send {  }
        return Mono.empty()
    }

}