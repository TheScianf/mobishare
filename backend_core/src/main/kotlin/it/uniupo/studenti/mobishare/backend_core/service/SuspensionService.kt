package it.uniupo.studenti.mobishare.backend_core.service

import it.uniupo.studenti.mobishare.backend_core.dao.DAOCustomerWithSuspension
import it.uniupo.studenti.mobishare.backend_core.entity.Customer
import it.uniupo.studenti.mobishare.backend_core.entity.Suspension
import it.uniupo.studenti.mobishare.backend_core.exception.SuspensionNotFoundException
import it.uniupo.studenti.mobishare.backend_core.exception.UsernameNotFoundException
import it.uniupo.studenti.mobishare.backend_core.repository.SuspensionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

interface SuspensionService {
    fun getAllByUser(username: String): List<Suspension>
    fun getAllByActive(active: Boolean): List<DAOCustomerWithSuspension>
    fun getAll(): List<Suspension>
    fun isPermanentSuspended(username: String): Boolean
    fun setUserLastSuspensionRejectedStatus(username: String, isRejected: Boolean)
    fun isSuspended(username: String): Boolean
    fun newSuspension(customer: Customer, value: Float)
}

//quando si aggiungerà il service per aggiungere la sospensione, questa viene ovviamente aggiunta+
// senza la data di fine e senza il flag isRejected
// questi vengono aggiunti nel momento in cui viene accettata o rifiutata (fattibile solo quando è stata saldata)
@Service
class SuspensionServiceImpl() : SuspensionService {

    @Autowired
    lateinit var suspensionRepository: SuspensionRepository

    @Autowired
    lateinit var customerService: CustomerService

    /**
     * Retrieves all suspensions associated with a given username.
     *
     * @param username the username of the customer.
     * @return a list of [Suspension] objects associated with the specified username.
     */
    override fun getAllByUser(username: String): List<Suspension> {
        return suspensionRepository.searchByCustomerUsernameU(username)
    }


    /**
     * Retrieves all customers with their associated suspensions that are active (isRejected == null)
     * or inactive (isRejected != null).
     *
     * @param active true to retrieve active suspensions, false otherwise.
     * @return a list of [DAOCustomerWithSuspension] objects.
     */
    override fun getAllByActive(active: Boolean): List<DAOCustomerWithSuspension> {
        return if (active) {
            suspensionRepository.findByIsRejectedNull().map { s ->
                DAOCustomerWithSuspension(
                    s.customer,
                    s
                )
            }
        } else {
            suspensionRepository.findByIsRejectedNotNull().map { s ->
                DAOCustomerWithSuspension(
                    s.customer,
                    s
                )
            }
        }
    }

/**
 * Retrieves a list of all suspensions.
 *
 * @return a list of [Suspension] objects representing all suspensions in the system.
 */

    override fun getAll(): List<Suspension> {
        return suspensionRepository.findAll()
    }

    /**
     * Checks if a customer with the specified username is permanently suspended.
     *
     * @param username the username of the customer to check.
     * @return true if the customer has at least one suspension marked as rejected, indicating a permanent suspension; false otherwise.
     * @throws UsernameNotFoundException if no customer with the given username exists.
     */

    override fun isPermanentSuspended(username: String): Boolean {
        if (customerService.findCustomerByUsername(username) == null) {
            throw UsernameNotFoundException(username)
        }
        return suspensionRepository.findByCustomerUsernameUAndIsRejected(
            username,
            true
        ).isNotEmpty()
    }

    /**
     * Checks if a customer with the specified username has an active suspension.
     *
     * @param username the username of the customer to check.
     * @return true if the customer has at least one active suspension (isRejected == null); false otherwise.
     * @throws UsernameNotFoundException if no customer with the given username exists.
     */
    override fun isSuspended(username: String): Boolean {
        if (customerService.findCustomerByUsername(username) == null) {
            throw UsernameNotFoundException(username)
        }
        val sus = suspensionRepository
            .findByCustomerUsernameUAndIsRejectedNull(username) //find all suspensions not yet flagged as isRejected (-> active)
        return if (sus.isEmpty()) {
            false
        } else {
            return sus.first().isRejected ?: true //if isRejected is null, then the suspension is active
        }
    }

    /**
     * Creates a new suspension for a given customer.
     *
     * @param customer the customer to suspend.
     * @param value the value of the suspension.
     */
    override fun newSuspension(customer: Customer, value: Float) {
        val suspension = Suspension().apply {
           this.customer = customer
           this.value = value
        }
        suspensionRepository.save(suspension)
    }

    /**
     * Sets the isRejected status of the last suspension associated with the given username.
     *
     * @param username the username of the customer.
     * @param isRejected the new value of the isRejected flag.
     * @throws UsernameNotFoundException if no customer with the given username exists.
     * @throws SuspensionNotFoundException if no suspensions are associated with the given username.
     */
    override fun setUserLastSuspensionRejectedStatus(username: String, isRejected: Boolean) {
        val customer = customerService.findCustomerByUsername(username)
        if (customer == null) {
            throw UsernameNotFoundException(username)
        }
        val lastSuspension = customer.suspensions.minByOrNull { it.id.start }
        if (lastSuspension != null) {
            lastSuspension.isRejected = isRejected
            lastSuspension.end = Date().toInstant()
            suspensionRepository.save(lastSuspension)
        } else {
            throw SuspensionNotFoundException("No suspensions found for user: $username")
        }
    }


}
