package com.yifeistudio.jungle.handler

import com.yifeistudio.jungle.model.Event
import com.yifeistudio.jungle.model.Message

interface EnvelopeItemHandler {

    fun handle(event: Event<*>)

    fun handle(message: Message<*>)
}