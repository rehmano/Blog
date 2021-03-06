package com.app.blog.controller

import com.app.blog.dao.CommentsDao
import com.app.blog.dao.UsersDao
import com.app.blog.model.BareComment
import com.app.blog.model.Comment
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

/**
 * Name: Syed Rehman
 * Class: CommentController                           @ /api/comments/
 * Functions = {
 *      loadComments(id: Int) -> Map<Comment, Boolean>      @ load
 *      loadBaseComments(id: Int) -> Map<Comment, Boolean>  @ base
 *      getNewPosts() -> List<Post>                         @ add
 * }
 *
 * Notes: Written to handle the commentsDao object
 */
@RestController
@RequestMapping("/api/comments/")
class CommentController @Autowired constructor (
        val commentsDao: CommentsDao,
        val usersDao: UsersDao
) {
    /**
     * Function: loadComments(id: Int) -> Map<Comment, Boolean>
     * 1. Gets comments with matching chain id for the passed ID
     * 2. Returns Map<Comment, Boolean> with boolean dictating if theres subcomments
     */
    @GetMapping("load")
    fun loadComments(@RequestParam id: Int): List<BareComment> = mapToBareComment(commentsDao.getCommentsForCommentId(id))

    fun mapToBareComment(p: Map<Comment, Boolean>): List<BareComment>{
        val x: MutableList<BareComment> = mutableListOf()
        p.forEach{
            val temp = it.key.toBareComment()
            temp.hasChildren = it.value
            x.plusAssign(temp)
        }
        x.forEach{println(it)}
        return x
    }
    /**
     * Function: loadBaseComments(id: Int) -> Map<Comment, Boolean>
     * 1. Gets base comments for the passed post_id
     */
    @GetMapping("base")
    fun loadBaseComments(@RequestParam id: Int): List<BareComment> {
        return mapToBareComment(commentsDao.getBaseCommentsForPost(id))
    }

    /**
     * Function: addNewComment(comment: BareComment) -> Post
     * 1. Passes the BareComment to the commentsDao to handle the insertion.
     */
    @PostMapping("add")
    fun addNewComment(@RequestBody comment: BareComment) {
        commentsDao.insert(post_id2 = comment.post_id!!,
                username = getCurrentlyLoggedUser(),
                comment_chain_id2 = comment.comment_chain_id,
                content2 = comment.content!!)
    }

    @GetMapping("owns")
    fun doesOwnComment(@RequestParam id: Int): Boolean {
        return commentsDao.doesOwnComment(getCurrentlyLoggedUser(), id)
    }

    @PostMapping("delete")
    fun deleteComment(@RequestParam id: Int): Boolean {
        if(doesOwnComment(id)){
            commentsDao.delete(id)
            return true
        }
        return false
    }
    /**
     * Function: getCurrentlyLoggedUser() -> String
     * 1. Grabs currently logged user from SecurityContext
     */
    fun getCurrentlyLoggedUser(): String {
        val auth: Authentication = SecurityContextHolder.getContext().authentication
        return usersDao.findOneByUsername(auth.name).orEmpty()
    }


}