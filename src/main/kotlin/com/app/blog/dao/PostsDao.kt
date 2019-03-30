package com.app.blog.dao

import com.app.blog.model.Post
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class PostsDao constructor(
        val db1: Database
) {
    private object PostsTable : Table("posts") {
        val name = varchar("username", 50)
        val title = varchar("title",128)
        val content = text("content")
        val date_created = datetime("date_created")
    }

    private fun ResultRow.toPostModel() = PostsTable.rowToPostModel(this)

    private fun PostsTable.rowToPostModel(row: ResultRow): Post {
        return Post(username = row[name], title = row[title], content = row[content], date_created = row[date_created])
    }

    fun selectAll(): List<Post> = transaction(db1) { PostsTable.selectAll().map{ Post(it[PostsTable.name], it[PostsTable.title], it[PostsTable.content], it[PostsTable.date_created]) } }

    fun insert(post: Post) {
        transaction(db1) {
            PostsTable.insert {
                println("[PostsDao] Adding new Post(username = \"${post.username}\", title = \"${post.title}\", content = \"${post.content}\"")
                it[name] = post.username
                it[title] = post.title
                it[content] = post.content
            }
        }
    }

    fun findPostsForUsername(username: String): List<Post> {
        println("[PostsDao] Serving up posts for: \"$username\"")
        var posts: List<Post> = listOf()
        transaction(db1) {
            posts = PostsTable.select { PostsTable.name eq username }
                    .map { it.toPostModel() }
        }
        println("[PostsDao] Finished serving up posts for: \"$username\"")
        return posts
    }



}