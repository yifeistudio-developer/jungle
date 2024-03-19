package com.yifeistudio.jungle.model

import java.time.LocalDateTime

abstract class Message<T> {

    var id: Long? = null

    var seqId: Long? = null

    var senderId: Long? = null

    var type: MessageType? = null

    var attr: Int? = null

    var content: T? = null

    var pubTime: LocalDateTime? = null


}