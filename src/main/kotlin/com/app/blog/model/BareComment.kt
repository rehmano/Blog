package com.app.blog.model

data class BareComment(val post_id: Int? = null,
                       val comment_id: Int? = null,
                       val username: String? = null,
                       val date_created: String?,
                       val comment_chain_id: Int? = null,
                       val content: String? = null,
                       var hasChildren: Boolean = false)