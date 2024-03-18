package com.yifeistudio.jungle.handler

import com.yifeistudio.jungle.service.PeerMessageService
import lombok.extern.slf4j.Slf4j
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

@Slf4j
class PeerMessageHandler(peerMessageService: PeerMessageService) : WebSocketHandler {
    override fun handle(session: WebSocketSession): Mono<Void> {
        return Mono.empty()
    }
}