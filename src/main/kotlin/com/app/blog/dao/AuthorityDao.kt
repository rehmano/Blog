package com.app.blog.dao

import com.app.blog.model.AuthorityModel
import com.app.blog.model.UserModel
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

/**
 * Name: Syed Rehman
 * Class: AuthorityDao
 * Functions: {
 *      insert(username: String, authorityToSet: String = "ROLE_USER") -> Void
 *      getAuthorities(username: String) -> List<String>
 * }
 */

@Repository
@Transactional
class AuthorityDao constructor(
        val db1: Database
) {

    /**
     * Object: AuthorityTable
     * Purpose: Build the representation of the table "authorities"
     */
    private object AuthorityTable : Table("authorities") {
        val name = varchar("username", 50)
        val authority = varchar("authority",50)
    }

    private fun ResultRow.toAuthorityModel() = AuthorityTable.rowToCommentModel(this)

    private fun AuthorityTable.rowToCommentModel(row: ResultRow): AuthorityModel {
        return AuthorityModel(username = row[name], authority = row[authority])
    }

    /**
     * Basic insertion query
     * insert(username: String, authorityToSet: String = "ROLE_USER") -> Void
     */
    fun insert(username: String, authorityToSet: String = "ROLE_USER") {
        transaction(db1) {
            AuthorityTable.insert {
                println("[AuthorityDao] Adding user $username with authority $authorityToSet")
                it[name] = username
                it[authority] = authorityToSet
            }
        }
    }

    /**
     * Queries the database for the list of authorities for a given username
     * getAuthorities(username: String) -> List<String>
     */
    fun getAuthorities(username: String): List<String>{
        println("[CommentsDao] Serving up authorities for: \"$username\"")
        var authorities: List<String> = listOf()
        transaction(db1) {
            authorities = AuthorityTable.select { AuthorityTable.name eq username }
                    .map { it.toAuthorityModel() }.map{it.authority}
        }
        println("[CommentsDao] Finished serving up authorities for: \"$username\"")
        return authorities
    }

}