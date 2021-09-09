package org.singularity.service

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.ReplaceOptions
import org.bson.conversions.Bson
import org.litote.kmongo.*;
import org.singularity.model.domain.SoftwareSystem

class SystemDAO(private val mongo: MongoClient) : SystemService {

    fun clearCollection() {
        this.collection.deleteMany("{}")
    }

    private val database: MongoDatabase by lazy {
        mongo.getDatabase("singularity")!!
    }

    private val collection: MongoCollection<SoftwareSystem> by lazy {
        database.getCollection<SoftwareSystem>("software_system")
    }

    override fun save(system: SoftwareSystem, replace:Boolean): SoftwareSystem {
        collection.replaceOneById(system.id, system, ReplaceOptions().upsert(true))
        return system
    }

    override fun get(id: String): SoftwareSystem? {
        return collection.findOne(SoftwareSystem::id eq id)
    }

    override fun find(queryParameters: SystemQuery): Page<SoftwareSystem> {
        val filters = listOf<Bson>()
        queryParameters.name?.let { filters + (queryParameters::name regex "^${it}") }
        queryParameters.owner?.let { filters + (queryParameters::owner regex "^${it}") }
        queryParameters.risk?.let { filters + (queryParameters::risk lte it) }

        return page(queryParameters) {
            collection
                .find<SoftwareSystem>(and(filters))
                .sort(ascending(queryParameters::name))
        }
    }


}