package it.uniupo.studenti.mobishare.backend_core.service

import it.uniupo.studenti.mobishare.backend_core.dao.DAOTransactionOnly
import it.uniupo.studenti.mobishare.backend_core.entity.Customer
import it.uniupo.studenti.mobishare.backend_core.entity.Transaction
import it.uniupo.studenti.mobishare.backend_core.exception.UsernameNotFoundException
import it.uniupo.studenti.mobishare.backend_core.repository.TransactionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant

interface TransactionService {
    fun getLastByCustomer(username: String): DAOTransactionOnly?
    fun getCreditByCustomer(username: String): Float
    fun getByUsername(username: String): List<DAOTransactionOnly>
    fun newTransaction(customer: Customer, value: Float)
}

@Service
class TransactionServiceImpl : TransactionService {

    @Autowired
    private lateinit var customerService: CustomerService
    @Autowired
    private lateinit var transactionRepository: TransactionRepository

    /**
     * Retrieve the last transaction for a given customer, if the customer exists.
     *
     * @param username the username of the customer.
     * @return the last transaction of the customer, null if no transaction exists or the customer does not exist.
     * @throws UsernameNotFoundException if the customer does not exist.
     */
    override fun getLastByCustomer(username: String): DAOTransactionOnly? {
        val customer = customerService.findCustomerByUsername(username)
        return customer?.let {
            it.transactions.maxByOrNull { it.time.epochSecond }?.let { transaction -> DAOTransactionOnly(transaction) }
        }
    }

    /**
     * Calculate the total credit for a given customer based on their transactions.
     *
     * @param username the username of the customer.
     * @return the total credit of the customer.
     * @throws UsernameNotFoundException if the customer does not exist.
     */
    override fun getCreditByCustomer(username: String): Float {
        val customer = customerService.findCustomerByUsername(username)
        if(customer != null) {
            return customer.transactions
                .map { 
                    it.value
                }.fold(0F) { acc, it -> acc + it }
        } else {
            throw UsernameNotFoundException(username)
        }
    }

    /**
     * Retrieve all transactions for a given customer, if the customer exists.
     *
     * @param username the username of the customer.
     * @return the list of transactions of the customer, empty list if no transaction exists or the customer does not exist.
     * @throws UsernameNotFoundException if the customer does not exist.
     */
    override fun getByUsername(username: String): List<DAOTransactionOnly> {
        val customer = customerService.findCustomerByUsername(username)
        if (customer != null) {
            return customer.transactions
                .sortedByDescending { it.time }
                .map { DAOTransactionOnly(it) }
        } else {
            throw UsernameNotFoundException(username)
        }
    }

    /**
     * Create a new transaction for a given customer with the given value.
     *
     * @param customer the customer to add the transaction to.
     * @param value the value of the transaction.
     */
    override fun newTransaction(customer: Customer, value: Float) {
        val transaction = Transaction().apply {
            this.customer = customer
            this.value = value
            time = Instant.now()
        }
        transactionRepository.save(transaction)
    }
}