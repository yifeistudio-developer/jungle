package com.yifeistudio.jungle.model

import com.yifeistudio.space.unit.util.Bits
import java.time.LocalDateTime

class Message<T> {

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
    var content: T? = null

    // 发送时间
    var pubTime: LocalDateTime? = null

    companion object {

        /**
         * 连接信息
         */
        fun connectMessage(marker: String): Message<String> {
            val message = Message<String>()
            message.attr = Bits.add(0, MessageAttr.connect)
            message.content = marker
            return message
        }


    }
}
