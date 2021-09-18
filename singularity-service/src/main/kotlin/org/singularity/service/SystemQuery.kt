package org.singularity.service

import net.odoframework.service.web.WebRequest

class SystemQuery(
    val name: String? = null,
    val owner: String? = null,
    val risk: Int? = null,
    page: Int = 1,
    size: Int = 10,
) : Query("/systems",page, size){

    constructor(
        request: WebRequest,
        name: String? = request.getQueryParam("name").orElse(null),
        owner: String? = request.getQueryParam("owner").orElse(null),
        risk: Int? = request.getQueryParamAsLong("risk").map { it.toInt() }.orElse(null),
        pageNumber: Int = request.getQueryParamAsLong("page").map { it.toInt() }.orElse(1),
        size: Int = request.getQueryParamAsLong("size").map { it.toInt() }.orElse(10),
    ) : this(name, owner, risk, pageNumber, size)

    override fun toParams(): String {
        var query = ""
        query += if (name != null) "name=$name&" else ""
        query += if (owner != null) "owner=$owner&" else ""
        query += if (risk != null) "risk=$risk&" else ""
        query += "page=$page"
        return if (query.endsWith("&")) query.substring(0, query.length - 1) else query
    }
}
