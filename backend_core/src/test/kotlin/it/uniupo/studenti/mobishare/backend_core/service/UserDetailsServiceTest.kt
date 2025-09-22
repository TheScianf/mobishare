package it.uniupo.studenti.mobishare.backend_core.service

import it.uniupo.studenti.mobishare.backend_core.entity.Customer
import it.uniupo.studenti.mobishare.backend_core.entity.Manager
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import org.mockito.Mockito.`when` as quando

@SpringBootTest
class UserDetailsServiceTest {

    @MockBean
    lateinit var customerService: CustomerService

    @MockBean
    lateinit var managerService: ManagerService

    @Autowired
    lateinit var userDetailsService: UserDetailsServiceImpl

    @Test
    fun loadUserByUsername_existingCustomer() {
        val username = "testcustomer"
        val customer = Customer().apply {
            usernameU = username
            passwordU = "password"
            name = "Test"
            surname = "Customer"
            cf = "TESTCF123456789"
            gender = "M"
            email = "test@example.com"
        }

        quando(customerService.findCustomerByUsername(username)).thenReturn(customer)
        quando(managerService.findByEmail(username)).thenReturn(null)

        val result = userDetailsService.loadUserByUsername(username)

        assertEquals(customer, result)
        Mockito.verify(customerService).findCustomerByUsername(username)
    }

    @Test
    fun loadUserByUsername_existingManager() {
        val email = "manager@example.com"
        val manager = Manager().apply {
            id = 1
            this.email = email
            passwordU = "password"
            isAdmin = false
        }

        quando(customerService.findCustomerByUsername(email)).thenReturn(null)
        quando(managerService.findByEmail(email)).thenReturn(manager)

        val result = userDetailsService.loadUserByUsername(email)

        assertEquals(manager, result)
        Mockito.verify(customerService).findCustomerByUsername(email)
        Mockito.verify(managerService).findByEmail(email)
    }

    @Test
    fun loadUserByUsername_nonExistentUser() {
        val username = "nonexistent"

        quando(customerService.findCustomerByUsername(username)).thenReturn(null)
        quando(managerService.findByEmail(username)).thenReturn(null)

        val result = userDetailsService.loadUserByUsername(username)

        assertNull(result)
        Mockito.verify(customerService).findCustomerByUsername(username)
        Mockito.verify(managerService).findByEmail(username)
    }
}