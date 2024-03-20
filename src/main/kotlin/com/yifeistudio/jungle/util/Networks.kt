package com.yifeistudio.jungle.util

import java.net.Inet4Address
import java.net.NetworkInterface

object Networks {

    /**
     * 获取本机IP地址
     */
    fun localIpAddress(ipPrefer: String = ""): String {
        val networkInterfaces = NetworkInterface.getNetworkInterfaces()
        while (networkInterfaces.hasMoreElements()) {
            val networkInterface = networkInterfaces.nextElement()
            val inetAddresses = networkInterface.inetAddresses
            while (inetAddresses.hasMoreElements()) {
                val inetAddress = inetAddresses.nextElement()
                if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                    val hostAddress = inetAddress.hostAddress
                    if (ipPrefer == "") {
                        return hostAddress
                    }
                    if (hostAddress.startsWith(ipPrefer)) {
                        return hostAddress
                    }
                }
            }
        }
        return ""
    }

}