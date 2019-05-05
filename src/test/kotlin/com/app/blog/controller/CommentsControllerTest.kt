package com.app.blog.controller

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner


@RunWith(SpringRunner::class)
@SpringBootTest
class CommentsControllerTest {

    @Autowired
    lateinit var commentController: CommentController

    @Test
    fun getComments() {
        val result = commentController.loadComments(1)
        assertNotNull(result)
        result.forEach{println("${it.key}\t${it.value}")}
        assertEquals("varoon20", result.keys.first().username)
        assertEquals("lul the guy above me is bad", result.keys.first().content)
        assertEquals(true, result.values.first())
    }

    @Test
    fun getBaseComments() {
        val result = commentController.loadBaseComments(9)
        assertNotNull(result)
        result.forEach{println("${it.key}\t${it.value}")}
    }

    @Test
    fun addComment() {

    }

}