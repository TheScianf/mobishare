package it.uniupo.studenti.mobishare.backend_core.repository

import it.uniupo.studenti.mobishare.backend_core.entity.Suspension
import it.uniupo.studenti.mobishare.backend_core.entity.SuspensionId
import org.springframework.data.jpa.repository.JpaRepository

interface SuspensionRepository: JpaRepository<Suspension, SuspensionId> {
    fun searchByCustomerUsernameU(username: String): List<Suspension>
    fun findByIsRejectedNull(): List<Suspension>
    fun findByIsRejectedNotNull(): List<Suspension>
    fun findByCustomerUsernameUAndIsRejected(username: String, isRejected: Boolean): List<Suspension>
    fun findByCustomerUsernameUAndIsRejectedNull(username: String): List<Suspension>
}
