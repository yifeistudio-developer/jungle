package com.yifeistudio.jungle.handler

import com.yifeistudio.jungle.adapter.RegistrationManager
import com.yifeistudio.jungle.service.PeerService
import jakarta.annotation.Resource
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

class PeerMessageHandler(peerMessageService: PeerService) : WebSocketHandler {

    @Resource
    private lateinit var registrationManager: RegistrationManager

    override fun handle(session: WebSocketSession): Mono<Void> {
        return Mono.empty()
    }
}