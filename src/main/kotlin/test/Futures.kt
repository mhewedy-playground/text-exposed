package test

import java.util.concurrent.CompletableFuture
import kotlin.concurrent.thread

typealias Promise<T> = CompletableFuture<T>


fun getHello(): Promise<String> {

    return Promise.supplyAsync {
        Thread.sleep(2000)
        "hello promise"
    }
}

fun main() {
    val hello = getHello()
    hello.whenComplete { t, _ -> println(t) }
    println("done calling getHello()")

    thread { Thread.sleep(3000) }
}