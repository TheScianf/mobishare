package it.uniupo.studenti.mobishare.backend_core.repository

import it.uniupo.studenti.mobishare.backend_core.entity.SensorType
import org.springframework.data.jpa.repository.JpaRepository

interface SensorTypeRepository: JpaRepository<SensorType, Int> {
}