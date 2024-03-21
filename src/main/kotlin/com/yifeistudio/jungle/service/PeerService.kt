package com.yifeistudio.jungle.service

import com.yifeistudio.jungle.model.ClusterProfile
import com.yifeistudio.jungle.model.Message
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

interface PeerService {

    /**
     * 启动
     */
    fun start(marker: String): Mono<Void>


    /**
     * 获取当前集群概览信息
     */
    fun profile(): ClusterProfile

    /**
     * 连接伙伴
     */
    fun connect(host: String, port: Int): Mono<Void>

    /**
     * 转发消息
     */
    fun dispatch(userId: Long, message: Message<Any>)

    /**
     * 处理伙伴消息
     */
    fun handle(session: WebSocketSession): Mono<Void>

}