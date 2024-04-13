package com.yifeistudio.jungle

import com.yifeistudio.jungle.mapper.EventMapper
import com.yifeistudio.jungle.model.ClusterProfile
import com.yifeistudio.jungle.model.database.EventDO
import com.yifeistudio.space.unit.util.Jsons
import jakarta.annotation.Resource
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

/**
 * 事件相关测试
 */
@SpringBootTest
class EventTests {

    @Resource
    private lateinit var eventMapper: EventMapper<ClusterProfile>

    @Test
    fun contextLoads() {

    }

    @Test
    fun insertTest() {
        val event = EventDO<ClusterProfile>()
        event.sign = "xxxxxxxxx"
        event.payload = ClusterProfile(1)
        eventMapper.insert(event)
    }

    @Test
    fun selectTest() {
        val selectById = eventMapper.selectById(5)
        println(Jsons.stringify(selectById.payload).get())
    }

    @Test
    fun deleteTest() {
        eventMapper.deleteById(2)
    }


}