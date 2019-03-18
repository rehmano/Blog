package com.app.blog.model

import com.app.blog.dao.BodyType

data class Post (val title: String,
                 val body_type: BodyType,
                 val body: String)