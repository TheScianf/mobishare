package it.uniupo.studenti.mobishare.backend_core.service

import it.uniupo.studenti.mobishare.backend_core.entity.SensorType
import it.uniupo.studenti.mobishare.backend_core.entity.ValueRange
import it.uniupo.studenti.mobishare.backend_core.entity.VehicleType
import it.uniupo.studenti.mobishare.backend_core.repository.ValueRangeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

interface ValueRangeService {
    fun findByVehicleTypeAndSensorType(vehicleType: VehicleType, sensorType: SensorType): ValueRange?
}

@Service
class ValueRangeServiceImpl: ValueRangeService{
    @Autowired
    private lateinit var valueRangeRepository: ValueRangeRepository

    override fun findByVehicleTypeAndSensorType(
        vehicleType: VehicleType,
        sensorType: SensorType
    ): ValueRange? {
        return valueRangeRepository.findByVehicleTypeAndSensorType(vehicleType, sensorType)
    }

}