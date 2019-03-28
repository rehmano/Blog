package com.app.blog.controller

import com.app.blog.dao.AuthorityDao
import com.app.blog.dao.UsersDao
import com.app.blog.model.UserModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import javax.validation.Valid

@Controller
@ControllerAdvice
class UserServiceController @Autowired constructor(
        val usersDao: UsersDao,
        val authorityDao: AuthorityDao
) {

    @GetMapping("/signup")
    fun signup(): ModelAndView {
        val modelAndView = ModelAndView()
        modelAndView.viewName = "signup"
        modelAndView.addObject("user", UserModel())

        return modelAndView
    }

    @PostMapping("/signup")
    fun createUser(@Valid @ModelAttribute("user") user: UserModel, bindingResult: BindingResult): ModelAndView {
        val modelAndView = ModelAndView()
        println("[POST @ \"/signup\"] Username: \"${user.username}\" + Password: \"${user.password}\" has attempted to register!")

        if (usersDao.findOneByUsername(user.username) == user.username) {
            bindingResult.rejectValue("username", "error.user", "User already exists! Try another username.")
            println("[POST @ \"/signup\"] \"${user.username}\" already exists.")
        }
        if (bindingResult.hasErrors()) {
            modelAndView.viewName = "signup"
        } else {
            usersDao.insert(user)
            authorityDao.insert(user)
            modelAndView.viewName = "login"
        }

        return modelAndView
    }

    @RequestMapping("login")
    fun login(): String {
        return "login"
    }

}