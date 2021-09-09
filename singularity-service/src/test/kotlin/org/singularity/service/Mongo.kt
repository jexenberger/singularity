package org.singularity.service

import de.flapdoodle.embed.mongo.MongodProcess
import de.flapdoodle.embed.mongo.MongodStarter
import de.flapdoodle.embed.mongo.config.MongodConfig
import de.flapdoodle.embed.mongo.config.Net
import de.flapdoodle.embed.mongo.distribution.Version
import de.flapdoodle.embed.process.runtime.Network

object Mongo {

    val mockedApp = SingularityApp().container
    val mongo: MongodProcess


    init {
        val starter = MongodStarter.getDefaultInstance()

        val port = Network.getFreeServerPort()
        val mongodConfig: MongodConfig = MongodConfig.builder()
            .version(Version.Main.PRODUCTION)
            .net(Net(port, Network.localhostIsIPv6()))
            .build()

        mockedApp.configuration["singularity.mongodb.connection.string"] = "mongodb://localhost:${port}"
        mongo = starter.prepare(mongodConfig).start()

        Runtime.getRuntime().addShutdownHook(Thread {
            mongo.stop()
        })
    }

    fun start() = mongo






}