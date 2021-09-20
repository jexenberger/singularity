package org.singularity.service

import net.odoframework.service.InvocationContext
import java.util.*

class MockInvocationContext() : InvocationContext<String> {
    override fun getRequestId(): String {
        return UUID.randomUUID().toString()
    }

    override fun getRequestContext(): String {
        return UUID.randomUUID().toString()
    }

    override fun getRawPayload(): Any {
        return ""
    }
}