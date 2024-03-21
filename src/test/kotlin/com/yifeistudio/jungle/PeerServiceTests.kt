package com.yifeistudio.jungle

import com.yifeistudio.jungle.service.PeerService
import jakarta.annotation.Resource
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class PeerServiceTests {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @Resource
    private lateinit var peerService: PeerService

    @Test
    fun contextLoads() {
    }


    /**
     * 连接伙伴测试
     */
    @Test
    fun connectTest() {
        peerService.connect("localhost", 44349)
            .block()

//        doOnError {
//            logger.error("handler connect error: ${it.message}")
//        }.block()

    }
}