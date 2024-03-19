package com.yifeistudio.jungle.adapter.impl

import com.yifeistudio.jungle.adapter.RegistrationManager
import org.springframework.stereotype.Component

/**
 * 使用Redis实现节点及用户关系注册中心
 */
@Component
class RedisRegistrationManager : RegistrationManager {

    /**
     * 获取所有伙伴信息
     */
    override fun listPeer(): List<String> {
        TODO("Not yet implemented")
    }

    /**
     * 注册
     *
     * 有新的连接建立时向注册中心注册用户关系
     */
    override fun register() {
        TODO("Not yet implemented")
    }

    /**
     * 注销节点
     *
     * 服务下线或不可用时向注册中心注销自己
     */
    override fun deregister() {
        TODO("Not yet implemented")
    }

    /**
     * 获取用户关系
     */
    override fun mapUserRelation(vararg userIds: String): Map<String, String> {
        TODO("Not yet implemented")
    }

}