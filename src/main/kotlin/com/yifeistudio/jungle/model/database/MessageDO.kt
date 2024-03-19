package com.yifeistudio.jungle.model.database

import com.baomidou.mybatisplus.annotation.TableName
import com.yifeistudio.jungle.model.MessageType
import java.time.LocalDateTime

@TableName("t_message", autoResultMap = true)
class MessageDO {

    private var id: Long? = null

    private var seqId: Long? = null

    private var sign: String? = null

    private var type: MessageType? = null

    private var status: Long? = null

    private var attr: Int? = null

    private var sender: Long? = null

    private var reciver: Long? = null

    private var createdAt: LocalDateTime? = null

    private var modifiedAt: LocalDateTime? = null
}