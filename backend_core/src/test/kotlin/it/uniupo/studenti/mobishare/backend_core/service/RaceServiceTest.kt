package it.uniupo.studenti.mobishare.backend_core.service

import it.uniupo.studenti.mobishare.backend_core.entity.Customer
import it.uniupo.studenti.mobishare.backend_core.entity.Race
import it.uniupo.studenti.mobishare.backend_core.entity.RaceId
import it.uniupo.studenti.mobishare.backend_core.entity.Vehicle
import it.uniupo.studenti.mobishare.backend_core.exception.UsernameNotFoundException
import it.uniupo.studenti.mobishare.backend_core.repository.RaceRepository
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import org.mockito.Mockito.`when` as quando
import java.time.Instant
import java.time.LocalDate
import java.util.*

@SpringBootTest
class RaceServiceTest {

    @MockBean
    lateinit var raceRepository: RaceRepository

    @MockBean
    lateinit var customerService: CustomerService

    @Autowired
    lateinit var raceService: RaceService

    @Test
    fun findAllRacesByCustomer_nonExistentCustomer() {
        val username = "nonexistent"

        quando(customerService.findCustomerByUsername(username)).thenReturn(null)

        assertFailsWith<UsernameNotFoundException> {
            raceService.findAllRacesByCustomer(username)
        }

        Mockito.verify(customerService).findCustomerByUsername(username)
    }

    @Test
    fun startRace() {
        val vehicle = Vehicle().apply {
            id = 1
            immissionDate = LocalDate.of(2023, 1, 1)
            disabled = false
        }
        val customer = Customer().apply {
            usernameU = "testuser"
            passwordU = "password"
            name = "Test"
            surname = "User"
            cf = "TESTCF123456789"
            gender = "M"
            email = "test@example.com"
        }

        val result = raceService.startRace(vehicle, customer)

        assertNotNull(result)
        Mockito.verify(raceRepository).save(Mockito.any(Race::class.java))
    }

    @Test
    fun endRace_existingRace() {
        val vehicle = Vehicle().apply {
            id = 1
            immissionDate = LocalDate.of(2023, 1, 1)
            disabled = false
        }
        val customer = Customer().apply {
            usernameU = "testuser"
            passwordU = "password"
            name = "Test"
            surname = "User"
            cf = "TESTCF123456789"
            gender = "M"
            email = "test@example.com"
        }
        val startTime = Instant.now()
        val raceId = RaceId().apply {
            start = startTime
            idCustomer = customer.usernameU
            idVehicle = vehicle.id
        }
        val race = Race().apply {
            id = raceId
            this.customer = customer
            minDuration = 0
        }
        val minuteDuration = 45

        quando(raceRepository.findById(raceId)).thenReturn(Optional.of(race))

        raceService.endRace(vehicle, customer, startTime, minuteDuration)

        assertEquals(minuteDuration, race.minDuration)
        Mockito.verify(raceRepository).findById(raceId)
        Mockito.verify(raceRepository).save(race)
    }

    @Test
    fun endRace_nonExistentRace() {
        val vehicle = Vehicle().apply {
            id = 1
            immissionDate = LocalDate.of(2023, 1, 1)
            disabled = false
        }
        val customer = Customer().apply {
            usernameU = "testuser"
            passwordU = "password"
            name = "Test"
            surname = "User"
            cf = "TESTCF123456789"
            gender = "M"
            email = "test@example.com"
        }
        val startTime = Instant.now()
        val raceId = RaceId().apply {
            start = startTime
            idCustomer = customer.usernameU
            idVehicle = vehicle.id
        }
        val minuteDuration = 45

        quando(raceRepository.findById(raceId)).thenReturn(Optional.empty())

        raceService.endRace(vehicle, customer, startTime, minuteDuration)

        Mockito.verify(raceRepository).findById(raceId)
        Mockito.verify(raceRepository, Mockito.never()).save(Mockito.any(Race::class.java))
    }
}