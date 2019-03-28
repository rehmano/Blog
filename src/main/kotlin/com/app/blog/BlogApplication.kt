package com.app.blog

import com.app.blog.dao.AuthorityDao
import com.app.blog.dao.UsersDao
import com.app.blog.model.UserModel
import org.jetbrains.exposed.sql.Database
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import javax.sql.DataSource

import javax.validation.Valid

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