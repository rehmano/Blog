package com.app.blog.controller

import com.app.blog.dao.BodyType
import com.app.blog.dao.PostsDao
import com.app.blog.model.Post
import org.jetbrains.exposed.sql.Query
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.concurrent.atomic.AtomicLong


@RestController
class PostController @Autowired constructor(
        private val postsDao: PostsDao
) {

    val counter = AtomicLong()

    @RequestMapping("/")
    fun getPosts() : List<Post> {
        return postsDao.selectAll()
    }

    @PutMapping(value = ["/post"])
    fun postPost(@RequestParam text: String,
                 @RequestParam body_type: BodyType,
                 @RequestParam content: String) : Query {
        return postsDao.insert(Post(text, body_type, content))
    }

}