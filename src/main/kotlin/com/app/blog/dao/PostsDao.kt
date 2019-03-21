package com.app.blog.dao

import com.app.blog.model.Post
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import javax.sql.CommonDataSource
import javax.sql.DataSource

enum class post_type {
    LINK_TYPE, TEXT_TYPE
}

object Posts : Table() {
    val title = varchar("title", 255)
    val body = text("body")
}

@Repository
@Transactional
class PostsDao @Autowired constructor(
        dataSource: DataSource
) {

    val db1 = Database.connect(dataSource)

    fun selectAll(): List<Post> = Posts.selectAll().map{ Post(it[Posts.title], it[Posts.body]) }

    fun insert(post: Post): Query {

        transaction(db1){
            Posts.insert {
                it[title] = post.title
                it[body] = post.body
            }
        }
        return Posts.select {Posts.title.eq(post.title)}
    }
}