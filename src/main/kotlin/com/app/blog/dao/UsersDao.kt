package com.app.blog.dao

import com.app.blog.model.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.sql.DataSource

object Users : Table() {
    val id = uuid("id")
    val name = varchar("name", 31)
    val pass = text("pass")
    val date_created = date("date_created")
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
                println("Adding user ${user.name} with pass ${user.pass}")
                it[name] = user.name
                it[pass] = user.pass
            }

        }
    }

    fun getId(user: User): List<UUID> =
        transaction(db1){
            Users.slice(Users.id, Users.name, Users.pass).select{Users.name eq user.name}.map{it[Users.id]}
        }
}