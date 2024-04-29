package com.yifeistudio.jungle.adapter.impl

import com.alibaba.nacos.api.annotation.NacosInjected
import com.alibaba.nacos.api.naming.NamingService
import com.yifeistudio.jungle.adapter.RegistrationManager
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component


@Component
class NacosRegistrationManager : RegistrationManager {

    @Value("\${spring.application.name}")
    private lateinit var applicationName: String

    @NacosInjected
    private lateinit var namingService: NamingService


    /**
     * 获取所有伙伴信息
     */
    override fun peers(): Set<String> {
        val allInstances = namingService.getAllInstances(applicationName)
        println(allInstances)
        return emptySet()
    }

    /**
     * 注册
     *
     * 有新的连接建立时向注册中心注册用户关系
     */
    override fun register(marker: String) {
        TODO("Not yet implemented")
    }

    /**
     * 注销节点
     *
     * 服务下线或不可用时向注册中心注销自己
     */
    override fun deregister(marker: String) {
        TODO("Not yet implemented")
    }

    /**
     * 获取用户关系
     */
    override fun mapUserRelation(vararg userIds: String): Map<String, String> {
        TODO("Not yet implemented")
    }


}