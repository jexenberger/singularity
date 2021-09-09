package org.singularity.service

import com.mongodb.client.MongoClient
import net.odoframework.container.Application
import net.odoframework.kt.extensions.get
import net.odoframework.kt.extensions.provide
import net.odoframework.util.ClasspathResource
import org.litote.kmongo.KMongo
import org.singularity.service.graphql.SingularityGraphQL

class SingularityApp: Application() {
    override fun build() {
        provide<MongoClient> {
            KMongo.createClient(it.value("singularity.mongodb.connection.string")!!)
        }
        provide<SystemService> { SystemServiceImpl(it.get()) }
        provide<SingularityApi> { SingularityApi(it.get(), it.get()) }
        provide<SingularityGraphQL> { SingularityGraphQL(it.get(),ClasspathResource("graphql/singularity.graphql")) }
    }
}