package com.app.blog.dao

import com.app.blog.model.CommunityToPost
import com.app.blog.model.Post
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class CommunityToPostDao constructor(
        val db1: Database,
        val postsDao: PostsDao
) {


    object CommunityPostTable : Table("community_to_post") {
        val post_id = integer("post_id").primaryKey()
        val community_id = integer("community_id")
    }


    fun CommunityPostTable.rowToCommunityToPost(row: ResultRow): CommunityToPost {
        return CommunityToPost(post_id = row[post_id], community_id = row[community_id])
    }

    fun selectAll(): List<CommunityToPost> = transaction(db1) { CommunityPostTable.selectAll().map{ CommunityToPost(it[CommunityPostTable.post_id], it[CommunityPostTable.community_id]) } }

    fun insert(communityToPost: CommunityToPost) {
        transaction(db1) {
            CommunityPostTable.insert {
                println("[CommunityToPostDao] Adding post_id ${communityToPost.post_id} with community_id ${communityToPost.community_id}")
                it[post_id] = communityToPost.post_id
                it[community_id] = communityToPost.community_id
            }
        }
    }

    fun getAllPostsForCommunity(id: Int): List<Post> {
        var post: List<CommunityToPost> = listOf()
        transaction(db1){
            post = CommunityPostTable.select { CommunityPostTable.community_id eq id}.map{it.toCommunityToPost()}
        }
        var posts: MutableList<Post> = mutableListOf()

        post.forEach { posts.plusAssign(postsDao.getPostById(it.post_id))  }
        return posts
    }

    private fun ResultRow.toCommunityToPost() = CommunityPostTable.rowToCommunityToPost(this)

}