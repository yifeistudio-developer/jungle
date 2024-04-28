package com.yifeistudio.jungle.adapter.impl

import com.yifeistudio.jungle.adapter.RegistrationManager

class NacosRegistrationManager : RegistrationManager {

    /**
     * 获取所有伙伴信息
     */
    override fun peers(): Set<String> {
        TODO("Not yet implemented")
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