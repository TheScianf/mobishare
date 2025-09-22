package it.uniupo.studenti.mobishare.backend_core.service

import it.uniupo.studenti.mobishare.backend_core.entity.Manager
import it.uniupo.studenti.mobishare.backend_core.entity.Park
import it.uniupo.studenti.mobishare.backend_core.repository.ParkRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

interface ParkService {
    fun findById(idPark: Int): Park?
    fun findAll(): List<Park>
    fun setManager(parkId: Int, manager: Manager)
}

@Service
class ParkServiceImpl : ParkService {

    @Autowired
    lateinit var parkRepository: ParkRepository

    /**
     * Returns a park with the given id, or null if no such park exists.
     *
     * @param idPark the id of the park to be found
     * @return the park with the given id, or null if no such park exists
     */
    override fun findById(idPark: Int): Park? {
        return parkRepository.findById(idPark).getOrNull()
    }

    /**
     * Returns a list of all parks.
     *
     * @return a list of [Park] objects, or an empty list if no parks are found
     */
    override fun findAll(): List<Park> {
        return parkRepository.findAll()
    }

    override fun setManager(parkId: Int, manager: Manager) {
        val park = findById(parkId)
        park?.manager = manager
        if (park != null) {
            parkRepository.save(park)
        }
    }
}