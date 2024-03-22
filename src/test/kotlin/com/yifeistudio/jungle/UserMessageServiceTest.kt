package com.yifeistudio.jungle

import com.yifeistudio.jungle.service.MessageService
import jakarta.annotation.Resource
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.web.reactive.socket.WebSocketSession
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient
import reactor.core.publisher.Mono
import java.net.URI

/**
 * 用户聊天消息测试
 */
@SpringBootTest
class UserMessageServiceTest {

    val client = ReactorNettyWebSocketClient()

    @Resource
    private lateinit var messageService: MessageService

//    private var webSocketSession: WebSocketSession

    @Test
    fun chatTest() {
        val url = URI("ws://localhost:43325/user-endpoint/message")
        var webSocketSession: WebSocketSession? = null
        client.execute(url) {
                session ->
            webSocketSession = session
            Mono.empty()
        }.block()
        println(webSocketSession!!.isOpen)
        var message = readlnOrNull()
        while (message != "" && webSocketSession!!.isOpen) {
            webSocketSession!!.send(Mono.just(webSocketSession!!.textMessage(message!!))).subscribe()
            message = readlnOrNull()
        }
    }
}