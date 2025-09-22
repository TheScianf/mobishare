package it.uniupo.studenti.mobishare.backend_core.service

import it.uniupo.studenti.mobishare.backend_core.entity.Actuator
import it.uniupo.studenti.mobishare.backend_core.entity.ActuatorType
import it.uniupo.studenti.mobishare.backend_core.entity.Dock
import it.uniupo.studenti.mobishare.backend_core.repository.ActuatorRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

interface ActuatorService {
    fun findByDockAndType(dock: Dock, type: ActuatorType): Actuator?
    fun findAllByDock(dock: Dock): List<Actuator>?
    fun save(actuator: Actuator)
}
@Service
class ActuatorServiceImpl: ActuatorService{
    @Autowired
    private lateinit var actuatorRepository: ActuatorRepository

    override fun findByDockAndType(
        dock: Dock,
        type: ActuatorType
    ): Actuator? {
        return actuatorRepository.findByDockAndType(dock, type)
    }

    override fun findAllByDock(dock: Dock): List<Actuator>? {
        return actuatorRepository.findAllByDock(dock)
    }

    override fun save(actuator: Actuator) {
       actuatorRepository.save(actuator)
    }


}