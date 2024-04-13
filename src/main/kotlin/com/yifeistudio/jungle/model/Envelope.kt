package com.yifeistudio.jungle.model

import java.io.Serializable
import java.time.LocalDateTime

data class Envelope<T>(
    var type: EnvelopeType,
    var item: T
) : Serializable {
    companion object {
        fun <R> of(message: Message<R>): Envelope<Message<R>> {
            return Envelope(EnvelopeType.MESSAGE, message)
        }
        fun <R> of(event: Event<R>): Envelope<Event<R>> {
            if (event.publishTime == null) {
                event.publishTime = LocalDateTime.now()
            }
            if (event.attr == null) {
                event.attr = 0
            }
            if (event.origin == null) {
                event.origin = EventOrigin.SYSTEM
            }
            return Envelope(EnvelopeType.MESSAGE, event)
        }
    }
}