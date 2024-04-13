package com.yifeistudio.jungle.model

import java.io.Serializable
import java.time.LocalDateTime

class Event<T> : Serializable {

    /**
     * 物理主键ID
     */
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
    var payload: T? = null

    /**
     * 发布时间
     */
    var publishTime: LocalDateTime? = null;

    /**
     * 事件来源
     */
    var origin: EventOrigin? = null

}