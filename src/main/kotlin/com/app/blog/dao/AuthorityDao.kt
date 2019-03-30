package com.app.blog.dao

import com.app.blog.model.UserModel
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class AuthorityDao constructor(
        val db1: Database
) {

    private object AuthorityTable : Table("authorities") {
        val name = varchar("username", 50)
        val authority = varchar("authority",50)
    }



    fun insert(user: UserModel, authorityToSet: String = "ROLE_USER") {
        transaction(db1) {
            AuthorityTable.insert {
                println("[AuthorityDao] Adding user ${user.username} with authority $authorityToSet")
                it[name] = user.username
                it[authority] = authorityToSet
            }
        }
    }


}