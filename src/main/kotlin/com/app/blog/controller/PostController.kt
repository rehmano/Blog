package com.app.blog.controller

import com.app.blog.model.Post
import org.springframework.web.bind.annotation.*
import java.util.concurrent.atomic.AtomicLong


@RestController
class PostController {

    val counter = AtomicLong()

    @GetMapping("/")
    fun getPost(@RequestParam(value = "title", defaultValue = "default title") title: String,
             @RequestParam(value = "content", defaultValue = "default content") content: String) : Post {
        return Post(counter.incrementAndGet(), title, content)
    }

}