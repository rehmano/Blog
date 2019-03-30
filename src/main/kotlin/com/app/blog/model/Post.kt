package com.app.blog.model

import org.joda.time.DateTime
import org.joda.time.DateTime.now

data class Post(val username: String, val title: String, val content: String, val date_created: DateTime = now())