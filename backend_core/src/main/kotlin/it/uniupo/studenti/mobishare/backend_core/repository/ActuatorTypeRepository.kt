package it.uniupo.studenti.mobishare.backend_core.repository

import it.uniupo.studenti.mobishare.backend_core.entity.ActuatorType
import org.springframework.data.jpa.repository.JpaRepository

interface ActuatorTypeRepository: JpaRepository<ActuatorType, String> {
}