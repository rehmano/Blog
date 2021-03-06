package com.app.blog.controller

import com.app.blog.dao.PostsDao
import com.app.blog.dao.UsersDao
import com.app.blog.model.BareComment
import com.app.blog.model.BarePost
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import javax.validation.Valid

@Controller
@RequestMapping("/post")
class PostViewController @Autowired constructor(
        private val postController: PostController,
        private val commentController: CommentController,
        private val usersDao: UsersDao,
        private val postsDao: PostsDao
){
    @RequestMapping
    fun loadPost(@RequestParam id: Int): ModelAndView {
        val postToLoad = postController.loadPost(id)
        val modelAndView = ModelAndView()
        modelAndView.addObject("username", getCurrentlyLoggedUser())
        modelAndView.addObject("postView", postToLoad)
        modelAndView.addObject("postToAdd", BarePost())
        modelAndView.addObject("commentToAdd", BareComment())
        modelAndView.viewName = "post"
        return modelAndView
    }

    @PostMapping
    fun addPost(@Valid @ModelAttribute("postToAdd") barePost: BarePost, bindingResult: BindingResult): ModelAndView {
        val postAdded = postController.addPost(barePost = barePost)
        val modelAndView = ModelAndView()
        modelAndView.addObject("username", getCurrentlyLoggedUser())
        modelAndView.addObject("postView", postController.getNewestPostForUser(getCurrentlyLoggedUser()!!)!!.toBarePost())
        modelAndView.addObject("postToAdd", BarePost())
        modelAndView.addObject("commentToAdd", BareComment())
        modelAndView.viewName = "post"
        return modelAndView
    }

    @PostMapping("/addcomment")
    fun addComment(@Valid @ModelAttribute("commentToAdd") bareComment: BareComment): String {
        val commentAdded = commentController.addNewComment(bareComment)
        return "redirect:/post?id=" + bareComment.post_id
    }



    fun getCurrentlyLoggedUser(): String? {
        val auth: Authentication = SecurityContextHolder.getContext().authentication
        return usersDao.findOneByUsername(auth.name)
    }
}