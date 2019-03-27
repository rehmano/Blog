package com.app.blog.dao

import org.jetbrains.exposed.sql.Table

object UsersTable : Table("users") {
    val name = varchar("username", 50)
    val pass = varchar("password",200)
}

