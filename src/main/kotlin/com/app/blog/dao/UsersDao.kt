package com.app.blog.dao

import com.app.blog.model.UserModel
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class UsersDao constructor(
        val db1: Database,
        val bCryptPasswordEncoder: BCryptPasswordEncoder
) {


    object UsersTable : Table("users") {
        val name = varchar("username", 50)
        val pass = varchar("password",200)
    }


    fun UsersTable.rowToUserModel(row: ResultRow): UserModel {
        return UserModel(username = row[name], password = row[name])
    }

    fun selectAll(): List<UserModel> = transaction(db1) { UsersTable.selectAll().map{ UserModel(it[UsersTable.name], it[UsersTable.pass]) } }

    fun insert(user: UserModel) {
        transaction(db1) {
            UsersTable.insert {
                println("[UsersDao] Adding user ${user.username} with pass ${user.password}")
                it[name] = user.username
                it[pass] = bCryptPasswordEncoder.encode(user.password)
            }
        }
    }

    fun findOneByUsername(username: String): String? {
        var user: UserModel? = UserModel()
        transaction(db1) {
            user = UsersTable.select { UsersTable.name eq username }
                    .limit(1)
                    .map { it.toUserModel() }
                    .firstOrNull()
        }
        return user?.username

    }

    private fun ResultRow.toUserModel() = UsersTable.rowToUserModel(this)

}