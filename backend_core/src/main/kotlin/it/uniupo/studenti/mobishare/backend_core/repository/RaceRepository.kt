package it.uniupo.studenti.mobishare.backend_core.repository

import it.uniupo.studenti.mobishare.backend_core.entity.Customer
import it.uniupo.studenti.mobishare.backend_core.entity.Race
import it.uniupo.studenti.mobishare.backend_core.entity.RaceId
import org.springframework.data.jpa.repository.JpaRepository

interface RaceRepository: JpaRepository<Race, RaceId> {
    fun findAllByCustomer(customer: Customer): List<Race>?
}
