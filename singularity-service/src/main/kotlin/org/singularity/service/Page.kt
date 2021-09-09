package org.singularity.service

data class Page<T>(val page:Int, val result:List<T>, val links:List<Link> = emptyList(), val total:Long) : AbstractList<T>(){
    override val size: Int
        get() = result.size

    override fun get(index: Int) = result[index]
}