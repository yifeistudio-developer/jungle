package com.yifeistudio.jungle.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

/**
 * 应用自定义配置
 */
@Configuration
@EnableConfigurationProperties(JungleProperties::class)
class JungleConfig {

}
///~