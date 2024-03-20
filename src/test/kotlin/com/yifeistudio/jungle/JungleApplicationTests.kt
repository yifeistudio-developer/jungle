package com.yifeistudio.jungle

import com.yifeistudio.jungle.config.JungleProperties
import com.yifeistudio.space.unit.util.Jsons
import jakarta.annotation.Resource
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.env.Environment


@SpringBootTest
class JungleApplicationTests {

	private val logger: Logger = LoggerFactory.getLogger(this::class.java)

	@Resource
	private lateinit var jungleProperties: JungleProperties

	@Resource
	private lateinit var environment: Environment


	@Test
	fun contextLoads() {
		logger.info("local server port = {}", environment.getProperty("server.port"))
		logger.info("load jungle configuration {}", Jsons.stringify(jungleProperties).get())
	}

}
