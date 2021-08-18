package org.singularity.service

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.ReplaceOptions
import com.mongodb.client.model.UpdateOptions
import org.litote.kmongo.*;
import org.singularity.model.domain.SoftwareSystem

class SystemDAO(private val mongo: MongoClient) : SystemService{

    private val database: MongoDatabase  by lazy {
        mongo.getDatabase("singularity")!!
    }

    private val collection: MongoCollection<SoftwareSystem> by lazy {
        database.getCollection<SoftwareSystem>("software_system")
    }

    override fun save(system: SoftwareSystem): String {
       collection.replaceOneById(system.id, system, ReplaceOptions().upsert(true))
       return system.id
    }

    override fun get(id: String) : SoftwareSystem? {
        return collection.findOne(SoftwareSystem::id eq id)
    }

    override fun getAll(query: SystemQuery): List<SoftwareSystem> {
        return collection.find().toList()
    }


}