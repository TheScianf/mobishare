package it.uniupo.studenti.mobishare.backend_core.dao
import it.uniupo.studenti.mobishare.backend_core.entity.Customer
import it.uniupo.studenti.mobishare.backend_core.entity.Race
import it.uniupo.studenti.mobishare.backend_core.entity.RaceId

data class DAORaceOnly(
    val id: RaceId,
    val customer: String,
    val minDuration: Int
) {
    constructor(race: Race) : this(
        race.id,
        race.customer.usernameU,
        race.minDuration
    )
}
