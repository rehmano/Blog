package com.app.blog.controller

import com.app.blog.dao.post_type
import com.app.blog.dao.PostsDao
import com.app.blog.dao.UsersDao
import com.app.blog.model.Post
import com.app.blog.model.User
import org.jetbrains.exposed.sql.Query
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*
import java.util.concurrent.atomic.AtomicLong
import javax.servlet.http.HttpSession


@RestController
class UserController @Autowired constructor(
        private val usersDao: UsersDao
) {

    val counter = AtomicLong()

    @RequestMapping("/user")
    fun getPosts(session: HttpSession) : List<User> {
        println(session.id)
        return usersDao.selectAll()
    }

    @PutMapping("/user/post")
    fun postPost(@RequestBody user: User,
                 session: HttpSession) {
        return usersDao.insert(user)
    }

}