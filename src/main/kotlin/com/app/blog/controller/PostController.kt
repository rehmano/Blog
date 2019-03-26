package com.app.blog.controller

import com.app.blog.dao.PostsDao
import com.app.blog.model.Post
import com.app.blog.model.User
import org.jetbrains.exposed.sql.Query
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import java.util.concurrent.atomic.AtomicLong
import javax.servlet.http.HttpSession


@RestController
class PostController @Autowired constructor(
        private val postsDao: PostsDao
) {

}