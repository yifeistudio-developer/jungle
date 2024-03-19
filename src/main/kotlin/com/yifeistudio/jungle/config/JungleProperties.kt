package com.yifeistudio.jungle.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jungle")
class JungleProperties {

    /**
     * 指定IP地址
     */
    var ipAddress: String = ""







}