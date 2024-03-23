package com.yifeistudio.jungle.handler

import com.yifeistudio.jungle.service.PeerService
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

class PeerMessageHandler(private val peerService: PeerService) : WebSocketHandler {

    override fun handle(session: WebSocketSession): Mono<Void> {
        // do something previously
        peerService.handle(session)
        // 挂起保持连接
        return Mono.never()
    }
}