package it.uniupo.studenti.mobishare.backend_core.repository

import it.uniupo.studenti.mobishare.backend_core.entity.Vehicle
import org.springframework.data.jpa.repository.JpaRepository

interface VehicleRepository: JpaRepository<Vehicle, Int> {
}