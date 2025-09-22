package it.uniupo.studenti.mobishare.backend_core.dao

import it.uniupo.studenti.mobishare.backend_core.entity.Customer
import it.uniupo.studenti.mobishare.backend_core.entity.Suspension
import it.uniupo.studenti.mobishare.backend_core.sumOf
import java.util.concurrent.atomic.AtomicReference

private enum class GENDER(val abbrev: String, val label: String) {
    MALE("M", "Male"),
    FEMALE("F", "Female"),
    NON_BINARY("X", "Non Binary");

    companion object {
        fun getFromAbbrev(abbrev: String): GENDER {
           return values().firstOrNull { it.abbrev == abbrev } ?: NON_BINARY
        }
    }
}

data class DAOCustomerOnlyNoPassword(
    val username: String,
    val email: String,
    val name: String,
    val surname: String,
    val gender: String,
    val cf: String,
) {
    constructor(c: Customer): this(
        c.usernameU,
        c.email,
        c.name,
        c.surname,
        GENDER.getFromAbbrev(c.gender).label,
        c.cf,
    )
}

data class DAOCustomerWithSuspension(
    val username: String,
    val email: String,
    val name: String,
    val surname: String,
    val start: Long,
    val end: Long?,
    val value: Float,
    val paid: Boolean
) {
    constructor(c: Customer, s: Suspension): this(
        c.usernameU,
        c.email,
        c.name,
        c.surname,
        s.id.start.toEpochMilli(),
        s.end?.toEpochMilli(),
        c.transactions.sumOf { it.value }.let {
            tempTotal.set(it)
            return@let it
        },
        tempTotal.get() >= s.value
    )

    companion object {
        //atomic ref to avoid any kind of concurrency issues (could, could not? does not matter)
        private var tempTotal: AtomicReference<Float> = AtomicReference(0f);
    }
}
