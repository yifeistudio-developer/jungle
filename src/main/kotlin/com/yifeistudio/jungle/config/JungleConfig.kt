package com.yifeistudio.jungle.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(JungleProperties::class)
class JungleConfig {

}
///~