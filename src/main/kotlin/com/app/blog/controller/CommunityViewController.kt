package com.app.blog.controller

import com.app.blog.dao.CommunityDao
import com.app.blog.dao.CommunityToPostDao
import com.app.blog.dao.UsersDao
import com.app.blog.model.BarePost
import com.app.blog.model.Post
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

@Controller
@RequestMapping("/r")
class CommunityViewController @Autowired constructor(
        val communityDao: CommunityDao,
        val communityToPost: CommunityToPostDao,
        val usersDao: UsersDao
) {
    @RequestMapping
    fun getCommunities() : ModelAndView {
        val modelAndView = ModelAndView()
        modelAndView.addObject("communities", communityDao.selectAll())
        modelAndView.addObject("postToAdd", BarePost())
        modelAndView.addObject("username", getCurrentlyLoggedUser())
        modelAndView.viewName = "communities"
        return modelAndView
    }


    @RequestMapping("/{community}")
    fun getPostsForCommunityName(@PathVariable communityName: String): List<Post> {
        return communityToPost.getAllPostsForCommunity(communityDao.getIdForName(communityName)!!)
    }

    fun getCurrentlyLoggedUser(): String? {
        val auth: Authentication = SecurityContextHolder.getContext().authentication
        return usersDao.findOneByUsername(auth.name)
    }

}