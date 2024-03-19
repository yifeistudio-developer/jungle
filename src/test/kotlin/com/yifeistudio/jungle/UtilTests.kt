package com.yifeistudio.jungle

import com.yifeistudio.jungle.util.Networks
import org.junit.jupiter.api.Test

class UtilTests {


    @Test
    fun localIpAddressTest() {
        val localIpAddress = Networks.localIpAddress()
        println(localIpAddress)
    }
}