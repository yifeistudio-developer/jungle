package com.yifeistudio.jungle.config

import com.yifeistudio.jungle.model.RunningMode
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jungle")
class JungleProperties {

    /**
     * 运行模式：默认单节点模式
     */
    var mode: RunningMode = RunningMode.STANDALONE

    /**
     * 伙伴关系
     */
    var cluster: Cluster = Cluster()

    class Cluster {

        /**
         * 指定IP地址
         */
        var ipPrefer: String = ""


    }

}