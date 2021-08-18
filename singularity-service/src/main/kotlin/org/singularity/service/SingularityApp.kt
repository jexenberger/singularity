package org.singularity.service

import com.mongodb.client.MongoClient
import net.odoframework.container.Application
import net.odoframework.kt.extensions.provide
import org.litote.kmongo.KMongo

import net.odoframework.kt.extensions.*

class SingularityApp: Application() {
    override fun build() {
        provide<MongoClient> { KMongo.createClient() }
        provide<SystemService> { SystemServiceImpl(it.get()) }
    }
}