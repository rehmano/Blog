package com.app.blog.controller

import com.app.blog.model.Post
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
class PostControllerTest {

    @Autowired
    lateinit var postController: PostController

    @Test
    fun loadPost() {
        val result = postController.loadPost(3)
        assertNotNull(result)
        assertEquals(3, result.id)
        assertEquals("hello title", result.title)
        assertEquals("content", result.content)
    }

    @Test
    fun addPost() {
        postController.addPost("varoon20", "testing this works", "jello is good")
        val toCheck = postController.getNewPosts().last()
        println(toCheck)
        assertEquals(toCheck.content, "jello is good")
    }

    @Test
    fun getNewPosts() {
        val result = postController.getNewPosts()
        assertNotNull(result)
        result.forEach{println(it)}
        assertEquals(981, result.first().id)
    }

    @Test
    fun loadMorePosts() {
        val result = postController.loadMorePosts(postController.getNewPosts().first().id)
        assertNotNull(result)
        result.forEach{println(it)}
        assertEquals(980, result.last().id)
    }

    @Test
    fun deletePost() {

    }

}
