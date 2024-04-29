package com.yifeistudio.jungle.config

import com.alibaba.nacos.api.annotation.NacosProperties
import com.alibaba.nacos.spring.context.annotation.discovery.EnableNacosDiscovery
import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

/**
 * 应用自定义配置
 */
@Configuration
@EnableNacosDiscovery(globalProperties = NacosProperties(serverAddr = "127.0.0.1:8848"))
@MapperScan("com.yifeistudio.jungle.mapper")
@EnableConfigurationProperties(JungleProperties::class)
internal class JungleConfig {

}
///~