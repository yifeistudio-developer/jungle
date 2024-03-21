package com.yifeistudio.jungle.handler

import com.yifeistudio.jungle.adapter.RegistrationManager
import com.yifeistudio.jungle.service.MessageService
import jakarta.annotation.Resource
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

class UserMessageHandler(messageService: MessageService) : WebSocketHandler {


    @Resource
    private lateinit var registrationManager: RegistrationManager

    override fun handle(session: WebSocketSession): Mono<Void> {

        val handshakeInfo = session.handshakeInfo
        println(handshakeInfo)
        val input = session.receive().map {
            println(it.payloadAsText)
            session.textMessage("echo: ${it.payloadAsText}")
        }
        return session.send(input)
    }

}