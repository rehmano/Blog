package com.app.blog.dao

import com.app.blog.model.User
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Repository
import javax.sql.DataSource

object Users : Table() {
    val name = varchar("username", 50)
    val pass = varchar("password",200)
}

@Repository
class UsersDao @Autowired constructor(
        dataSource: DataSource
) {

    val db1 = Database.connect(dataSource)


    fun selectAll(): List<User> = transaction(db1) { Users.selectAll().map{ User(it[Users.name], it[Users.pass]) } }

    fun insert(user: User) {
        transaction(db1){
            Users.insert {
                println("Adding user ${user.username} with pass ${user.password}")
                it[name] = user.username
                it[pass] = BCryptPasswordEncoder().encode(user.password)
            }

        }
    }

    fun doesExist(username: String): Boolean{
        for(user in selectAll()){
            if(user.username == username) {
                return true
            }
        }
        return false
    }
}