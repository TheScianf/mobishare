package it.uniupo.studenti.mobishare.backend_core.repository

import it.uniupo.studenti.mobishare.backend_core.entity.Manager
import org.springframework.data.jpa.repository.JpaRepository

interface ManagerRepository: JpaRepository<Manager, Int> {
    fun findByEmail(email: String): Manager?
}