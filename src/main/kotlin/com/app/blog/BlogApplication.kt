package com.app.blog

import com.app.blog.dao.PostsDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

@SpringBootApplication
@EnableTransactionManagement
class BlogApplication @Autowired constructor(
		private val dataSource: DataSource
) {
	@Bean
	fun getPostsDao(): PostsDao {
		return PostsDao(dataSource = dataSource)
	}
}

fun main(args: Array<String>) {
	runApplication<BlogApplication>(*args)
}


