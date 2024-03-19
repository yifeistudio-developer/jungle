package com.yifeistudio.jungle.handler

import com.yifeistudio.jungle.adapter.RegistrationManager
import com.yifeistudio.jungle.service.UserMessageService
import jakarta.annotation.Resource
import lombok.extern.slf4j.Slf4j
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

@Slf4j
class UserMessageHandler(userMessageService: UserMessageService) : WebSocketHandler {


    @Resource
    private lateinit var registrationManager: RegistrationManager


    override fun handle(session: WebSocketSession): Mono<Void> {

        val input = session.receive().map {
            println(it.payloadAsText)
            session.textMessage("echo: ${it.payloadAsText}")
        }
        return session.send(input)
    }


}