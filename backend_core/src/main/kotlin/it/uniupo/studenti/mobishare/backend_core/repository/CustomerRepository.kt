package it.uniupo.studenti.mobishare.backend_core.repository

import it.uniupo.studenti.mobishare.backend_core.entity.Customer
import org.springframework.data.jpa.repository.JpaRepository

interface CustomerRepository: JpaRepository<Customer, String> {
    fun findByUsernameU(username: String):  Customer?
}