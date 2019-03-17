package com.app.blog.model

data class Post(val user: User, val id: Long, val title: String, val content: String)