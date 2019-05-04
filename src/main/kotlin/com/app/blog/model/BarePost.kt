package com.app.blog.model

import org.joda.time.DateTime

data class BarePost(val id: Int? = null, val username: String? = null, val title: String? = null, val content: String = "", val dateTime: String? = null)