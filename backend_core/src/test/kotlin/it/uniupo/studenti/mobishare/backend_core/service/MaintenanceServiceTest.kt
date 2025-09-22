package it.uniupo.studenti.mobishare.backend_core.service

import it.uniupo.studenti.mobishare.backend_core.dao.DAOActiveMaintenance
import it.uniupo.studenti.mobishare.backend_core.entity.Maintenance
import it.uniupo.studenti.mobishare.backend_core.entity.Vehicle
import it.uniupo.studenti.mobishare.backend_core.repository.MaintenanceRepository
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import org.mockito.Mockito.`when` as quando
import java.time.LocalDate

@SpringBootTest
class MaintenanceServiceTest {

    @MockBean
    lateinit var maintenanceRepository: MaintenanceRepository

    @Autowired
    lateinit var maintenanceService: MaintenanceService

    @Test
    fun isMaintaining_vehicleInMaintenance() {
        val vehicle = Vehicle().apply {
            id = 1
            immissionDate = LocalDate.of(2023, 1, 1)
            disabled = false
        }
        val activeMaintenance = Maintenance().apply {
            id = 1
            description = "Test maintenance"
            start = LocalDate.now()
            end = null
        }

        quando(maintenanceRepository.findAllByVehicle(vehicle)).thenReturn(listOf(activeMaintenance))

        val result = maintenanceService.isMaintaining(vehicle)

        assertTrue(result)
        Mockito.verify(maintenanceRepository).findAllByVehicle(vehicle)
    }

    @Test
    fun isMaintaining_vehicleNotInMaintenance() {
        val vehicle = Vehicle().apply {
            id = 1
            immissionDate = LocalDate.of(2023, 1, 1)
            disabled = false
        }
        val completedMaintenance = Maintenance().apply {
            id = 1
            description = "Completed maintenance"
            start = LocalDate.of(2024, 1, 1)
            end = LocalDate.of(2024, 1, 15)
        }

        quando(maintenanceRepository.findAllByVehicle(vehicle)).thenReturn(listOf(completedMaintenance))

        val result = maintenanceService.isMaintaining(vehicle)

        assertFalse(result)
        Mockito.verify(maintenanceRepository).findAllByVehicle(vehicle)
    }

    @Test
    fun newMaintenance_withDescription() {
        val vehicle = Vehicle().apply {
            id = 1
            immissionDate = LocalDate.of(2023, 1, 1)
            disabled = false
        }
        val description = "Test maintenance description"

        maintenanceService.newMaintenance(vehicle, description)

        Mockito.verify(maintenanceRepository).save(Mockito.any(Maintenance::class.java))
    }

    @Test
    fun endVehicleMaintenance_withActiveMaintenance() {
        val vehicle = Vehicle().apply {
            id = 1
            immissionDate = LocalDate.of(2023, 1, 1)
            disabled = false
        }
        val activeMaintenance = Maintenance().apply {
            id = 1
            description = "Active maintenance"
            start = LocalDate.now()
            end = null
        }

        quando(maintenanceRepository.findByVehicleAndEndIsNull(vehicle)).thenReturn(activeMaintenance)

        maintenanceService.endVehicleMaintenance(vehicle)

        Mockito.verify(maintenanceRepository).findByVehicleAndEndIsNull(vehicle)
        Mockito.verify(maintenanceRepository).save(activeMaintenance)
    }
}