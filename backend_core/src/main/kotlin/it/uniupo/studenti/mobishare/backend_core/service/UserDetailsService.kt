package it.uniupo.studenti.mobishare.backend_core.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl : UserDetailsService {

    @Autowired
    lateinit var customerService: CustomerService

    @Autowired
    lateinit var managerService: ManagerService

    /**
     * Returns a [UserDetails] for the given username if the user exists.
     * The user can be either a customer or a manager.
     *
     * @param username the username to search for
     * @return the user with the given username, or null if no user exists
     */
    override fun loadUserByUsername(username: String): UserDetails? {
        return customerService.findCustomerByUsername(username)
            ?: managerService.findByEmail(username)
    }
}