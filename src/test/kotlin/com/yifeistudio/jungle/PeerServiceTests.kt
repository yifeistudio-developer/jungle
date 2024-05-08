package com.yifeistudio.jungle

import com.yifeistudio.space.unit.util.Jsons
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime

@SpringBootTest
class PeerServiceTests {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @Test
    fun contextLoads() {
        val obj = mapOf(Pair("publishTime", LocalDateTime.now()))
        logger.info("test result: {}", Jsons.stringifyAndGet(obj))
    }

}