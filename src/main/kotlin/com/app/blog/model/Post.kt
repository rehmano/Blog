package com.app.blog.model

import com.app.blog.dao.post_type
import java.util.*

data class Post (val title: String,
                 val body: String,
                 val body_type: post_type = post_type.TEXT_TYPE
                 ) {

}