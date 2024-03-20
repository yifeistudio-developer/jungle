package com.yifeistudio.jungle

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JungleApplication

fun main(args: Array<String>) {
	runApplication<JungleApplication>(*args).registerShutdownHook()
}
