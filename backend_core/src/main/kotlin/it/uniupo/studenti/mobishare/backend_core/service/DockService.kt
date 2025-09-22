package it.uniupo.studenti.mobishare.backend_core.service

import it.uniupo.studenti.mobishare.backend_core.entity.Actuator
import it.uniupo.studenti.mobishare.backend_core.entity.Dock
import it.uniupo.studenti.mobishare.backend_core.exception.DockNotFoundException
import it.uniupo.studenti.mobishare.backend_core.repository.ActuatorRepository
import it.uniupo.studenti.mobishare.backend_core.repository.DockRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

enum class Light {
    GREEN,
    RED,
    OFF;
}

interface DockService {
    fun findById(dockId:Int): Dock?
    fun getFreeDockByParkId(parkId: Int) : Dock?
    fun findByParkIdAndNumber(parkId: Int, number: Int): Dock?
}

@Service
class DockServiceImpl: DockService {
    @Autowired
    private lateinit var dockRepository : DockRepository

    @Autowired
    private lateinit var parkService: ParkService

    @Autowired
    private lateinit var vehicleDockService: VehicleDockService
    override fun findById(dockId: Int): Dock?{
        return dockRepository.findById(dockId).getOrNull()
    }

    /**
     * Returns a free dock by parkId, i.e., a dock in the given park which does not have a vehicle assigned to it.
     *
     * @param parkId the id of the park
     * @return a dock without a vehicle in the given park, or null if no free docks are found or the park does not exist
     */
    override fun getFreeDockByParkId(parkId: Int): Dock? {
        val park = parkService.findById(parkId)
        if (park !== null){
            val freeDock = dockRepository.findAllByPark(park).find { dock ->  vehicleDockService.getVehicleDockByDock(dock) === null}
            return freeDock
        }
        return null
    }

    override fun findByParkIdAndNumber(
        parkId: Int,
        number: Int
    ): Dock? {
        return dockRepository.findAllByParkIdAndNumber(parkId, number)
    }
}
