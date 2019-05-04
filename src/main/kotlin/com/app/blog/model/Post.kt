package com.app.blog.model

import org.joda.time.DateTime
import org.joda.time.DateTime.now
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

//Id is not used unless its grabbed from the DB in which id can be stored, ID is never inserted
data class Post(val id: Int, val username: String, val title: String, val content: String, val date_created: DateTime = now()) {
    fun toBarePost(): BarePost {
        val dateTimeFormatter: DateTimeFormatter = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss")
        return BarePost(id = this.id, username = this.username, title = this.title, content = this.content, dateTime = dateTimeFormatter.print(this.date_created))
    }
}