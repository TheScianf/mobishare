package it.uniupo.studenti.mobishare.backend_core.repository

import it.uniupo.studenti.mobishare.backend_core.entity.Maintenance
import it.uniupo.studenti.mobishare.backend_core.entity.Vehicle
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

interface MaintenanceRepository: JpaRepository<Maintenance, Int> {
    fun findAllByVehicle(vehicle: Vehicle): List<Maintenance>
    fun findAllByEndIsNull(): List<Maintenance>
    fun findByVehicleAndEndIsNull(vehicle: Vehicle): Maintenance?
    @Modifying
    @Transactional
    @Query("UPDATE Maintenance m SET m.end = :endDate WHERE m.vehicle = :vehicle AND m.end IS NULL")
    fun updateEndDateByVehicle(@Param("vehicle") vehicle: Vehicle, @Param("endDate") endDate: LocalDate): Int
}