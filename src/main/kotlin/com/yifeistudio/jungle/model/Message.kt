package com.yifeistudio.jungle.model

import java.io.Serializable
import java.time.LocalDateTime

class Message<T> : Serializable {

    // 消息ID
    var id: Long? = null

    // 消息序列ID
    var seqId: Long? = null

    // 消息发送方
    var senderId: Long? = null

    // 消息类型
    var type: MessageType? = null

    // 消息属性
    var attr: Int? = null

    // 消息内容
    var payload: T? = null

    // 发送时间
    var pubTime: LocalDateTime? = null

    companion object {

    }
}
