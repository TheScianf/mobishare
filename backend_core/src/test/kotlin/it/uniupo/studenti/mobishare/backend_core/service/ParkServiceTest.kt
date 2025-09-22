package it.uniupo.studenti.mobishare.backend_core.service

import it.uniupo.studenti.mobishare.backend_core.entity.Manager
import it.uniupo.studenti.mobishare.backend_core.entity.Park
import it.uniupo.studenti.mobishare.backend_core.repository.ParkRepository
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
class ParkServiceTest {

    @MockBean
    lateinit var parkRepository: ParkRepository

    @Autowired
    lateinit var parkService: ParkService

    @Test
    fun findById_existingPark() {
        val parkId = 1
        val park = Park().apply {
            id = parkId
            address = "Via Roma 123"
            latitude = 45.4642f
            longitude = 9.1900f
        }

        quando(parkRepository.findById(parkId)).thenReturn(Optional.of(park))

        val result = parkService.findById(parkId)

        assertEquals(park, result)
        Mockito.verify(parkRepository).findById(parkId)
    }

    @Test
    fun findById_nonExistentPark() {
        val parkId = 999

        quando(parkRepository.findById(parkId)).thenReturn(Optional.empty())

        val result = parkService.findById(parkId)

        assertNull(result)
        Mockito.verify(parkRepository).findById(parkId)
    }

    @Test
    fun findAll_existingParks() {
        val park1 = Park().apply {
            id = 1
            address = "Via Roma 123"
            latitude = 45.4642f
            longitude = 9.1900f
        }
        val park2 = Park().apply {
            id = 2
            address = "Via Milano 456"
            latitude = 45.4700f
            longitude = 9.1950f
        }
        val parkList = listOf(park1, park2)

        quando(parkRepository.findAll()).thenReturn(parkList)

        val result = parkService.findAll()

        assertEquals(parkList, result)
        Mockito.verify(parkRepository).findAll()
    }

    @Test
    fun setManager_existingPark() {
        val parkId = 1
        val park = Park().apply {
            id = parkId
            address = "Via Roma 123"
            latitude = 45.4642f
            longitude = 9.1900f
        }
        val manager = Manager().apply {
            id = 1
            email = "manager@example.com"
            passwordU = "password"
            isAdmin = false
        }

        quando(parkRepository.findById(parkId)).thenReturn(Optional.of(park))

        parkService.setManager(parkId, manager)

        assertEquals(manager, park.manager)
        Mockito.verify(parkRepository).findById(parkId)
        Mockito.verify(parkRepository).save(park)
    }
}