package it.uniupo.studenti.mobishare.backend_core.service

import it.uniupo.studenti.mobishare.backend_core.entity.Customer
import it.uniupo.studenti.mobishare.backend_core.repository.CustomerRepository
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

interface CustomerService {
    fun findCustomerByUsername(username: String): Customer?
    fun existsByUsername(username: String): Boolean
    fun save(c: Customer): Customer
}

@Service
class CustomerServiceImpl(
    private val customerRepository: CustomerRepository
) : CustomerService {

    /**
     * Find a customer by their username.
     *
     * @param username the username to search for
     * @return the customer with the given username, or null if no customer exists
     */
    override fun findCustomerByUsername(username: String): Customer? {
        return customerRepository.findById(username).getOrNull()
    }

    /**
     * Check if a customer exists by their username.
     *
     * @param username the username to check
     * @return true if the customer exists, false otherwise
     */
    override fun existsByUsername(username: String): Boolean {
        return customerRepository.findByUsernameU(username) != null
    }

    /**
     * Save a customer to the database.
     *
     * @param c the customer to save
     * @return the saved customer
     *
     * This function will trim and lowercase the username, and trim and capitalize the name and surname.
     */
    override fun save(c: Customer): Customer {
        return customerRepository.save(c.also {
            it.usernameU = it.usernameU.trim().lowercase()
            it.name = it.name.trim().replaceFirstChar { it.uppercaseChar() }
            it.surname = it.surname.trim().replaceFirstChar { it.uppercaseChar() }
        })
    }
}
