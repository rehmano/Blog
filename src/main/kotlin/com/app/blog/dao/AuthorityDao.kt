package com.app.blog.dao

import com.app.blog.model.UserModel
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class AuthorityDao @Autowired constructor(
        val db1: Database
) {

    fun insert(user: UserModel, authorityToSet: String = "ROLE_USER") {
        transaction(db1) {
            AuthorityTable.insert {
                println("Adding user ${user.username} with authority $authorityToSet")
                it[name] = user.username
                it[authority] = authorityToSet
            }
        }
    }
}