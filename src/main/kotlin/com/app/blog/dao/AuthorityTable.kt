package com.app.blog.dao

import org.jetbrains.exposed.sql.Table

object AuthorityTable : Table("authorities") {
    val name = varchar("username", 50)
    val authority = varchar("authority",50)
}
