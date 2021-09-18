package org.singularity.service

import com.mongodb.client.MongoClient
import org.singularity.model.domain.SoftwareSystem

class SystemServiceImpl(client: MongoClient, private val dao:SystemDAO = SystemDAO(client)) : SystemService by dao {

    override fun save(system: SoftwareSystem, replace:Boolean): SoftwareSystem {
        if (replace) {
            dao.save(system.calcState())
        }
        return get(system._id)?.let {
            val newSystem = it.update(system.name, system.blurb, system.owner, system.nextDeliverableDate)
            dao.save(newSystem.calcState())
            return newSystem
        } ?: dao.save(system.calcState())
    }
}