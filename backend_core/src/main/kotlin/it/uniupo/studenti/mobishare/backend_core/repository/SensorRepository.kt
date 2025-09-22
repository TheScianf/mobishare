package it.uniupo.studenti.mobishare.backend_core.repository

import it.uniupo.studenti.mobishare.backend_core.entity.Sensor
import it.uniupo.studenti.mobishare.backend_core.entity.Vehicle
import org.springframework.data.jpa.repository.JpaRepository

interface SensorRepository: JpaRepository<Sensor, Int> {
    fun findAllByVehicle(vehicle: Vehicle): List<Sensor>?
}