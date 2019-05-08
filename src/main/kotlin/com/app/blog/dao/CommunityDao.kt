package com.app.blog.dao

import com.app.blog.model.Community
import com.app.blog.model.Post
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class CommunityDao constructor(
        val db1: Database
) {

    object CommunityTable : Table("communities") {
        val id = integer("id").primaryKey()
        val community_name = varchar("community_name",25)
    }


    fun CommunityTable.rowToCommunity(row: ResultRow): Community {
        return Community(id = row[id], communityName = row[community_name])
    }

    fun selectAll(): List<Community> = transaction(db1) { CommunityTable.selectAll().map{ Community(id = it[CommunityTable.id], communityName = it[CommunityTable.community_name]) } }

    fun insert(community: Community) {
        transaction(db1) {
            CommunityTable.insert {
                println("[CommunityDao] Adding community ${community.communityName}")
                it[community_name] = community.communityName
            }
        }
    }

    fun getIdForName(name: String): Int? {
        var x: Community? = null
        transaction(db1) {
            x = CommunityTable.select { CommunityTable.community_name eq name }.map{it.toCommunity()}.first()
        }
        return x?.id
    }


    private fun ResultRow.toCommunity() = CommunityTable.rowToCommunity(this)

}