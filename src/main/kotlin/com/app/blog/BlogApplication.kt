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
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import javax.sql.DataSource

import javax.validation.Valid

@SpringBootApplication(scanBasePackages = ["com.app.blog", "com.app.blog.dao"])
@EnableTransactionManagement
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

@Controller
@ControllerAdvice
class UserServiceController @Autowired constructor(
        val usersDao: UsersDao,
        val authorityDao: AuthorityDao
){
    @ModelAttribute("user")
    fun addUserAtt(): UserModel = UserModel()

    @GetMapping("/signup")
    fun signup(): String = "signup"

    @PostMapping("/signup")
    fun createUser(@Valid @ModelAttribute("user") user: UserModel): String{
        println("${user.username} + ${user.password} has attempted to register!")
        usersDao.insert(user)
        authorityDao.insert(user)
        return "login"
    }
}




