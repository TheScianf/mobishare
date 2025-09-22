package it.uniupo.studenti.mobishare.backend_core.service

import it.uniupo.studenti.mobishare.backend_core.entity.Customer
import it.uniupo.studenti.mobishare.backend_core.repository.CustomerRepository

import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import kotlin.test.Test
import kotlin.test.assertEquals
import org.mockito.Mockito.`when` as quando

@SpringBootTest
class CustomerServiceTest {

    @MockBean
    lateinit var customerRepository: CustomerRepository

    @Autowired
    lateinit var customerService: CustomerService

    @Test
    fun existsByUsername() {
        quando(customerRepository.findByUsernameU("tuxdave")).thenReturn(null)
        val c = Customer().apply {
            usernameU = "davidee"
            passwordU = "password"
            name = "davide"
            surname = "tuxdave"
            cf = "cf"
            gender = "M"
            email = "email@davide.it"
        }
        quando(customerRepository.findByUsernameU("davidee")).thenReturn(c)

        assertEquals(customerService.existsByUsername("davidee"), true)
        Mockito.verify(customerRepository).findByUsernameU("davidee")
        assertEquals(customerService.existsByUsername("tuxdave"), false)
        Mockito.verify(customerRepository).findByUsernameU("tuxdave")
    }
}