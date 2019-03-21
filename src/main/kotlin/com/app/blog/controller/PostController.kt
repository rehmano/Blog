package com.app.blog.controller

import com.app.blog.dao.post_type
import com.app.blog.dao.PostsDao
import com.app.blog.model.Post
import org.jetbrains.exposed.sql.Query
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*
import java.util.concurrent.atomic.AtomicLong
import javax.servlet.http.HttpSession


@RestController
class PostController @Autowired constructor(
        private val postsDao: PostsDao
) {

    val counter = AtomicLong()

    @RequestMapping("/")
    fun getPosts() : List<Post> {
        return postsDao.selectAll()
    }

    @PutMapping("/post")
    fun postPost(@RequestBody post: Post,
                 session: HttpSession) : Query {

        return postsDao.insert(Post(post.title, post.body))
    }

}