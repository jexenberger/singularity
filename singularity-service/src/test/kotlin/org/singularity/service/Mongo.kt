package org.singularity.service

import de.flapdoodle.embed.mongo.MongodProcess
import de.flapdoodle.embed.mongo.MongodStarter
import de.flapdoodle.embed.mongo.config.MongodConfig
import de.flapdoodle.embed.mongo.config.Net
import de.flapdoodle.embed.mongo.distribution.Version
import de.flapdoodle.embed.process.runtime.Network
import net.odoframework.container.injection.Container

object Mongo {


    init {
        System.setProperty("odo.test", true.toString())
    }


    val mockedApp: Container by lazy {
        val starter = MongodStarter.getDefaultInstance()

        val port = Network.getFreeServerPort()
        val mongodConfig: MongodConfig = MongodConfig.builder()
            .version(Version.Main.PRODUCTION)
            .net(Net(port, Network.localhostIsIPv6()))
            .build()
        val mockedApp = SingularityApp().container
        mockedApp.configuration.setProperty("singularity.mongodb", "mongodb://localhost:$port")

        mongo = starter.prepare(mongodConfig).start()

        Runtime.getRuntime().addShutdownHook(Thread {
            mongo!!.stop()
        })
        return@lazy mockedApp
    }
    var mongo: MongodProcess? = null




    init {

    }

    fun start() = mongo






}