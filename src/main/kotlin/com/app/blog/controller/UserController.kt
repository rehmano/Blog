package com.app.blog.controller

import com.app.blog.dao.UsersDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/")
class UserController @Autowired constructor(
        private val usersDao: UsersDao
) {

    @RequestMapping
    fun index(): String {
        return "index"
    }

    @RequestMapping("login")
    fun login(): String {
        return "login"
    }





    /**
     * Currently 405's out
     *
    @PostMapping("signup")
    fun createUser(@ModelAttribute("user") user: User): String {
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

        return "signup"
    }*/

}