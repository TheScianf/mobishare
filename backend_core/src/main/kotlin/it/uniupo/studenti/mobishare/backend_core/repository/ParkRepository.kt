package it.uniupo.studenti.mobishare.backend_core.repository

import it.uniupo.studenti.mobishare.backend_core.entity.Park
import org.springframework.data.jpa.repository.JpaRepository

interface ParkRepository: JpaRepository<Park, Int> {
}