package it.uniupo.studenti.mobishare.backend_core.repository

import it.uniupo.studenti.mobishare.backend_core.entity.Dock
import it.uniupo.studenti.mobishare.backend_core.entity.Vehicle
import it.uniupo.studenti.mobishare.backend_core.entity.VehicleDock
import it.uniupo.studenti.mobishare.backend_core.entity.VehicleDockId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional

interface VehicleDockRepository: JpaRepository<VehicleDock, VehicleDockId> {
    fun findByDock(dock: Dock) : VehicleDock?
    fun findByVehicle(vehicle: Vehicle): VehicleDock?
    @Modifying
    @Transactional
    @Query("DELETE FROM VehicleDock vd WHERE vd.vehicle.id = :vehicleId")
    fun deleteByVehicleId(@Param("vehicleId") vehicleId: Int): Int

    @Modifying
    @Transactional
    @Query("UPDATE VehicleDock vd SET vd.dock = :dock WHERE vd.vehicle = :vehicle")
    fun updateDockByVehicle(@Param("vehicle") vehicle: Vehicle, @Param("dock") dock: Dock): Int
}