package com.app.blog.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class AdminController @Autowired constructor(

) {
    @RequestMapping("/admin")
    fun getAdmin(): ModelAndView {
        val adminView = ModelAndView().addObject("username", "admin")
        adminView.viewName = "index"
        return adminView
    }
}