package com.app.blog.controller

import com.app.blog.dao.UsersDao
import com.app.blog.model.UserModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView

@Controller
@RequestMapping("/")
class HomeController @Autowired constructor(
        private val usersDao: UsersDao
) {

    @RequestMapping
    fun index(): ModelAndView {
        val modelAndView = ModelAndView()
        val auth: Authentication = SecurityContextHolder.getContext().authentication

        val username: String? = usersDao.findOneByUsername(auth.name)
        modelAndView.addObject("username", username)
        modelAndView.viewName = "index"
        return modelAndView
    }

}