package com.yifeistudio.jungle.service

import com.yifeistudio.jungle.model.ClusterProfile
import com.yifeistudio.jungle.model.Message
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

/**
 * 伙伴关系管理
 */
interface PeerService {

    /**
     * 启动
     */
    fun launch(marker: String): Mono<Void>

    /**
     * 获取当前集群概览信息
     */
    fun profile(): ClusterProfile

    /**
     * 转发消息
     */
    fun dispatch(userId: Long, message: Message<Any>)

    /**
     * 处理伙伴消息
     */
    fun handle(session: WebSocketSession)

}