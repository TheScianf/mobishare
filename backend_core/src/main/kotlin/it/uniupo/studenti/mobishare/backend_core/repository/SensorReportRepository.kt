package it.uniupo.studenti.mobishare.backend_core.repository

import it.uniupo.studenti.mobishare.backend_core.entity.Sensor
import it.uniupo.studenti.mobishare.backend_core.entity.SensorReport
import it.uniupo.studenti.mobishare.backend_core.entity.Vehicle
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface SensorReportRepository: JpaRepository<SensorReport, Long> {

    @Query("SELECT sr FROM SensorReport sr JOIN sr.sensor s WHERE s.vehicle = :vehicle AND sr.state = :status")
    fun findAllByVehicleAndStatus(
        @Param("vehicle") vehicle: Vehicle,
        @Param("status") status: String
    ): Set<SensorReport>
    @Modifying
    @Transactional
    @Query("DELETE FROM SensorReport sr WHERE sr.sensor = :sensor")
    fun deleteAllBySensor(@Param("sensor") sensor: Sensor): Int
    @Modifying
    @Transactional
    @Query("UPDATE SensorReport sr SET sr.state = 'D' WHERE sr.sensor = :sensor AND sr.state = 'P'")
    fun updatePendingToDeprecatedBySensor(@Param("sensor") sensor: Sensor): Int
}