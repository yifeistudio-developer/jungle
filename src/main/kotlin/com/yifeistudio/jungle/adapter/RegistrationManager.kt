package com.yifeistudio.jungle.adapter

import com.yifeistudio.jungle.model.Peer


/**
 * 集群注册管理
 */
interface RegistrationManager {

    /**
     * 获取所有伙伴信息
     */
    fun peers(): Set<Peer>

}
///~