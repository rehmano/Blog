package com.app.blog.controller

import com.app.blog.dao.UsersDao
import com.app.blog.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import javax.validation.Valid


@Controller
class UserController @Autowired constructor(
        private val usersDao: UsersDao
) {

    @RequestMapping("/login")
    fun login(): String {
        return "login"
    }

    @GetMapping("/signup")
    fun signup(): ModelAndView {
        val model = ModelAndView()
        model.viewName = "signup"
        val user = User()
        if(user == null){
            print("wow")

        } else {
            model.addObject("user", User())
        }
        return model
    }

    /**
     * Currently 405's out
     */
    @PostMapping("/signup")
    fun createUser(): User {
        /*val userExists = usersDao.doesExist(user.username)
        if(userExists){
            bindingResult.rejectValue("username", "error.user", "This username is already in use!")
        }
        if(bindingResult.hasErrors()){
            model.viewName = "signup"
        } else {
            usersDao.insert(user)
            model.addObject("user", user)
            model.viewName = "signup"
        }*/

        return User()
    }

}