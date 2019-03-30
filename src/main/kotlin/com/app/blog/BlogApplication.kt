package com.app.blog

import com.app.blog.dao.AuthorityDao
import com.app.blog.dao.PostsDao
import com.app.blog.dao.UsersDao
import org.jetbrains.exposed.sql.Database
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import javax.sql.DataSource
@SpringBootApplication(scanBasePackages = ["com.app.blog", "com.app.blog.dao"])
class BlogApplication @Autowired constructor(
        val dataSource: DataSource
){
    @Bean
    fun getUsersDao(): UsersDao {
        return UsersDao(db1 = getDB(), bCryptPasswordEncoder = getBCrypt())
    }

    @Bean
    fun getAuthorityDao(): AuthorityDao {
        return AuthorityDao(db1 = getDB())
    }

    @Bean
    fun getPostsDao(): PostsDao {
        return PostsDao(db1 = getDB())
    }

    @Bean
    fun getBCrypt(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun getDB(): Database {
        return Database.connect(dataSource)
    }
}

fun main(args: Array<String>) {
    runApplication<BlogApplication>(*args)
}