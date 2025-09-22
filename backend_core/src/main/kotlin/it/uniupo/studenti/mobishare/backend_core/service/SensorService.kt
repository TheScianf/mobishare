package it.uniupo.studenti.mobishare.backend_core.service

import it.uniupo.studenti.mobishare.backend_core.entity.Sensor
import it.uniupo.studenti.mobishare.backend_core.entity.SensorType
import it.uniupo.studenti.mobishare.backend_core.entity.Vehicle
import it.uniupo.studenti.mobishare.backend_core.entity.VehicleType
import it.uniupo.studenti.mobishare.backend_core.repository.SensorRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

interface SensorService{
    fun findById(sensorId: Int): Sensor?
    fun findAllByVehicle(vehicle: Vehicle): List<Sensor>?
    fun addSensor(type: SensorType, vehicle: Vehicle)
}

@Service
class SensorServiceImpl : SensorService {
    @Autowired
    private lateinit var sensorRepository: SensorRepository
    /**
     * Retrieves a sensor by its ID.
     *
     * @param sensorId The ID of the sensor to retrieve.
     * @return The sensor with the given ID, or null if no such sensor exists.
     */
    override fun findById(sensorId: Int): Sensor? {
        return sensorRepository.findById(sensorId).getOrNull()
    }

    /**
     * Retrieves all sensors for a given vehicle.
     *
     * @param vehicle The vehicle to retrieve sensors for.
     * @return A list of sensors for the given vehicle, or null if no such sensors exist.
     */
    override fun findAllByVehicle(vehicle: Vehicle): List<Sensor>? {
        return sensorRepository.findAllByVehicle(vehicle)
    }

    /**
     * Adds a new sensor to the specified vehicle.
     *
     * @param type The type of the sensor to add.
     * @param vehicle The vehicle to which the sensor will be added.
     */

    override fun addSensor(
        type: SensorType,
        vehicle: Vehicle
    ) {
        val sensor = Sensor().apply {
            sensorType = type
            this.vehicle = vehicle
        }
        sensorRepository.save(sensor)
    }

}