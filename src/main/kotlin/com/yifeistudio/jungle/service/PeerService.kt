package com.yifeistudio.jungle.service

import com.yifeistudio.jungle.model.ClusterProfile
import reactor.core.publisher.Mono

interface PeerService {

    /**
     * 启动
     */
    fun start(): Mono<Void>

    /**
     * 端开与伙伴的连接
     */
    fun stop(): Mono<Void>


    /**
     * 获取当前集群概览信息
     */
    fun profile(): ClusterProfile

}