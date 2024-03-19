package com.yifeistudio.jungle

import com.yifeistudio.jungle.config.JungleProperties
import jakarta.annotation.Resource
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest
class JungleApplicationTests {

	private val logger: Logger = LoggerFactory.getLogger(this::class.java)

	@Resource
	private lateinit var jungleProperties: JungleProperties

	@Test
	fun contextLoads() {
		logger.info("load jungle configuration {}", jungleProperties)
	}



}
