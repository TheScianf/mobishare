package it.uniupo.studenti.mobishare.backend_core.service

import it.uniupo.studenti.mobishare.backend_core.entity.Vehicle
import it.uniupo.studenti.mobishare.backend_core.entity.VehicleType
import it.uniupo.studenti.mobishare.backend_core.repository.VehicleRepository
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import org.mockito.Mockito.`when` as quando
import java.time.LocalDate
import java.util.*

@SpringBootTest
class VehicleServiceTest {

    @MockBean
    lateinit var vehicleRepository: VehicleRepository

    @MockBean
    lateinit var vehicleTypeService: VehicleTypeService

    @MockBean
    lateinit var parkService: ParkService

    @MockBean
    lateinit var maintenanceService: MaintenanceService

    @MockBean
    lateinit var sensorReportService: SensorReportService

    @Autowired
    lateinit var vehicleService: VehicleService

    @Test
    fun findAll_existingVehicles() {
        val vehicle1 = Vehicle().apply {
            id = 1
            immissionDate = LocalDate.of(2023, 1, 1)
            disabled = false
        }
        val vehicle2 = Vehicle().apply {
            id = 2
            immissionDate = LocalDate.of(2023, 2, 1)
            disabled = false
        }
        val vehicles = listOf(vehicle1, vehicle2)

        quando(vehicleRepository.findAll()).thenReturn(vehicles)

        val result = vehicleService.findAll()

        assertEquals(vehicles, result)
        Mockito.verify(vehicleRepository).findAll()
    }

    @Test
    fun findById_existingVehicle() {
        val vehicleId = 1
        val vehicle = Vehicle().apply {
            id = vehicleId
            immissionDate = LocalDate.of(2023, 1, 1)
            disabled = false
        }

        quando(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle))

        val result = vehicleService.findById(vehicleId)

        assertEquals(vehicle, result)
        Mockito.verify(vehicleRepository).findById(vehicleId)
    }

    @Test
    fun findById_nonExistentVehicle() {
        val vehicleId = 999

        quando(vehicleRepository.findById(vehicleId)).thenReturn(Optional.empty())

        val result = vehicleService.findById(vehicleId)

        assertNull(result)
        Mockito.verify(vehicleRepository).findById(vehicleId)
    }

    @Test
    fun isAvailable_enabledVehicle() {
        val vehicle = Vehicle().apply {
            id = 1
            immissionDate = LocalDate.of(2023, 1, 1)
            disabled = false
        }

        val result = vehicleService.isAvailable(vehicle)

        assertTrue(result)
    }

    @Test
    fun isAvailable_disabledVehicle() {
        val vehicle = Vehicle().apply {
            id = 1
            immissionDate = LocalDate.of(2023, 1, 1)
            disabled = true
        }

        val result = vehicleService.isAvailable(vehicle)

        assertFalse(result)
    }

    @Test
    fun enable_vehicle() {
        val vehicle = Vehicle().apply {
            id = 1
            immissionDate = LocalDate.of(2023, 1, 1)
            disabled = true
        }

        vehicleService.enable(vehicle)

        assertFalse(vehicle.disabled)
        Mockito.verify(vehicleRepository).save(vehicle)
    }

    @Test
    fun addVehicle_withValidType() {
        val typeId = 1
        val vehicleType = VehicleType().apply {
            id = typeId
            name = "E-Bike"
            description = "Electric Bike"
            constantPrice = 2.0f
            minutePrice = 0.1f
        }
        val savedVehicle = Vehicle().apply {
            id = 1
            immissionDate = LocalDate.now()
            disabled = false
            dismissionDate = null
        }

        quando(vehicleTypeService.findById(typeId)).thenReturn(vehicleType)
        quando(vehicleRepository.save(Mockito.any(Vehicle::class.java))).thenReturn(savedVehicle)

        val result = vehicleService.addVehicle(typeId)

        assertEquals(savedVehicle, result)
        Mockito.verify(vehicleTypeService).findById(typeId)
        Mockito.verify(vehicleRepository).save(Mockito.any(Vehicle::class.java))
    }
}