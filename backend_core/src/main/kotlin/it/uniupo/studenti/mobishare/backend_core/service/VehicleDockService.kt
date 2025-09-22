package it.uniupo.studenti.mobishare.backend_core.service

import it.uniupo.studenti.mobishare.backend_core.entity.Dock
import it.uniupo.studenti.mobishare.backend_core.entity.Vehicle
import it.uniupo.studenti.mobishare.backend_core.entity.VehicleDock
import it.uniupo.studenti.mobishare.backend_core.entity.VehicleDockId
import it.uniupo.studenti.mobishare.backend_core.repository.VehicleDockRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

interface VehicleDockService {
    fun getVehicleDockByDock(dock: Dock): VehicleDock?
    fun getVehicleDockByVehicle(vehicle: Vehicle): VehicleDock?
    fun addVehicleDock(vehicle: Vehicle, dock: Dock)
    fun removeVehicleDock(vehicleId: Int)
    fun updateVehicleDock(vehicle: Vehicle, dock: Dock)
}

@Service
@Transactional
class VehicleDockServiceImpl: VehicleDockService{

    @Autowired
    lateinit var vehicleDockRepository: VehicleDockRepository

    /**
     * Finds a VehicleDock by dock.
     *
     * @param dock the dock
     * @return the VehicleDock found, or null if no VehicleDock is found
     */
    override fun getVehicleDockByDock(dock: Dock): VehicleDock?{
        return vehicleDockRepository.findByDock(dock)
    }
    /**
     * Finds a VehicleDock by vehicle.
     *
     * @param vehicle the vehicle
     * @return the VehicleDock found, or null if no VehicleDock is found
     */
    override fun getVehicleDockByVehicle(vehicle: Vehicle): VehicleDock?{
        return vehicleDockRepository.findByVehicle(vehicle)
    }

    /**
     * Adds a VehicleDock by giving a vehicle and a dock.
     *
     * @param vehicle the vehicle
     * @param dock the dock
     */
    override fun addVehicleDock(
        vehicle: Vehicle,
        dock: Dock
    ) {
        val vehicleDock = VehicleDock().apply {
            this.vehicle = vehicle
            this.dock = dock
            this.id = VehicleDockId()
        }
        vehicleDockRepository.save(vehicleDock)
    }


    /**
     * Deletes a VehicleDock by vehicleId.
     *
     * @param vehicleId the id of the vehicle to delete the VehicleDock for
     */
    override fun removeVehicleDock(vehicleId: Int) {
        vehicleDockRepository.deleteByVehicleId(vehicleId)
    }

    /**
     * Updates a VehicleDock by vehicleId and dockId.
     *
     * @param vehicle the vehicle to update the VehicleDock for
     * @param dock the dock to set in the VehicleDock
     */
    override fun updateVehicleDock(
        vehicle: Vehicle,
        dock: Dock
    ) {
        vehicleDockRepository.updateDockByVehicle(vehicle, dock)
    }
}