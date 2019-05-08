package com.app.blog.dao

import com.app.blog.model.Comment
import com.app.blog.model.Post
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

/**
 * Name: Syed Rehman
 * Class: CommentsDao
 * Purpose: Query the database for the "comments" table
 * Functions: {
 *      ResultRow.toPostModel() -> Post
 *      PostTable.rowToPostModel(row: ResultRow) -> Post
 *      selectAll() -> List<Comment>
 *      insert(post_id2: Int, username: String, comment_chain_id2: Int?, content2: String) -> Void
 *      findCommentsForUsername(username: String) -> List<Comment>
 *      doesCommentHaveChildren(comment: Comment) -> Boolean
 *      getBaseCommentsForPost(id: Int) -> Map<Comment, Boolean>
 *      getCommentsForCommentId(commentid: Int) -> Map<Comment, Boolean>
 * }
 */

@Repository
@Transactional
class CommentsDao constructor(
        val db1: Database
) {
    /**
     * Object: CommentsTable
     * Purpose: Build the representation of the table "comments"
     */
    private object CommentsTable : Table("comments") {
        val post_id = integer("post_id")
        val comment_id = integer("comment_id").primaryKey()
        val name = varchar("username", 50)
        val comment_chain_id = integer("comment_chain_id").nullable()
        val date_created = datetime("date_created")
        val content = text("content")
    }

    /**
     * Just some helper functions designed to convert ResultRow to a proper Post object
     * ResultRow.toPostModel() -> Post
     * PostTable.rowToPostModel(row: ResultRow) -> Post
     */
    private fun ResultRow.toCommentModel() = CommentsTable.rowToCommentModel(this)

    private fun CommentsTable.rowToCommentModel(row: ResultRow): Comment {
        return Comment(post_id = row[post_id], comment_id = row[comment_id], username = row[name], comment_chain_id = row[comment_chain_id], date_created = row[date_created], content = row[content])
    }

    fun selectQuery(toCompare: Column<Int>, other: Int): List<Comment>? {
        var comments: List<Comment> = listOf()
        transaction(db1) {
            comments = CommentsTable.select { toCompare eq other }
                    .map { it.toCommentModel() }
        }
        return comments
    }

    fun selectQuery(toCompare: Column<Int?>, other: Int?): List<Comment>? {
        var comments: List<Comment> = listOf()
        transaction(db1) {
            comments = CommentsTable.select { toCompare eq other }
                    .map { it.toCommentModel() }
        }
        return comments
    }

    /**
     * Basic selectAll query
     * selectAll() -> List<Comment>
     */

    fun selectAll(): List<Comment> = transaction(db1) {
        CommentsTable.selectAll().map{
            Comment(it[CommentsTable.post_id], it[CommentsTable.comment_id],it[CommentsTable.name], it[CommentsTable.comment_chain_id], it[CommentsTable.date_created], it[CommentsTable.content])
        }
    }

    /**
     * Basic insertion query
     * insert(post_id2: Int, username: String, comment_chain_id2: Int?, content2: String) -> Void
     */
    fun insert(post_id2: Int, username: String, comment_chain_id2: Int?, content2: String) {
        transaction(db1) {
            CommentsTable.insert {
                println("[CommentsDao] Adding new Comment(post_id =\"$post_id2\", username = \"$username\", title = \"$comment_chain_id2\", content = \"$content2\")")
                it[post_id] = post_id2
                it[name] = username
                it[comment_chain_id] = comment_chain_id2
                it[content] = content2
            }
        }
    }

    /**
     * Finds comments for a specific username
     * findCommentsForUsername(username: String) -> List<Comment>
     */
    fun findCommentsForUsername(username: String): List<Comment> {
        println("[CommentsDao] Serving up comments for: \"$username\"")
        var posts: List<Comment> = listOf()
        transaction(db1) {
            posts = CommentsTable.select { CommentsTable.name eq username }
                    .map { it.toCommentModel() }
        }
        println("[CommentsDao] Finished serving up comments for: \"$username\"")
        return posts
    }

    /**
     * Checks to see if the comment has chilren
     * doesCommentHaveChildren(comment: Comment) -> Boolean
     */
    fun doesCommentHaveChildren(comment: Comment): Boolean {
        var hasChild = false
        transaction(db1){
            hasChild = selectQuery(CommentsTable.comment_chain_id, comment.comment_id).orEmpty() != listOf<Comment>()
        }
        return hasChild
    }

    /**
     * Queries the database for the base comments for the passed in "post_id"
     * getBaseCommentsForPost(id: Int) -> Map<Comment, Boolean>
     */
    fun getBaseCommentsForPost(id: Int): Map<Comment, Boolean> {
        println("[CommentsDao] Serving up comments for POSTID: \"${id}\"")

        var comments: List<Comment> = listOf()

        transaction(db1) {
            comments = CommentsTable.select{CommentsTable.post_id eq id and (CommentsTable.comment_chain_id.isNull() )}.orderBy(CommentsTable.date_created to SortOrder.DESC)
                    .map{it.toCommentModel()}
        }

        val commentsWithChild: MutableMap<Comment, Boolean> = mutableMapOf()
        comments.orEmpty().forEach{commentsWithChild[it] = doesCommentHaveChildren(it)}

        return commentsWithChild
    }
    fun doesOwnComment(username: String, id: Int): Boolean {
        val comment = selectQuery(CommentsTable.comment_id, id).orEmpty().first()
        return comment.username == username
    }

    fun delete(id: Int) {
        var comment: Comment? = null
        transaction(db1) {
            comment = CommentsTable.select{CommentsTable.comment_id eq id}.map{it.toCommentModel()}.first()
            if(comment != null){
                CommentsTable.deleteWhere{CommentsTable.comment_id eq id}
                CommentsTable.insert {
                    println("[CommentsDao] Updating Comment(post_id =\"${comment!!.post_id}\", comment_id=\"${comment!!.comment_id})")
                    it[post_id] = comment!!.post_id
                    it[comment_id] = comment!!.comment_id
                    it[name] = "[DELETED]"
                    it[comment_chain_id] = comment!!.comment_chain_id
                    it[content] = "[DELETED]"
                    it[date_created] = comment!!.date_created!!
                }
            }
        }

    }

    /**
     * Retrieves comments for a specific comment by looking for matching chain and id columns
     * getCommentsForCommentId(commentid: Int) -> Map<Comment, Boolean>
     */
    fun getCommentsForCommentId(commentid: Int) : Map<Comment, Boolean> {
        println("[CommentsDao] Serving up comments for COMMENTID: \"$commentid\"")

        var comments: List<Comment> = listOf()
        transaction(db1) {
            comments = CommentsTable.select { CommentsTable.comment_chain_id eq commentid }.orderBy(CommentsTable.date_created to SortOrder.DESC)
                    .map { it.toCommentModel() }
        }

        val commentsWithChild: MutableMap<Comment, Boolean> = mutableMapOf()

        comments.orEmpty().forEach{commentsWithChild[it] = doesCommentHaveChildren(it)}

        println("[CommentsDao] Finished serving up comments for COMMENTID: \"$commentid\"")
        return commentsWithChild
    }




}