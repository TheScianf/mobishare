package it.uniupo.studenti.mobishare.backend_core.service

import it.uniupo.studenti.mobishare.backend_core.entity.Actuator
import it.uniupo.studenti.mobishare.backend_core.entity.ActuatorId
import it.uniupo.studenti.mobishare.backend_core.entity.ActuatorType
import it.uniupo.studenti.mobishare.backend_core.entity.Dock
import it.uniupo.studenti.mobishare.backend_core.repository.ActuatorRepository
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import org.mockito.Mockito.`when` as quando

@SpringBootTest
class ActuatorServiceTest {

    @MockBean
    lateinit var actuatorRepository: ActuatorRepository

    @Autowired
    lateinit var actuatorService: ActuatorService

    @Test
    fun save_actuator() {
        val actuatorId = ActuatorId().apply {
            type = "LIGHT"
            idDock = 1
        }
        val actuator = Actuator().apply {
            id = actuatorId
            value = "RED"
        }

        actuatorService.save(actuator)

        Mockito.verify(actuatorRepository).save(actuator)
    }
}