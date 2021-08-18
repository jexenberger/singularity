package org.singularity.service

import com.mongodb.client.MongoClient
import org.singularity.model.domain.SoftwareSystem

class SystemServiceImpl(client: MongoClient, val dao:SystemDAO = SystemDAO(client)) : SystemService by dao {

    override fun save(system: SoftwareSystem): String {
        return get(system.id)?.let {
            val newSystem = it.update(system.name, system.blurb, system.owner, system.nextDeliverableDate)
            dao.save(newSystem.calcState())
            return it.id
        } ?: dao.save(system.calcState())
    }
}