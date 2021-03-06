package test.dsl.`1`

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.concurrent.thread

fun main() {

    // to be able to monitor the app via jvisualvm
    thread {

        Database.connect("jdbc:h2:mem:test;DATABASE_TO_UPPER=false", driver = "org.h2.Driver")

        transaction {
            addLogger(StdOutSqlLogger)

            SchemaUtils.create(Cities)

            val cities = arrayListOf(
                City("Cairo", 20_000_000),
                City("Madrid", 20_000),
                City("Riyadh", 99_933),
                City("Hidrbad", 20_000_000),
                City("Newyork", 20_000),
                City("Bangladish", 20_000),
                City("Kazablanka", 10_000_000)
            )

            Cities.batchInsert(cities) {
                this[Cities.name] = it.name
                this[Cities.population] = it.population
            }

            Cities.slice(Cities.id, Cities.name, Cities.population)
                .selectAll()
                .andWhere { Cities.population greaterEq 10 }
                .andWhere { Cities.name like "Cai%" }
                .orderBy(Cities.id)
                .forEach { println(it) }
        }
    }

}

object Cities : IntIdTable() {
    val name = varchar("name", 255).nullable()
    val population = integer("population").nullable()
}

data class City(val name: String?, val population: Int?, val id: Int? = null)