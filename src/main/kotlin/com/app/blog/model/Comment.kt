package com.app.blog.model

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

data class Comment(val post_id: Int,
                   val comment_id: Int,
                   val username: String,
                   val comment_chain_id: Int?,
                   val date_created: DateTime = DateTime.now(),
                   val content: String){
    fun toBareComment(): BareComment {
        val dateTimeFormatter: DateTimeFormatter = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss")
        return BareComment(post_id = post_id, comment_id = comment_id, username = username, date_created = dateTimeFormatter.print(date_created), comment_chain_id = comment_chain_id, content = content)
    }
}