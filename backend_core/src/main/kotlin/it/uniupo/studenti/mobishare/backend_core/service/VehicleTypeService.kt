
package it.uniupo.studenti.mobishare.backend_core.service

import it.uniupo.studenti.mobishare.backend_core.entity.Park
import it.uniupo.studenti.mobishare.backend_core.entity.VehicleType
import it.uniupo.studenti.mobishare.backend_core.repository.VehicleTypeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

interface VehicleTypeService {
    fun findById(id: Int):
            VehicleType?
    fun numberOfVehicleByParkAndId(Park: Park, vehicleTypeId: Int): Int
}
@Service
class VehicleTypeServiceImpl : VehicleTypeService {
    @Autowired
    private lateinit var vehicleTypeRepository: VehicleTypeRepository

    /**
     * Finds a vehicle type by its id.
     *
     * @param id the id of the vehicle type to be found
     * @return the vehicle type with the given id, or null if no such vehicle type exists
     */
    override fun findById(id: Int): VehicleType? {
        return vehicleTypeRepository.findById(id).getOrNull()
    }

    /**
     * Finds the number of vehicles of a given type in a given park.
     *
     * @param park the park to count vehicles in
     * @param vehicleTypeId the id of the vehicle type to count
     * @return the number of vehicles of the given type in the given park
     */
    override fun numberOfVehicleByParkAndId(park: Park, vehicleTypeId: Int): Int {
        return vehicleTypeRepository.countVehiclesByParkAndVehicleType(park, vehicleTypeId)
    }
}

