package it.uniupo.studenti.mobishare.backend_core.service

import it.uniupo.studenti.mobishare.backend_core.entity.Customer
import it.uniupo.studenti.mobishare.backend_core.entity.Suspension
import it.uniupo.studenti.mobishare.backend_core.repository.SuspensionRepository
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import org.mockito.Mockito.`when` as quando
import java.time.Instant

@SpringBootTest
class SuspensionServiceTest {

    @MockBean
    lateinit var suspensionRepository: SuspensionRepository

    @MockBean
    lateinit var customerService: CustomerService

    @Autowired
    lateinit var suspensionService: SuspensionService

    @Test
    fun getAllByUser_existingSuspensions() {
        val username = "testuser"
        val suspension1 = Suspension().apply {
            value = 50.0f
            isRejected = null
        }
        val suspension2 = Suspension().apply {
            value = 75.0f
            isRejected = false
        }
        val suspensions = listOf(suspension1, suspension2)

        quando(suspensionRepository.searchByCustomerUsernameU(username)).thenReturn(suspensions)

        val result = suspensionService.getAllByUser(username)

        assertEquals(suspensions, result)
        Mockito.verify(suspensionRepository).searchByCustomerUsernameU(username)
    }

    @Test
    fun getAll_allSuspensions() {
        val suspension1 = Suspension().apply {
            value = 50.0f
            isRejected = null
        }
        val suspension2 = Suspension().apply {
            value = 75.0f
            isRejected = true
        }
        val allSuspensions = listOf(suspension1, suspension2)

        quando(suspensionRepository.findAll()).thenReturn(allSuspensions)

        val result = suspensionService.getAll()

        assertEquals(allSuspensions, result)
        Mockito.verify(suspensionRepository).findAll()
    }

    @Test
    fun isPermanentSuspended_withRejectedSuspension() {
        val username = "testuser"
        val customer = Customer().apply {
            usernameU = username
            passwordU = "password"
            name = "Test"
            surname = "User"
            cf = "TESTCF123456789"
            gender = "M"
            email = "test@example.com"
        }
        val rejectedSuspension = Suspension().apply {
            value = 100.0f
            isRejected = true
        }

        quando(customerService.findCustomerByUsername(username)).thenReturn(customer)
        quando(suspensionRepository.findByCustomerUsernameUAndIsRejected(username, true))
            .thenReturn(listOf(rejectedSuspension))

        val result = suspensionService.isPermanentSuspended(username)

        assertTrue(result)
        Mockito.verify(customerService).findCustomerByUsername(username)
        Mockito.verify(suspensionRepository).findByCustomerUsernameUAndIsRejected(username, true)
    }

    @Test
    fun isSuspended_withActiveSuspension() {
        val username = "testuser"
        val customer = Customer().apply {
            usernameU = username
            passwordU = "password"
            name = "Test"
            surname = "User"
            cf = "TESTCF123456789"
            gender = "M"
            email = "test@example.com"
        }
        val activeSuspension = Suspension().apply {
            value = 50.0f
            isRejected = null
        }

        quando(customerService.findCustomerByUsername(username)).thenReturn(customer)
        quando(suspensionRepository.findByCustomerUsernameUAndIsRejectedNull(username))
            .thenReturn(listOf(activeSuspension))

        val result = suspensionService.isSuspended(username)

        assertTrue(result)
        Mockito.verify(customerService).findCustomerByUsername(username)
        Mockito.verify(suspensionRepository).findByCustomerUsernameUAndIsRejectedNull(username)
    }

    @Test
    fun isSuspended_withoutActiveSuspension() {
        val username = "testuser"
        val customer = Customer().apply {
            usernameU = username
            passwordU = "password"
            name = "Test"
            surname = "User"
            cf = "TESTCF123456789"
            gender = "M"
            email = "test@example.com"
        }

        quando(customerService.findCustomerByUsername(username)).thenReturn(customer)
        quando(suspensionRepository.findByCustomerUsernameUAndIsRejectedNull(username))
            .thenReturn(emptyList())

        val result = suspensionService.isSuspended(username)

        assertFalse(result)
        Mockito.verify(customerService).findCustomerByUsername(username)
        Mockito.verify(suspensionRepository).findByCustomerUsernameUAndIsRejectedNull(username)
    }

    @Test
    fun newSuspension() {
        val customer = Customer().apply {
            usernameU = "testuser"
            passwordU = "password"
            name = "Test"
            surname = "User"
            cf = "TESTCF123456789"
            gender = "M"
            email = "test@example.com"
        }
        val value = 100.0f

        suspensionService.newSuspension(customer, value)

        Mockito.verify(suspensionRepository).save(Mockito.any(Suspension::class.java))
    }
}