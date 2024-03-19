package com.yifeistudio.jungle.model.database

import com.baomidou.mybatisplus.annotation.TableName
import com.yifeistudio.jungle.model.MessageStatus
import com.yifeistudio.jungle.model.MessageType
import java.time.LocalDateTime

@TableName("t_message", autoResultMap = true)
class MessageDO {

    /**
     * 物理主键
     */
    var id: Long? = null

    /**
     * 全局序列
     */
    var seqId: Long? = null

    /**
     * 消息签名
     */
    var sign: String? = null

    /**
     * 消息类型
     */
    var type: MessageType? = null

    /**
     * 状态
     */
    var status: MessageStatus? = null

    /**
     * 属性
     */
    var attr: Int? = null

    /**
     * 发送方
     */
    var sender: Long? = null

    /**
     * 接收方
     */
    var receiver: Long? = null

    /**
     * 创建时间
     */
    var createdAt: LocalDateTime? = null

    /**
     * 修改时间
     */
    var modifiedAt: LocalDateTime? = null
}
///～