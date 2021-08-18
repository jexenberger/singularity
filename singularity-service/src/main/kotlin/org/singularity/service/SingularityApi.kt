package org.singularity.service

import net.odoframework.container.util.Json
import net.odoframework.service.extensions.fromPath
import net.odoframework.service.extensions.get
import net.odoframework.service.extensions.post
import net.odoframework.service.extensions.put
import net.odoframework.service.web.HttpRouter
import org.singularity.model.domain.SoftwareSystem
import org.singularity.model.domain.TeamMember

class SingularityApi(private val service: SystemService, json: Json) : HttpRouter(json) {


    override fun build() {
        post<SoftwareSystem>("/system") { it, _ ->
            created("/system/${service.save(it)}")
        }
        get("/system") { req ->
            ok(Page(1, service.getAll(SystemQuery())))
        }
        get("/system/{id}") { req ->
            service.get(req.fromPath("id"))?.let {
                ok(it)
            } ?: notFound()
        }
        put<SoftwareSystem>("/system/{id}") { system, _ ->
            ok(service.save(system))
        }
        put<List<TeamMember>>("/system/{id}/team") { team, req ->
            ok(service.saveTeam(req.fromPath("id"), team))
        }
        get("/system/{id}/alphas/{alphaId}/states/{stateId}") { req ->
            ok(service.getState(req.fromPath("id"), req.fromPath("stateId")))
        }
        put<CheckListActivation>("/system/{id}/alphas/{alphaId}/states/{stateId}/card/activation") { act, req ->
            val user = req.userPrincipal.map { it.name }  ?: ""
            ok(service.activation(req.fromPath("id"),req.fromPath("stateId"),user.toString(),act))
        }

    }
}