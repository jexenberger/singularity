package org.singularity.service

import net.odoframework.container.util.Json
import net.odoframework.kt.extensions.get
import net.odoframework.kt.extensions.post
import net.odoframework.kt.extensions.put
import net.odoframework.kt.extensions.toObject
import net.odoframework.service.MockInvocationContext
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.singularity.model.domain.Competency
import org.singularity.model.domain.SoftwareSystem
import org.singularity.model.domain.TeamMember
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class SingularityApiTest {

    val app = Mongo.mockedApp


    @BeforeEach
    internal fun setUp() {
        app.get<SystemDAO>()!!.clearCollection()
    }

    @Test
    internal fun createSaveSoftwareSystem() {
        val api = app.get<SingularityApi>()!!
        val json = app.get<Json>()!!

        val system = createSystem()
        val context = MockInvocationContext(system)
        val request = get(json) {
            path = "/systems"
            method = "POST"
            setBodyAsObject(system)
        }

        val result = api.apply(request, context)
        println(result.headers["Location"])
        assertEquals(201, result.statusCode)
        assertTrue { result.headers.containsKey("Location") }
        assertTrue { result.headers["Location"]!!.startsWith("/systems/") }

        val lookup = get(json) {
            path = result.headers["Location"]
        }

        val foundInstance = api.apply(lookup, context)
        assertEquals(200, foundInstance.statusCode)

        val existingSystem = json.unmarshal(foundInstance.body, SoftwareSystem::class.java)
        val updatedSystem = updateSystem(existingSystem, "My updated name")
        val update = put(json) {
            path = result.headers["Location"]
            setBodyAsObject(updatedSystem)
        }
        val updateResult = api.apply(update, context)
        assertEquals(200, updateResult.statusCode)
        assertEquals("My updated name", updateResult.toObject<SoftwareSystem>().name)
    }

    private fun createSystem(name: String = "Test") = SoftwareSystem(
        name = name,
        blurb = "This is a call to all my mass resignations",
        owner = "Acme Inc",
        nextDeliverableDate = LocalDate.now().plusMonths(3),
    )

    private fun updateSystem(system: SoftwareSystem, newName: String) = system.update(name = newName)


    @Test
    internal fun findAll() {
        val api = app.get<SingularityApi>()!!
        val json = app.get<Json>()!!
        val context = MockInvocationContext(createSystem())
        (1..101).forEach {
            val system = createSystem(name = "Test$it")

            val req = post(json) {
                path = "/systems"
                setBodyAsObject(system)
            }
            api.apply(req, context)
        }

        val query = get(json) {
            path = "/systems"
            addQueryParam("name", "Test")
            addQueryParam("page", 1)
            addQueryParam("size", 25)
        }
        val result = api.apply(query, context)
        val page = result.toObject<SoftwareSystemPage>()
        assertEquals(25, page.result.size)
        assertEquals(1, page.links.size)
        assertEquals("next", page.links[0].rel)

        val lastPageQuery = get(json) {
            path = "/systems"
            addQueryParam("name", "Test")
            addQueryParam("page", 5)
            addQueryParam("size", 25)
        }
        val lastPage = api.apply(lastPageQuery, context).toObject<SoftwareSystemPage>()
        assertEquals(1, lastPage.result.size)
        assertEquals(1, lastPage.links.size)
        assertEquals("prev", lastPage.links[0].rel)

        val middlePageQuery = get(json) {
            path = "/systems"
            addQueryParam("name", "Test")
            addQueryParam("page", 3)
            addQueryParam("size", 25)
        }
        val middlePage = api.apply(middlePageQuery, context).toObject<SoftwareSystemPage>()
        assertEquals(25, middlePage.result.size)
        assertEquals(2, middlePage.links.size)
        assertEquals("next", middlePage.links[0].rel)
        assertEquals("prev", middlePage.links[1].rel)
    }


    @Test
    internal fun testSaveTeamMembers() {
        val api = app.get<SingularityApi>()!!
        val json = app.get<Json>()!!
        val context = MockInvocationContext(createSystem())
        val candidateSystem = createSystem(name = "Test_teams")
        assertTrue { candidateSystem.team.isEmpty() }

        val req = post(json) {
            path = "/systems"
            setBodyAsObject(candidateSystem)
        }
        val system = api.apply(req, context).headers["Location"]!!.split("/")[2]

        val teamMember: TeamMember = TeamMember(
            name = "Bob Jones",
            organisation = "Acme Inc",
            competency = arrayListOf(Competency.Development)
        )
        val saveTeam = put(json) {
            path = "/systems/${system}/team"
            setBodyAsObject(TeamPage(0, listOf(teamMember)))
        }
        assertEquals(200, api.apply(saveTeam, context).statusCode)
        val getTeam = get(json) {
            path = "/systems/${system}"
        }
        val lookupResult = api.apply(getTeam, context)
        val saveSystem = lookupResult.toObject<SoftwareSystem>()
        assertEquals(1, saveSystem.team.size)



    }
}