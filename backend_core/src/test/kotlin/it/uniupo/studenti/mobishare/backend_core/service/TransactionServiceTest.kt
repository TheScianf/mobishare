package it.uniupo.studenti.mobishare.backend_core.service

import it.uniupo.studenti.mobishare.backend_core.entity.Customer
import it.uniupo.studenti.mobishare.backend_core.entity.Transaction
import it.uniupo.studenti.mobishare.backend_core.exception.UsernameNotFoundException
import it.uniupo.studenti.mobishare.backend_core.repository.TransactionRepository
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertFailsWith
import org.mockito.Mockito.`when` as quando
import java.time.Instant

@SpringBootTest
class TransactionServiceTest {

    @MockBean
    lateinit var transactionRepository: TransactionRepository

    @MockBean
    lateinit var customerService: CustomerService

    @Autowired
    lateinit var transactionService: TransactionService

    @Test
    fun getLastByCustomer_existingCustomerWithTransactions() {
        val username = "testuser"
        val customer = Customer().apply {
            usernameU = username
            passwordU = "password"
            name = "Test"
            surname = "User"
            cf = "TESTCF123456789"
            gender = "M"
            email = "test@example.com"
            transactions = mutableSetOf(
                Transaction().apply {
                    id = 1
                    value = 50.0f
                    time = Instant.now().minusSeconds(3600)
                },
                Transaction().apply {
                    id = 2
                    value = 25.0f
                    time = Instant.now()
                }
            )
        }

        quando(customerService.findCustomerByUsername(username)).thenReturn(customer)

        val result = transactionService.getLastByCustomer(username)

        assertEquals(25.0f, result?.value)
        Mockito.verify(customerService).findCustomerByUsername(username)
    }

    @Test
    fun getLastByCustomer_nonExistentCustomer() {
        val username = "nonexistent"

        quando(customerService.findCustomerByUsername(username)).thenReturn(null)

        val result = transactionService.getLastByCustomer(username)

        assertNull(result)
        Mockito.verify(customerService).findCustomerByUsername(username)
    }

    @Test
    fun getCreditByCustomer_existingCustomer() {
        val username = "testuser"
        val customer = Customer().apply {
            usernameU = username
            passwordU = "password"
            name = "Test"
            surname = "User"
            cf = "TESTCF123456789"
            gender = "M"
            email = "test@example.com"
            transactions = mutableSetOf(
                Transaction().apply {
                    id = 1
                    value = 50.0f
                    time = Instant.now()
                },
                Transaction().apply {
                    id = 2
                    value = 25.0f
                    time = Instant.now()
                }
            )
        }

        quando(customerService.findCustomerByUsername(username)).thenReturn(customer)

        val result = transactionService.getCreditByCustomer(username)

        assertEquals(75.0f, result)
        Mockito.verify(customerService).findCustomerByUsername(username)
    }

    @Test
    fun getCreditByCustomer_nonExistentCustomer() {
        val username = "nonexistent"

        quando(customerService.findCustomerByUsername(username)).thenReturn(null)

        assertFailsWith<UsernameNotFoundException> {
            transactionService.getCreditByCustomer(username)
        }

        Mockito.verify(customerService).findCustomerByUsername(username)
    }

    @Test
    fun getByUsername_existingCustomer() {
        val username = "testuser"
        val customer = Customer().apply {
            usernameU = username
            passwordU = "password"
            name = "Test"
            surname = "User"
            cf = "TESTCF123456789"
            gender = "M"
            email = "test@example.com"
            transactions = mutableSetOf(
                Transaction().apply {
                    id = 1
                    value = 50.0f
                    time = Instant.now().minusSeconds(3600)
                },
                Transaction().apply {
                    id = 2
                    value = 25.0f
                    time = Instant.now()
                }
            )
        }

        quando(customerService.findCustomerByUsername(username)).thenReturn(customer)

        val result = transactionService.getByUsername(username)

        assertEquals(2, result.size)
        assertEquals(25.0f, result[0].value) // Più recente per primo
        Mockito.verify(customerService).findCustomerByUsername(username)
    }

    @Test
    fun newTransaction() {
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

        transactionService.newTransaction(customer, value)

        Mockito.verify(transactionRepository).save(Mockito.any(Transaction::class.java))
    }
}
