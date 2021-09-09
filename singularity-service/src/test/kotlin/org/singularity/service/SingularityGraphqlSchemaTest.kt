package org.singularity.service

import graphql.ExecutionInput
import net.odoframework.kt.extensions.get
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.singularity.service.graphql.SingularityGraphQL
import java.util.*
import kotlin.test.assertTrue


class SingularityGraphqlSchemaTest {

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

    private val app = Mongo.mockedApp

    private val createSoftwareSystem = createSoftwareSystem()

    private fun createSoftwareSystem(id: String = UUID.randomUUID().toString(), teamId: String = UUID.randomUUID().toString()) = """
                query {
                        save(system: {
                            id: "${id}"
                            name: "Test",
                            blurb: "The test system to accomplish the test objective",
                            owner: "Acme Inc"
                            nextDeliveryDate: "2030-01-01",
                            team: [
                                {
                                    id : "$teamId",
                                    name: "Bob Jones",
                                    organisation: "CoderX",
                                    competency: [Development],
                                    email: "bob.jones@coderx.com",
                                    number: "1231231234",
                                }
                            ]
                            
                        }) {
                          id,
                          alpha(name: Stakeholders) {
                            name
                          }
                        }
                }
            """

    @Test
    internal fun testSaveSoftwareSystem() {
        app.get<SystemDAO>()?.clearCollection()
        val executionResult  = execute<Any>(createSoftwareSystem)
        println(executionResult.toString())
    }

    @Test
    internal fun testFind() {
        app.get<SystemDAO>()?.clearCollection()
        val createSoftwareSystem = createSoftwareSystem
        repeat(15) {
            execute(createSoftwareSystem(it.toString()))
        }

        val find = """
            query {
                byFilter(page:1, size:12) {
                    id
                    name
                }
            }
        """.trimIndent()
        println(this.execute<Any>(find))
    }
    @Test
    internal fun testCheckCardItem() {


        app.get<SystemDAO>()?.clearCollection()
        execute<Any>(createSoftwareSystem("test"))

        val find = """
            query {
                byId(id: "test") {
                    id
                    name
                    alpha(name: Stakeholders) {
                       name
                       state(id: "Recognized") {
                         id
                         name
                         updateCard(newCard: [
                            { id: 0, answer: true }
                         ]) {
                           id
                           answer
                         }
                       }
                    }            
                }
        }
        """.trimIndent()
        println(this.execute<Any>(find))
    }

    @Test
    internal fun testAddTeamMember() {
        app.get<SystemDAO>()?.clearCollection()

        val createSoftwareSystem = createSoftwareSystem("test", "team01")
        execute<Map<String, Any>>(createSoftwareSystem)

        val addTeamMember = """
            query {
              byId(id: "test") {
                id
                saveTeamMember(input: {
                    name: "Alice Smith",
                    organisation: "Acme Inc",
                    competency: [Testing, Development],
                    organisation: "MeGoTest"
                }) {
                  id
                }
              }
            }
        """.trimIndent()
        println(execute<Any>(addTeamMember))

        val lookup = """
            query {
                byId(id: "test") {
                    id
                    name
                    team {
                        name
                        competency
                    }
                }
            }
        """.trimIndent()

        val data = execute<Any>(lookup).toString()
        assertTrue { data.contains("Alice Smith") }

        val updateExisting = """
            query {
              byId(id: "test") {
                id
                saveTeamMember(input: {
                    id: "team01",
                    name: "Jim Jones",
                    organisation: "Acme Inc",
                    competency: [Development],
                    organisation: "MeGoTest"
                }) {
                  id
                  name
                }
              }
            }
        """.trimIndent()

        execute<Any>(updateExisting).toString()
        val dataExisting = execute<Any>(lookup).toString()
        assertTrue { dataExisting.contains("Jim Jones") }


    }

    private fun <T> execute(query: String) : T {
        val service = app.get<SingularityGraphQL>()!!
        val execution = ExecutionInput
            .newExecutionInput(query)
            .localContext(mutableMapOf<String, String>()).build()
        val message = service.execute(execution)
        if (!message.isDataPresent) {
            fail { message.toString() }
        }
        return message.getData()
    }
}