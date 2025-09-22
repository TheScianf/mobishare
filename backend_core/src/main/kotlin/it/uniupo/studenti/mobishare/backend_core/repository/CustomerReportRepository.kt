package it.uniupo.studenti.mobishare.backend_core.repository

import it.uniupo.studenti.mobishare.backend_core.entity.CustomerReport
import it.uniupo.studenti.mobishare.backend_core.entity.CustomerReportId
import it.uniupo.studenti.mobishare.backend_core.entity.Vehicle
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional

interface CustomerReportRepository: JpaRepository<CustomerReport, CustomerReportId> {
    fun findByVehicleAndState(vehicle: Vehicle, string: String): List<CustomerReport>
    @Modifying
    @Transactional
    @Query("DELETE FROM CustomerReport cr WHERE cr.vehicle = :vehicle")
    fun deleteByVehicle(@Param("vehicle") vehicle: Vehicle): Int
}