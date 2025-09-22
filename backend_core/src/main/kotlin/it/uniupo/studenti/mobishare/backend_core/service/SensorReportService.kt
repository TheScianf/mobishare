package it.uniupo.studenti.mobishare.backend_core.service

import it.uniupo.studenti.mobishare.backend_core.dao.DAOSensorReportOnly
import it.uniupo.studenti.mobishare.backend_core.entity.Sensor
import it.uniupo.studenti.mobishare.backend_core.entity.SensorReport
import it.uniupo.studenti.mobishare.backend_core.entity.Vehicle
import it.uniupo.studenti.mobishare.backend_core.repository.SensorReportRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.Date

interface SensorReportService {
    fun hasPendingReports(vehicle: Vehicle): Boolean
    fun getPendingByVehicle(
        vehicle: Vehicle
    ): List<DAOSensorReportOnly>
    fun deleteAllBySensor(sensor: Sensor)
    fun addSensorReport(time: Date, sensor: Sensor, value: Float, state: String = "P" )
    fun setPendingToDeprecatedBySensor(sensor: Sensor)
}

@Service
class SensorReportServiceImpl: SensorReportService {

    @Autowired
    private lateinit var sensorReportRepository: SensorReportRepository

    /**
     * Check if there are pending reports for a vehicle.
     *
     * @param vehicle The vehicle to check.
     * @return True if there are pending reports, false otherwise.
     */
    override fun hasPendingReports(vehicle: Vehicle): Boolean {
        return sensorReportRepository.findAllByVehicleAndStatus(vehicle, "P").isNotEmpty()
    }

    /**
     * Retrieves a list of pending sensor reports for a specific vehicle.
     *
     * @param vehicle The vehicle for which to retrieve pending sensor reports.
     * @return A list of [DAOSensorReportOnly] objects representing the pending sensor reports for the given vehicle.
     */

    override fun getPendingByVehicle(
        vehicle: Vehicle
    ): List<DAOSensorReportOnly> {
        return sensorReportRepository.findAllByVehicleAndStatus(vehicle, "P")
            .map { DAOSensorReportOnly(
                it.time,
                it.value,
            ) }
    }

    /**
     * Deletes all sensor reports for a given sensor.
     *
     * @param sensor The sensor for which to delete sensor reports.
     */
    override fun deleteAllBySensor(sensor: Sensor){
        sensorReportRepository.deleteAllBySensor(sensor)
    }

    override fun addSensorReport(
        time: Date,
        sensor: Sensor,
        value: Float,
        state: String
    ) {
        val sensorReport = SensorReport().apply {
            this.time = time
            this.sensor = sensor
            this.value = value
            this.state = state
        }
        sensorReportRepository.save(sensorReport)
    }

    override fun setPendingToDeprecatedBySensor(sensor: Sensor) {
        sensorReportRepository.updatePendingToDeprecatedBySensor(sensor)
    }

}