package com.yifeistudio.jungle.adapter

/**
 * 集群注册管理
 */
interface RegistrationManager {

    /**
     * 获取所有伙伴信息
     */
    fun listPeer() :List<String>

    /**
     * 注册
     *
     * 有新的连接建立时向注册中心注册用户关系
     */
    fun register()

    /**
     * 注销节点
     *
     * 服务下线或不可用时向注册中心注销自己
     */
    fun deregister()

    /**
     * 获取用户关系
     */
    fun mapUserRelation(vararg userIds: String): Map<String, String>


}