package org.singularity.service

import com.mongodb.client.MongoClient
import net.odoframework.container.Application
import net.odoframework.kt.extensions.configuration
import net.odoframework.kt.extensions.get
import net.odoframework.kt.extensions.provide
import net.odoframework.service.ServiceFunction
import net.odoframework.util.ClasspathResource
import org.litote.kmongo.KMongo
import org.singularity.service.graphql.SingularityGraphQL

var client: MongoClient? = null

open class SingularityApp : Application() {


    override fun build() {
        provide<MongoClient> {
            if (client == null) {
                client = KMongo.createClient(it.configuration<String>("singularity.mongodb"))
                Runtime.getRuntime().addShutdownHook(Thread {
                    if (client != null) {
                        client!!.close()
                    }
                })
            }
            client!!
        }
        addStartupBean(provide<SystemDAO> { SystemDAO(it.get()) })
        //provide<Json> { KotlinXSerializationJson() }
        provide<SystemService> { SystemServiceImpl(it.get(), it.get()) }
        provide<SingularityApi> { SingularityApi(it.get(), it.get()) }
        provide<SingularityGraphQL> { SingularityGraphQL(it.get(), ClasspathResource("graphql/singularity.graphql")) }

        provide<SingularityGraphQLEndpoint>(ServiceFunction.NAME) {
            SingularityGraphQLEndpoint(it.get(), it.get())
        }

    }
}