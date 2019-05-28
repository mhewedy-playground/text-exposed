package test.dao

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.LongIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {

    Database.connect("jdbc:h2:mem:test;DATABASE_TO_UPPER=false", driver = "org.h2.Driver")

    transaction {

        SchemaUtils.createMissingTablesAndColumns(Movies)
        addLogger(StdOutSqlLogger)

        Movie.new {
            name = "men in black"
            year = 1998
        }

        Movie.new {
            name = "menem in belal"
            year = 1999
        }

        // DAO style
        val daoResult = Movie.find { Movies.name like "%in%" }
            .orderBy(Movies.year to SortOrder.ASC)
            .toList()
        println("daoResult= $daoResult")

        // DSL style
        val query = Movies.select { Movies.name like "%in%" }
            .orderBy(Movies.name)
        println("pure dsl= ${query.toList()}")

        val dslResult = Movie.wrapRows(query).toList()

        println("dslResult= $dslResult")
    }
}

object Movies : LongIdTable() {
    val name = varchar("name", 255)
    val year = integer("year")
}

class Movie(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<Movie>(Movies)

    var name by Movies.name
    var year by Movies.year

    override fun toString() = "name: $name, year: $year"
}