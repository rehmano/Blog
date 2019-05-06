package com.app.blog.controller

import com.app.blog.dao.PostsDao
import com.app.blog.dao.UsersDao
import com.app.blog.model.BarePost
import com.app.blog.model.Post
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

/**
 * Name: Syed Rehman
 * Class: PostController                            @ /api/posts/
 * Functions = {
 *      loadPost(id: Int) -> Post                           @ {id}
 *      getPosts() -> List<Post>                            @ all
 *      getNewPosts() -> List<Post>                         @ new
 *      loadMorePost(id: Int) -> List<Post>                 @ newid
 *      addPost(title: String, content: String) -> Post?    @ add
 *      deletePost(id: Int) -> Post?                        @ delete
 * }
 *
 * Notes: Written to handle the postsDao object
 */

@RestController
@RequestMapping("/api/posts/")
class PostController @Autowired constructor(
        val postsDao: PostsDao,
        val usersDao: UsersDao
)  {
    /**
     * Function: loadPost(id: Int) -> Post
     * 1. Gets post for the passed ID
     */
    @GetMapping("{id}")
    fun loadPost(@PathVariable(value="id") id: Int): Post {
        return postsDao.getPostById(id)
    }
    /**
     * Function: getPosts() -> List<Post>
     * 1. Gets all of the posts
     *
     */
    @GetMapping("all") //ROLE_ADMIN
    fun getPosts(): List<Post> {
        return postsDao.selectAll()
    }
    /**
     * Function: getNewPosts() -> List<Post>
     * 1. Gets newest posts
     */
    @GetMapping("new")
    fun getNewPosts(): List<Post> = postsDao.getNewPosts()

    /**
     * Function: loadMorePost(id: Int) -> List<Post>
     * 1. The id passed in correlates to the spot you left off in the posts
     * 2. Takes the id to get the next 25 posts to view
     */
    @GetMapping("newid")
    fun loadMorePosts(@RequestParam id: Int): List<Post> = postsDao.getNewPosts(id)

    /**
     * Function: addPost(title: String, content: String) -> Post?
     * 1. Title and content passed in
     * 2. Currently authenticated user is grabbed.
     * 3. Passed to postsDao for insertion.
     * 4. If it posted it will return the newly added post.
     */
    @PostMapping("add")
    fun addPost(@RequestBody barePost: BarePost): Post? {
        val username = getCurrentlyLoggedUser()
        if(username.isEmpty()) {
            return null
        }
        postsDao.insert(username, barePost.title!!, barePost.content)
        return postsDao.getNewestPostForUsername(username)
    }

    /**
     * Function: deletePost(id: Int) -> Post?
     * 1. Id for the chosen post passed in
     * 2. Username grabbed from SecurityContext
     * 3. Username passed to postsDao along with the id
     * to see if the user truly owns the Post
     * 4. Post is removed and returned
     * 5. Returned as a null Post if User does not own it
     */
    @DeleteMapping("delete")
    fun deletePost(id: Int): Post? {
        return if(postsDao.doesOwnPost(getCurrentlyLoggedUser(), id)){
            postsDao.removePostById(id)
        } else {
            null
        }
    }

    fun getNewestPostForUser(username: String): Post? {
        return postsDao.getNewestPostForUsername(username)
    }

    /**
     * Function: getNewPosts() -> List<Post>
     * 1. Gets newest posts and returns in the form of an element
     */

    @GetMapping("/news")
    fun getNewPostss(): List<BarePost> {
        val posts = postsDao.getNewPosts()
        val barePosts: MutableList<BarePost> = mutableListOf()
        posts.forEach {barePosts.plusAssign(it.toBarePost())}
        return barePosts.reversed()
    }

    @GetMapping("/new/{id}")
    fun getNewPostFromId(@PathVariable(value="id") id: Int): List<BarePost> {
        val posts = postsDao.getNewPosts(id)
        val barePosts: MutableList<BarePost> = mutableListOf()
        posts.forEach {barePosts.plusAssign(it.toBarePost())}
        return barePosts.reversed()
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