package com.yifeistudio.jungle

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * Jungle 基于spring-webflux 的IM中间件
 */
@SpringBootApplication
class JungleApplication

fun main(args: Array<String>) {
	runApplication<JungleApplication>(*args).registerShutdownHook()
}
///~