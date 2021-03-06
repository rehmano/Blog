package com.app.blog.controller

import com.app.blog.dao.AuthorityDao
import com.app.blog.dao.UsersDao
import com.app.blog.model.BarePost
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView

@Controller
@RequestMapping("/")
class HomeController @Autowired constructor(
        private val usersDao: UsersDao,
        private val authorityDao: AuthorityDao,
        private val postController: PostController
) {

    @RequestMapping
    fun index(): ModelAndView {
        val modelAndView = ModelAndView()
        val bareNewPosts = postController.getNewPosts().reversed().map{it.toBarePost()}
        modelAndView.addObject("username", getCurrentlyLoggedUser())
        modelAndView.addObject("posts", bareNewPosts)
        modelAndView.addObject("postToAdd", BarePost())
        modelAndView.viewName = "index"
        return modelAndView
    }



    fun getCurrentlyLoggedUser(): String? {
        val auth: Authentication = SecurityContextHolder.getContext().authentication
        return usersDao.findOneByUsername(auth.name)
    }


}