package com.app.blog.dao

import com.app.blog.model.UserModel
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class UsersDao @Autowired constructor(
        val db1: Database,
        val bCryptPasswordEncoder: BCryptPasswordEncoder
) {


    fun selectAll(): List<UserModel> = transaction(db1) { UsersTable.selectAll().map{ UserModel(it[UsersTable.name], it[UsersTable.pass]) } }

    fun insert(user: UserModel) {
        transaction(db1) {
            UsersTable.insert {
                println("Adding user ${user.username} with pass ${user.password}")
                it[name] = user.username
                it[pass] = bCryptPasswordEncoder.encode(user.password)
            }
        }
    }
}