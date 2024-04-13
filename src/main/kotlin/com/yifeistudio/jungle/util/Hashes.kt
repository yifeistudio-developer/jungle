package com.yifeistudio.jungle.util

import com.yifeistudio.space.unit.util.Jsons
import java.security.MessageDigest

object Hashes {

    @OptIn(ExperimentalStdlibApi::class)
    fun md5(any: Any): String {
        val instance = MessageDigest.getInstance("MD5")
        val jsonStr = Jsons.stringify(any).get()
        instance.update(jsonStr.toByteArray())
        return instance.digest().toHexString()
    }
}