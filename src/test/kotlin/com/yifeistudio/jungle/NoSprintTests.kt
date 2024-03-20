package com.yifeistudio.jungle

import com.yifeistudio.jungle.util.Networks
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class NoSprintTests {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @Test
    fun networkUtilTest() {
        logger.info(Networks.localIpAddress())
    }

}