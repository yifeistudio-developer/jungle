package com.yifeistudio.jungle.config

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler
import org.apache.ibatis.reflection.MetaObject
import org.springframework.stereotype.Component
import java.time.LocalDateTime


/**
 * 数据字段自动填充
 */
@Component
internal class DataMetaObjectHandler : MetaObjectHandler {

    override fun insertFill(metaObject: MetaObject?) {
        strictInsertFill(
            metaObject, "createdTime",
            { LocalDateTime.now() },
            LocalDateTime::class.java
        )
        strictInsertFill(
            metaObject, "lastModifiedTime",
            { LocalDateTime.now() },
            LocalDateTime::class.java
        )
        strictInsertFill(
            metaObject, "isDeleted",
            { false },
            Boolean::class.java
        )
    }

    override fun updateFill(metaObject: MetaObject?) {
        strictUpdateFill(
            metaObject, "lastModifiedTime",
            { LocalDateTime.now() },
            LocalDateTime::class.java
        )
    }

}