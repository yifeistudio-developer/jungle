package com.yifeistudio.jungle.handler

import com.yifeistudio.jungle.model.Envelope
import com.yifeistudio.jungle.model.EnvelopeType
import com.yifeistudio.jungle.model.Event
import com.yifeistudio.jungle.model.Message
import com.yifeistudio.space.unit.util.Jsons
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

/**
 * 新建处理
 */
class EnvelopeHandler(private val handler: EnvelopeItemHandler) : WebSocketHandler {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    override fun handle(session: WebSocketSession): Mono<Void> {

        // do something previously
        session.receive().doOnNext {
            if (it.type == WebSocketMessage.Type.TEXT) {
                val payloadAsText = it.payloadAsText
                val envelopeOptional = Jsons.parse(payloadAsText, Envelope::class.java)
                if (!envelopeOptional.isPresent) {
                    logger.info("handle message. $payloadAsText")
                    return@doOnNext
                }
                val envelope: Envelope<*> = envelopeOptional.get()
                if (envelope.type == EnvelopeType.EVENT) {
                    handler.handle(envelope.item as Event<*>)
                }
                if (envelope.type == EnvelopeType.MESSAGE) {
                    handler.handle(envelope.item as Message<*>)
                }
            }
        }.subscribeOn(Schedulers.immediate()).subscribe()

        // 挂起保持连接
        return Mono.never()
    }
}