package com.app.blog

import org.jetbrains.exposed.sql.Database
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import javax.sql.DataSource
@SpringBootApplication
class BlogApplication @Autowired constructor(
        val dataSource: DataSource
){
    @Bean
    fun getBCrypt(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder(8)
    }

    @Bean
    fun getDB(): Database {
        return Database.connect(dataSource)
    }
}

fun main(args: Array<String>) {
    runApplication<BlogApplication>(*args)
}