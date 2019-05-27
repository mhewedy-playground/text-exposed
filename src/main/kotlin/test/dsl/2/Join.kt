package test.dsl.`2`

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {

    Database.connect("jdbc:h2:mem:test;DATABASE_TO_UPPER=false", driver = "org.h2.Driver")

    transaction {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(Cities, Users)

        (Users innerJoin Cities)
            .slice(Users.name, Cities.name)
            .selectAll()
            .andWhere { Cities.name like "%%" }
            .fetchSize(40)
            .forEach { println(it) }
    }

}

object Users : IntIdTable() {
    val name = varchar("name", 255)
    val city = reference("city_id", Cities).nullable()
}

object Cities : IntIdTable() {
    val name = varchar("name", 255)
}