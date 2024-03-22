package com.yifeistudio.jungle.handler

import com.yifeistudio.jungle.service.MessageService
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

class UserMessageHandler(private val messageService: MessageService) : WebSocketHandler {

    override fun handle(session: WebSocketSession): Mono<Void> {
        return messageService.handle(session)
    }

}