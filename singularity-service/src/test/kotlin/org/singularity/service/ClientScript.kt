package org.singularity.service

import net.odoframework.http.HttpRequest
import net.odoframework.http.URLConnectionHttpService
import java.net.URI

val endpoint = "xxxx"
val apiKey = "xxx"

fun main(args: Array<String>) {
    ClientScript.callApi()
}

object ClientScript {

    fun readQuery(): String {
        println("Enter query: ")
        return System.`in`.bufferedReader().readLine();
    }

    fun callApi(query: String = readQuery()) {
        val http = URLConnectionHttpService()
        val httpRequest = HttpRequest.post(URI(endpoint))
            .header("x-api-key", apiKey)
            .body(query)
        val result = http.execute(httpRequest)
        println("RESULT-----")
        if (result.isLeft) {
            println(result.left().bodyAsString())
        } else {
            println(result.right().bodyAsString())
        }
    }


}