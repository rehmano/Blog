package com.app.blog.controller

import com.app.blog.dao.PostsDao
import com.app.blog.model.Post
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PostController @Autowired constructor(
        val postsDao: PostsDao
)  {
    @RequestMapping("/posts")
    fun getPosts(): List<Post> {
        val auth: Authentication = SecurityContextHolder.getContext().authentication
        return postsDao.findPostsForUsername(auth.name)
    }
}