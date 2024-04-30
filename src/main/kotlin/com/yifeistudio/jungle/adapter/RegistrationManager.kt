package com.yifeistudio.jungle.adapter

/**
 * 集群注册管理
 */
interface RegistrationManager {

    /**
     * 获取所有伙伴信息
     */
    fun peers(): Set<String>

}
///~