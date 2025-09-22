package it.uniupo.studenti.mobishare.backend_core.dao

import it.uniupo.studenti.mobishare.backend_core.entity.Suspension
import java.util.*

data class DAOActiveSuspensionOnly(
    val customerUsername: String,
    val start: Date,
    val end: Date?,
    val value: Float,
) {
    constructor(s: Suspension): this(
        s.customer.usernameU,
        Date(s.id.start.toEpochMilli()),
        if (s.end != null) Date(s.end!!.toEpochMilli()) else null,
        s.value,
    )
}

data class DAOSuspensionRejectStatus(
    val isRejected: Boolean
)
