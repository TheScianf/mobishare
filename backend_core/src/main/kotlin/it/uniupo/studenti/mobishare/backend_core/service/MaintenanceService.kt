package it.uniupo.studenti.mobishare.backend_core.service

import it.uniupo.studenti.mobishare.backend_core.dao.DAOActiveMaintenance
import it.uniupo.studenti.mobishare.backend_core.entity.Maintenance
import it.uniupo.studenti.mobishare.backend_core.entity.Vehicle
import it.uniupo.studenti.mobishare.backend_core.repository.MaintenanceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate

interface MaintenanceService {
    fun isMaintaining(vehicle: Vehicle): Boolean
    fun getActiveMaintenances(): List<DAOActiveMaintenance>
    fun newMaintenance(vehicle: Vehicle, description: String?)
    fun endVehicleMaintenance(vehicle: Vehicle)
}

@Service
class MaintenanceServiceImpl: MaintenanceService {

    @Autowired
    private lateinit var maintenanceRepository: MaintenanceRepository

    /**
     * Returns true if the vehicle is currently in maintenance, false otherwise.
     *
     * A vehicle is considered in maintenance if there is at least one maintenance
     * for it with null end date.
     *
     * @param vehicle the vehicle to check
     * @return true if the vehicle is currently in maintenance, false otherwise
     */
    override fun isMaintaining(vehicle: Vehicle): Boolean {
        return maintenanceRepository.findAllByVehicle(vehicle).any { it.end == null }
    }

    /**
     * Retrieves a list of all active maintenances.
     *
     * A maintenance is considered active if it has a null end date.
     *
     * @return a list of [DAOActiveMaintenance] objects representing all active
     *         maintenances in the system.
     */

    override fun getActiveMaintenances(): List<DAOActiveMaintenance> {
        val maintenances: List<Maintenance> = maintenanceRepository.findAllByEndIsNull()
        return maintenances.map { maintenance -> DAOActiveMaintenance(maintenance) }
    }

    /**
     * Initiates a new maintenance record for a given vehicle.
     *
     * The maintenance will start on the current date and can include an optional description.
     * The new maintenance record is saved in the repository.
     *
     * @param vehicle the vehicle to be maintained
     * @param description an optional description of the maintenance
     */

    override fun newMaintenance(vehicle: Vehicle, description: String?) {
        val maintenance: Maintenance = Maintenance().apply{
            this.vehicle= vehicle
                if (description != null) {
                    this.description= description
                }
                this.start= LocalDate.now()
            }
        maintenanceRepository.save(maintenance)
    }

    /**
     * Ends the ongoing maintenance for a given vehicle by setting the end date to the current date.
     *
     * If the vehicle has an active maintenance (i.e., a maintenance record with a null end date),
     * the end date is updated to the current date and the maintenance record is saved.
     *
     * @param vehicle the vehicle whose maintenance is to be ended
     * @return true if the maintenance was successfully ended, false if no active maintenance was found
     */

    override fun endVehicleMaintenance(vehicle: Vehicle) {
        val activeMaintenance = maintenanceRepository.findByVehicleAndEndIsNull(vehicle)
        if (activeMaintenance != null) {
            activeMaintenance.end = LocalDate.now()
            maintenanceRepository.save(activeMaintenance)
            true
        } else {
            false
        }
    }
}