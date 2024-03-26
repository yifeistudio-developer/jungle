package com.yifeistudio.jungle

import com.yifeistudio.jungle.mapper.EventMapper
import com.yifeistudio.jungle.model.database.EventDO
import jakarta.annotation.Resource
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

/**
 * 事件相关测试
 */
@SpringBootTest
class EventTests {

    @Resource
    private lateinit var eventMapper: EventMapper

    @Test
    fun contextLoads() {

    }

    @Test
    fun insertTest() {
        val event = EventDO()
        event.sign = "xxx"
        eventMapper.insert(event)
    }

    @Test
    fun selectTest() {
        val selectById = eventMapper.selectById(2)
        println(selectById)
    }

    @Test
    fun deleteTest() {
        eventMapper.deleteById(2)
    }


}