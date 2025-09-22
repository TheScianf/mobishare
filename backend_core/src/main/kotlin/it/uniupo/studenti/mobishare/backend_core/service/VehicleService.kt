package it.uniupo.studenti.mobishare.backend_core.service

import it.uniupo.studenti.mobishare.backend_core.dao.DAOVehicleWithStatus
import it.uniupo.studenti.mobishare.backend_core.dao.VehicleStatus
import it.uniupo.studenti.mobishare.backend_core.entity.Vehicle
import it.uniupo.studenti.mobishare.backend_core.repository.VehicleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate
import kotlin.jvm.optionals.getOrNull

interface VehicleService {
    fun findAll(): List<Vehicle>
    fun findAllByPark(parkId: Int): List<Vehicle>
    fun addVehicle(type: Int): Vehicle
    fun addStatus(vehicle: Vehicle): DAOVehicleWithStatus
    fun findById(idVehicle: Int): Vehicle?
    fun isAvailable(vehicle: Vehicle): Boolean
    fun enable(vehicle: Vehicle)
    fun getStatus(vehicle: Vehicle): VehicleStatus
}

@Service
class VehicleServiceImpl : VehicleService {

    @Autowired
    private lateinit var vehicleRepository: VehicleRepository

    @Autowired
    private lateinit var vehicleTypeService: VehicleTypeService

    @Autowired
    private lateinit var parkService: ParkService

    @Autowired
    private lateinit var maintenanceService: MaintenanceService

    @Autowired
    private lateinit var sensorReportService: SensorReportService

    /**
     * Retrieves a list of all vehicles from the repository.
     *
     * @return A list of all vehicles.
     */
    override fun findAll(): List<Vehicle> {
        return vehicleRepository.findAll()
    }

    /**
     * Retrieves a list of all vehicles in a given park.
     *
     * @param parkId the id of the park
     * @return a list of all vehicles in the given park, or an empty list if no vehicles are found or the park does not exist
     */
    override fun findAllByPark(parkId: Int): List<Vehicle> {
        val park = parkService.findById(parkId)
        return park?.let {
            it.docks
                .filter { it.vehicle != null }
                .map { it.vehicle!!.vehicle }
                .toList()
        } ?: listOf()
    }

    /**
     * Computes the status of the given vehicle, based on various conditions.
     * The status is computed as follows:
     * - If the vehicle is currently being maintained, the status is UNDER_MAINTENANCE
     * - If the vehicle has been dismissed, the status is ABANDONED
     * - If the vehicle has some pending sensor reports, the status is NOT_AVAILABLE
     * - If the vehicle is disabled, the status is DISABLED
     * - Otherwise, the status is AVAILABLE
     *
     * @param vehicle the vehicle to compute the status for
     * @return A DAOVehicleWithStatus object containing the vehicle and its status
     */
    override fun addStatus(vehicle: Vehicle): DAOVehicleWithStatus {
        var status: VehicleStatus = VehicleStatus.AVAILABLE

        if (maintenanceService.isMaintaining(vehicle)) {
            status = VehicleStatus.UNDER_MAINTENANCE
        } else if (vehicle.dismissionDate != null) {
            status = VehicleStatus.ABANDONED
        } else if (sensorReportService.hasPendingReports(vehicle)) {
            status = VehicleStatus.NOT_AVAILABLE
        } else if (vehicle.disabled) {
            status = VehicleStatus.DISABLED
        }

        return DAOVehicleWithStatus(vehicle, status)
    }

    /**
     * Computes the status of the given vehicle, based on various conditions.
     * The status is computed as follows:
     * - If the vehicle is currently being maintained, the status is UNDER_MAINTENANCE
     * - If the vehicle has been dismissed, the status is ABANDONED
     * - If the vehicle has some pending sensor reports, the status is NOT_AVAILABLE
     * - If the vehicle is disabled, the status is DISABLED
     * - Otherwise, the status is AVAILABLE
     *
     * @param vehicle the vehicle to compute the status for
     * @return A DAOVehicleWithStatus object containing the vehicle and its status
     */
    override fun getStatus(vehicle: Vehicle): VehicleStatus {
        var status: VehicleStatus = VehicleStatus.AVAILABLE

        if (maintenanceService.isMaintaining(vehicle)) {
            status = VehicleStatus.UNDER_MAINTENANCE
        } else if (vehicle.dismissionDate != null) {
            status = VehicleStatus.ABANDONED
        } else if (sensorReportService.hasPendingReports(vehicle)) {
            status = VehicleStatus.NOT_AVAILABLE
        } else if (vehicle.disabled) {
            status = VehicleStatus.DISABLED
        }

        return status
    }

    /**
     * Retrieves a vehicle by id.
     *
     * @param idVehicle the id of the vehicle
     * @return the vehicle with the given id, or null if no such vehicle exists
     */
    override fun findById(idVehicle: Int): Vehicle? {
        return vehicleRepository.findById(idVehicle).getOrNull()
    }

    /**
     * Returns true if the given vehicle is available, false otherwise.
     *
     * A vehicle is considered available if it is not disabled.
     *
     * @param vehicle the vehicle to check
     * @return true if the vehicle is available, false otherwise
     */
    override fun isAvailable(vehicle: Vehicle): Boolean {
        return !vehicle.disabled
    }


    /**
     * Enables the given vehicle, setting its disabled field to false.
     * If the vehicle does not exist, this operation does nothing.
     *
     * @param vehicle the vehicle to enable
     */
    override fun enable(vehicle: Vehicle) {
        vehicle.disabled = false
        vehicleRepository.save(vehicle)
    }

    /**
     * Adds a new vehicle with the specified type.
     *
     * The new vehicle will have its immission date set to the current date
     * and will be marked as enabled. The vehicle type is determined by the
     * given type ID.
     *
     * @param type the ID of the vehicle type to associate with the new vehicle
     * @return the newly created and saved vehicle
     */
    override fun addVehicle(type: Int): Vehicle {
        val vehicleType = vehicleTypeService.findById(type)

        val vehicle = Vehicle().apply {
            immissionDate = LocalDate.now()
            disabled = false
            dismissionDate = null
            if (vehicleType != null) {
                this.vehicleType = vehicleType
            }
        }

        return vehicleRepository.save(vehicle)
    }
}
