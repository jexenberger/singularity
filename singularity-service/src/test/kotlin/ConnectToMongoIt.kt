import net.odoframework.container.util.Json
import net.odoframework.kt.extensions.get
import org.junit.jupiter.api.Test
import org.singularity.model.domain.SoftwareSystem
import org.singularity.service.SingularityApp
import org.singularity.service.SystemService
import java.time.LocalDate
import java.util.*

class ConnectToMongoIt {


    @Test
    internal fun connectToMongo() {
        val container = SingularityApp().container
        val dao = container.get<SystemService>() ?: throw RuntimeException("borked")

        val id = UUID.randomUUID().toString()
        val system = SoftwareSystem(
            _id = id,
            name = "test",
            blurb = "A test",
            owner = "Acme Inc.",
            nextDeliverableDate = LocalDate.now()
        )
        dao.save(system = system)

        dao.get(id)?.let {
            println(container.get<Json>()!!.marshal(it))
        } ?: throw RuntimeException("borked")

    }
}