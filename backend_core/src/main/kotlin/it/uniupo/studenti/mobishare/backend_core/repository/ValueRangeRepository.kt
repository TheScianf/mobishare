package it.uniupo.studenti.mobishare.backend_core.repository

import it.uniupo.studenti.mobishare.backend_core.entity.SensorType
import it.uniupo.studenti.mobishare.backend_core.entity.ValueRange
import it.uniupo.studenti.mobishare.backend_core.entity.ValueRangeId
import it.uniupo.studenti.mobishare.backend_core.entity.VehicleType
import org.springframework.data.jpa.repository.JpaRepository

interface ValueRangeRepository: JpaRepository<ValueRange, ValueRangeId> {
    fun findByVehicleTypeAndSensorType(vehicleType: VehicleType, sensorType: SensorType): ValueRange?
}