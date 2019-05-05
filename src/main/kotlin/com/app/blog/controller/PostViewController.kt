package com.app.blog.controller

import com.app.blog.dao.UsersDao
import com.app.blog.model.BarePost
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView

@Controller
@RequestMapping("/post")
class PostViewController @Autowired constructor(
        private val postController: PostController,
        private val usersDao: UsersDao
){
    @RequestMapping
    fun loadPost(@RequestParam id: Int): ModelAndView {
        val postToLoad = postController.loadPost(id).toBarePost()
        val modelAndView = ModelAndView()
        modelAndView.addObject("username", getCurrentlyLoggedUser())
        modelAndView.addObject("postView", postToLoad)
        modelAndView.addObject("postToAdd", BarePost())
        modelAndView.viewName = "post"
        return modelAndView
    }

    fun getCurrentlyLoggedUser(): String? {
        val auth: Authentication = SecurityContextHolder.getContext().authentication
        return usersDao.findOneByUsername(auth.name)
    }
}