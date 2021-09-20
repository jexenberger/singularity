package org.singularity.service

import net.odoframework.container.util.Json
import net.odoframework.kt.extensions.get
import net.odoframework.service.ServiceFunction
import net.odoframework.service.web.SimpleWebRequest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SingularityGraphQLEndpointTest {

    val app = Mongo.mockedApp

    val query = """
        query {
            byId(id: "1") {
                id
                name
                alpha(name: Stakeholders) {
                   name
                }            
            }
        }
    """


    @BeforeEach
    internal fun setUp() {
        app.get<SystemDAO>()!!.clearCollection()
    }

    @Test
    internal fun testApply() {
        val service = app.get<SingularityGraphQLEndpoint>(ServiceFunction.NAME)!!
        val json = app.get<Json>()!!
        val t = SimpleWebRequest(json)
        t.body = query
        service.apply(t, MockInvocationContext())
    }
}