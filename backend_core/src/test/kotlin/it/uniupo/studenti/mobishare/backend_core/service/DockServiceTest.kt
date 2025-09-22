package it.uniupo.studenti.mobishare.backend_core.service

import it.uniupo.studenti.mobishare.backend_core.entity.Dock
import it.uniupo.studenti.mobishare.backend_core.repository.DockRepository
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import org.mockito.Mockito.`when` as quando
import java.util.*

@SpringBootTest
class DockServiceTest {

    @MockBean
    lateinit var dockRepository: DockRepository

    @MockBean
    lateinit var parkService: ParkService

    @MockBean
    lateinit var vehicleDockService: VehicleDockService

    @Autowired
    lateinit var dockService: DockService

    @Test
    fun findById_existingDock() {
        val dockId = 1
        val dock = Dock().apply {
            id = dockId
            number = 1
        }

        quando(dockRepository.findById(dockId)).thenReturn(Optional.of(dock))

        val result = dockService.findById(dockId)

        assertEquals(dock, result)
        Mockito.verify(dockRepository).findById(dockId)
    }

    @Test
    fun findById_nonExistentDock() {
        val dockId = 999

        quando(dockRepository.findById(dockId)).thenReturn(Optional.empty())

        val result = dockService.findById(dockId)

        assertNull(result)
        Mockito.verify(dockRepository).findById(dockId)
    }
}