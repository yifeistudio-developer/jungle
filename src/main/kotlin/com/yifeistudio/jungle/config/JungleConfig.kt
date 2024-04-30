package com.yifeistudio.jungle.config

import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

/**
 * 应用自定义配置
 */
@Configuration
@MapperScan("com.yifeistudio.jungle.mapper")
@EnableConfigurationProperties(JungleProperties::class)
internal class JungleConfig {

}
///~