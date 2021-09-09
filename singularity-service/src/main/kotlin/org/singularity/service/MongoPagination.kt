package org.singularity.service

import com.mongodb.client.FindIterable


fun <T> page(query: Query, filter: ()-> FindIterable<T>) : Page<T> {
    return MongoPagination(query,  filter()).page()
}

class MongoPagination<T>(private val query: Query, private val filter: FindIterable<T>) {

    fun page(): Page<T> {
        val count = filter.count()
        val dataSet = filter.skip(query.skipSize).limit(query.size)
        return Page(query.page, dataSet.toList(), query.createLinks(count, query.page), count.toLong())
    }

}