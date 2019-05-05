package com.app.blog.dao

import com.app.blog.model.BarePost
import com.app.blog.model.Post
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

/**
 * Name: Syed Rehman
 * Class: PostsDao
 * Purpose: Interact with the posts table using the Exposed DSL
 * Functions: {
 *      ResultRow.toPostModel() -> Post
 *      PostTable.rowToPostModel(row: ResultRow) -> Post
 *      selectAll() -> List<Post>
 *      insert(username: String, titleToAdd: String, contentToAdd: String) -> Void
 *      getOrderedPosts() -> List<Post>
 *      getPostById(id: Int) -> Post
 *      removePostById(id: Int) -> Post
 *      doesOwnPost(username: String, id: Int) -> Boolean
 *      getNewestPostForUsername(username: String) -> Post
 *      getPostsForUsername(username: String) -> List<Post>
 * }
 */
@Repository
@Transactional
class PostsDao constructor(
        val db1: Database
) {

    /**
     * Object: PostsTable
     * Purpose: Build the representation of the table "posts"
     */
    private object PostsTable : Table("posts") { //"posts" shows the table is named posts
        val id = integer("id")
        val name = varchar("username", 50)
        val title = varchar("title", 128)
        val content = text("content")
        val date_created = datetime("date_created")
    }


    /**
     * Just some helper functions designed to convert ResultRow to a proper Post object
     * ResultRow.toPostModel() -> Post
     * PostTable.rowToPostModel(row: ResultRow) -> Post
     */
    private fun ResultRow.toPostModel(): Post = PostsTable.rowToPostModel(this)

    private fun PostsTable.rowToPostModel(row: ResultRow): Post {
        return Post(id = row[id], username = row[name], title = row[title], content = row[content], date_created = row[date_created])
    }


    /**
     * Written to reduce boilerplate
     */
    fun selectEqQuery(toCompare: Column<String>, other: String): List<Post>? {
        var posts: List<Post> = listOf()
        transaction(db1) {
            posts = PostsTable.select { toCompare eq other }.orderBy(PostsTable.date_created to SortOrder.ASC)
                    .map { it.toPostModel() }
        }
        return posts
    }

    fun selectEqQuery(toCompare: Column<Int>, other: Int): List<Post>? {
        var posts: List<Post> = listOf()
        transaction(db1) {
            posts = PostsTable.select { toCompare eq other }
                    .map { it.toPostModel() }
        }
        return posts
    }

    /**
     * Basic selectAll query
     * selectAll() -> List<Post>
     */
    fun selectAll(): List<Post> = transaction(db1) { PostsTable.selectAll().map { Post(it[PostsTable.id], it[PostsTable.name], it[PostsTable.title], it[PostsTable.content], it[PostsTable.date_created]) } }


    /**
     * Insertion query that takes in a username, title, and content and adds it to the database
     * insert(username: String, titleToAdd: String, contentToAdd: String) -> Void
     */
    fun insert(username: String,
               titleToAdd: String,
               contentToAdd: String) {
        transaction(db1) {
            PostsTable.insert {
                println("[PostsDao] Adding new Post(username = \"${username}\", title = \"${titleToAdd}\", content = \"${contentToAdd}\"")
                it[name] = username
                it[title] = titleToAdd
                it[content] = contentToAdd
            }
        }
    }

    /**
     *  Queries the database for Posts that have the same username as the one passed in.
     *  findPostsForUsername(username: String) -> List<Post>
     */
    fun findPostsForUsername(username: String): List<Post> {
        println("[PostsDao] Serving up posts for username: \"$username\"")
        var posts: List<Post> = listOf()
        transaction(db1) {
            posts = PostsTable.select { PostsTable.name eq username }.orderBy(PostsTable.date_created to SortOrder.ASC)
                    .map { it.toPostModel() }
        }
        println("[PostsDao] Finished serving up posts for username: \"$username\"")
        return posts
    }

    /**
     * Queries the database for the latest 25 posts
     * If id != 0 then it finds the last 25 in regards to id
     * getNewPosts(id: Int = 0) -> List<Post>
     */
    //0 is a default so you are able to handle if someone has more posts they want to view and have viewed previously
    fun getNewPosts(id: Int = 0): List<Post> {
        if (id != 0) { //If this is triggered you have the id of the last post they viewed
            var posts: List<Post> = listOf()
            println("[PostsDao] Getting new Posts starting @ id: $id")
            transaction(db1) {
                posts = PostsTable.select { PostsTable.id less id and (PostsTable.id greaterEq (id - 25)) }.orderBy(PostsTable.date_created to SortOrder.ASC)
                        .map { it.toPostModel() }
            }
            println("[PostsDao] Finished getting new Posts starting @ id: $id")
            return posts
        } else {
            return getOrderedPosts()
        }

    }


    /**
     * Queries the database to get the latest 25 posts in ordered form.
     * getOrderedPosts() -> List<Post>
     */
    fun getOrderedPosts(): List<Post> {
        var posts: List<Post> = listOf()
        transaction(db1) {
            posts = PostsTable.selectAll().orderBy(PostsTable.date_created to SortOrder.ASC)
                    .map { it.toPostModel() }.takeLast(26)
        }
        return posts
    }

    /**
     * Queries the database to retrieve the post relating to the id passed in
     * getPostById(id: Int) -> Post
     *
     * */
    fun getPostById(id: Int): Post = selectEqQuery(PostsTable.id, id).orEmpty().first()


    /**
     * Queries the database to remove the post relating to the id passed in
     * (expects user auth to be checked before)
     * removePostById(id: Int) -> Post
     */
    fun removePostById(id: Int): Post {
        println("[PostsDao] Deleting Post for id: \"$id\"")

        val postToRemove = selectEqQuery(PostsTable.id, id).orEmpty().first()
        transaction(db1) {
            PostsTable.deleteWhere { PostsTable.id eq id }
        }

        return postToRemove
    }

    /**
     * Checks if the username passed in owns the Post requested
     * doesOwnPost(username: String, id: Int) -> Boolean
     */
    fun doesOwnPost(username: String, id: Int): Boolean {
        val post = selectEqQuery(PostsTable.id, id).orEmpty().first()
        return post.username == username
    }

    /**
     * Queries the database for the newest post for that username
     * getNewestPostForUsername(username: String) -> Post
     */
    fun getNewestPostForUsername(username: String): Post? {
        return getPostsForUsername(username).last()
    }


    /**
     * Queries the database for all posts for that username
     * getPostsForUsername(username: String) -> List<Post>
     */
    fun getPostsForUsername(username: String): List<Post> {
        return selectEqQuery(PostsTable.name, username).orEmpty()
    }

    /**
     *
     */
}