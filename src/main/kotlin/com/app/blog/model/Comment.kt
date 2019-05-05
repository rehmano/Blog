package com.app.blog.model

import org.joda.time.DateTime

data class Comment(val post_id: Int,
                   val comment_id: Int,
                   val username: String,
                   val comment_chain_id: Int?,
                   val date_created: DateTime = DateTime.now(),
                   val content: String)