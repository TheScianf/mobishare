package it.uniupo.studenti.mobishare.backend_core.repository

import it.uniupo.studenti.mobishare.backend_core.entity.Actuator
import it.uniupo.studenti.mobishare.backend_core.entity.ActuatorId
import it.uniupo.studenti.mobishare.backend_core.entity.ActuatorType
import it.uniupo.studenti.mobishare.backend_core.entity.Dock
import org.springframework.data.jpa.repository.JpaRepository

interface ActuatorRepository: JpaRepository<Actuator, ActuatorId>{
    fun findByDockAndType(Dock: Dock, ActuatorType : ActuatorType):  Actuator?
    fun findAllByDock(dock:Dock): List<Actuator>?
}