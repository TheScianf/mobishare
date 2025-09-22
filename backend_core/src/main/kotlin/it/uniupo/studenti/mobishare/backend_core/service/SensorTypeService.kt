package it.uniupo.studenti.mobishare.backend_core.service

import it.uniupo.studenti.mobishare.backend_core.entity.SensorType
import it.uniupo.studenti.mobishare.backend_core.repository.SensorTypeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

interface SensorTypeService {
    fun findAll(): List<SensorType>
}

@Service
class SensorTypeServiceImpl: SensorTypeService{
    @Autowired
    private lateinit var sensorTypeRepository: SensorTypeRepository
    /**
     * Retrieves all sensor types in the database.
     *
     * @return A list of all sensor types, or an empty list if no sensor types exist.
     */
    override fun findAll(): List<SensorType>{
        return sensorTypeRepository.findAll()
    }
}