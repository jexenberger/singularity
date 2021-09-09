package org.singularity.service

const val PAGE_SIZE = 25

abstract class Query(private val path: String, val page: Int, val size: Int = PAGE_SIZE) {


    val skipSize get() = if (page == 1) 0 else PAGE_SIZE * (page - 1)

    override fun toString(): String {
        return "$path?${toParams()}"
    }

    abstract fun toParams(): String

    fun createLinks(count: Int, number: Int, path: String = toString()): List<Link> {
        val links = arrayListOf<Link>()
        val pages = count / PAGE_SIZE
        if (number < pages) {
            links.add(
                Link(
                    rel = "next",
                    link = path.replace("page=${number}", "page=${number + 1}"),
                    type = "application/json"
                )
            )
        }
        if (number > 1) {
            links.add(
                Link(
                    rel = "prev",
                    link = path.replace("page=${number}", "page=${number - 1}"),
                    type = "application/json"
                )
            )
        }
        return links
    }


}