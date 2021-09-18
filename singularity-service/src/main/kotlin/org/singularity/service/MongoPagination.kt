package org.singularity.service

import com.mongodb.client.FindIterable
import org.singularity.model.domain.SoftwareSystem


fun  page(query: Query, filter: ()-> FindIterable<SoftwareSystem>) : SoftwareSystemPage {
    return MongoPagination(query,  filter()).page()
}

class MongoPagination(private val query: Query, private val filter: FindIterable<SoftwareSystem>) {

    fun page(): SoftwareSystemPage {
        val count = filter.count()
        val dataSet = filter.skip(query.skipSize).limit(query.size)
        return SoftwareSystemPage(query.page, dataSet.toList(), query.createLinks(count, query.page))
    }

}