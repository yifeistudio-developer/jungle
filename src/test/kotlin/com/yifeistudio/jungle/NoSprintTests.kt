package com.yifeistudio.jungle

import com.yifeistudio.jungle.util.Hashes
import com.yifeistudio.jungle.util.Networks
import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono

class NoSprintTests {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @Test
    fun networkUtilTest() {
        logger.info(Networks.localIpAddress())
    }

    @Test
    fun fluxTest() {
        val coroutineScope = CoroutineScope(Dispatchers.Default)
        coroutineScope.launch {
            for (i: Int in 1..10) {
                delay(1000)
                println("xxx")
            }
        }
        readlnOrNull()
        coroutineScope.cancel()
    }

    @Test
    fun monoTest() {
        Mono.just("abc").doOnNext { println(it) }.then(Mono.just("efg")).doOnNext { println(it) }.then()
        Thread.sleep(50000)
    }

    @Test
    fun hashTest() {
        println(Hashes.md5("abc"))
    }

}