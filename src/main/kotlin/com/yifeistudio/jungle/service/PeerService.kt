package com.yifeistudio.jungle.service

import com.yifeistudio.jungle.handler.EnvelopeItemHandler
import com.yifeistudio.jungle.model.ClusterProfile
import com.yifeistudio.jungle.model.Message
import reactor.core.publisher.Mono

/**
 * 伙伴关系管理
 */
interface PeerService : EnvelopeItemHandler {

    /**
     * 启动
     */
    fun launch(marker: String): Mono<Void>

    /**
     * 断开与伙伴的连接
     * - 自我注销
     * - 取消保活协程
     * - 断开与伙伴的连接
     */
    fun stop()

    /**
     * 获取当前集群概览信息
     */
    fun profile(): ClusterProfile

    /**
     * 转发消息
     */
    fun dispatch(userId: Long, message: Message<Any>)

}