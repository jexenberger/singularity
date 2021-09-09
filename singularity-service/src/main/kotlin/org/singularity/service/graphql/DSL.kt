package org.singularity.service.graphql

import graphql.schema.DataFetchingEnvironment


fun <T> DataFetchingEnvironment.argument(name: String): T? = this.arguments[name] as T?

fun DataFetchingEnvironment.setCtxValue(key: String, value:Any){
    this.getLocalContext<MutableMap<String,Any>>()[key] = value
}

fun <T> DataFetchingEnvironment.getCtxValue(key: String) : T? = this.getLocalContext<MutableMap<String,Any>>()[key] as T?

fun <T> DataFetchingEnvironment.requiredCtxValue(key: String) : T = this.getLocalContext<MutableMap<String,Any>>()[key] as T ?: throw IllegalArgumentException("$key is a required parameter")

fun <T> DataFetchingEnvironment.required(name: String): T =
    argument<T>(name) ?: throw IllegalArgumentException("$name is a required parameter")

fun <T> Map<String, Any>.valueAs(name: String) : T? = this[name] as T?


fun <T> Map<String, Any>.required(name: String) : T = this.valueAs<T>(name) ?: throw IllegalArgumentException("$name is a required parameter")



