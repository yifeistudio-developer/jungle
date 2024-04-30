package com.yifeistudio.jungle.adapter.impl

import com.alibaba.cloud.nacos.NacosDiscoveryProperties
import com.yifeistudio.jungle.adapter.RegistrationManager
import com.yifeistudio.jungle.model.Peer
import jakarta.annotation.Resource
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.client.discovery.DiscoveryClient
import org.springframework.stereotype.Component


@Component
class NacosRegistrationManager : RegistrationManager {

    @Value("\${spring.application.name}")
    private lateinit var applicationName: String

    @Resource
    private lateinit var discoveryClient: DiscoveryClient

    @Resource
    private lateinit var nacosDiscoveryProperties: NacosDiscoveryProperties

    /**
     * 获取所有伙伴信息
     */
    override fun peers(): Set<String> {
        val allInstances = discoveryClient.getInstances(applicationName)
        val ip = nacosDiscoveryProperties.ip
        val port = nacosDiscoveryProperties.port
        println(Peer(ip, port))
        return emptySet()
    }

}