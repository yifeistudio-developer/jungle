package com.yifeistudio.jungle.model.database

import com.baomidou.mybatisplus.annotation.*
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler
import com.yifeistudio.jungle.model.Event
import com.yifeistudio.jungle.model.EventOrigin
import com.yifeistudio.jungle.util.Hashes
import org.springframework.beans.BeanUtils
import java.time.LocalDateTime

@TableName("t_event", autoResultMap = true)
class EventDO<T> {

    /**
     * 物理主键ID
     */
    @TableId(type = IdType.AUTO)
    var id: Long? = null

    /**
     * 签名
     */
    var sign: String? = null

    /**
     * 属性
     */
    var attr: Int? = null

    /**
     * 内容
     */
    @TableField(typeHandler = JacksonTypeHandler::class)
    var payload: T? = null

    /**
     * 发布时间
     */
    var publishTime: LocalDateTime? = null;

    /**
     * 事件来源
     */
    var origin: EventOrigin? = null

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    var createdTime: LocalDateTime? = null

    /**
     * 最后修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    var lastModifiedTime: LocalDateTime? = null

    /**
     * 逻辑删除标识
     */
    @TableLogic
    var isDeleted: Boolean? = null


    companion object {
        fun <T> of(event: Event<T>): EventDO<T> {
            val data = EventDO<T>()
            BeanUtils.copyProperties(event, data)
            if (data.sign == null) {
                data.sign = Hashes.md5(event.payload ?: "")
            }
            return data
        }
    }
}