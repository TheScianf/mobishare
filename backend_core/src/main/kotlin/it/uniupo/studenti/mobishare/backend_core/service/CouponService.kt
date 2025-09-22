package it.uniupo.studenti.mobishare.backend_core.service

import it.uniupo.studenti.mobishare.backend_core.entity.Coupon
import it.uniupo.studenti.mobishare.backend_core.entity.Customer
import it.uniupo.studenti.mobishare.backend_core.exception.UsernameNotFoundException
import it.uniupo.studenti.mobishare.backend_core.repository.CouponRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate

interface CouponService {
    fun getGreenPointsByUsername(username: String): Int
    fun useCoupons(username: String, amount: Float): Int
    fun addGreenPoints(customer: Customer, points: Int) // <- Aggiungi questa riga
}

@Service
class CouponServiceImpl : CouponService {

    @Autowired
    private lateinit var couponRepository: CouponRepository

    @Autowired
    private lateinit var customerService: CustomerService

    /**
     * Gets the total green points of a customer.
     *
     * @param username the username of the customer
     * @return the total green points of the customer
     * @throws UsernameNotFoundException if the customer is not found
     */
    override fun getGreenPointsByUsername(username: String): Int {
        val customerExists = customerService.findCustomerByUsername(username) != null
        if (customerExists) {
            return couponRepository.findAllByCustomerUsernameUAndUsed(username, false)
                .filter { it.experiation.isAfter(LocalDate.now()) }
                .sumOf { it.value }
        } else {
            throw UsernameNotFoundException(username)
        }
    }

        /**
         * Use the coupons of a customer and return the green points used.
         *
         * @param username the username of the customer
         * @param amount the amount of green points to use
         * @return the green points used
         * @throws UsernameNotFoundException if the customer is not found
         */
    override fun useCoupons(username: String, amount: Float): Int {
        val customerExists = customerService.findCustomerByUsername(username) != null
        if (customerExists) {
            val coupon = couponRepository.findAllByCustomerUsernameUAndUsed(username, false)
            var amount_decreased= (amount*100).toInt()
            var greenPoints = 0
            for (c in coupon){
                if (c.experiation.isAfter(LocalDate.now())){
                    if(c.value <= amount_decreased) {
                        amount_decreased -= c.value
                        greenPoints += c.value
                        c.used = true
                    } else {
                        val newValue = c.value - amount_decreased
                        greenPoints += amount_decreased
                        c.value = newValue
                        return greenPoints
                    }
                }
            }
            return greenPoints
        } else {
            throw UsernameNotFoundException(username)
        }
    }


    /**
     * Adds green points to a customer by creating a new coupon.
     *
     * @param customer the customer to add points to
     * @param points the number of green points to add
     */
    override fun addGreenPoints(customer: Customer, points: Int) {
        val coupon = Coupon().apply {
            this.customer = customer
            this.value = points
            this.used = false
            this.experiation = LocalDate.now().plusYears(1) // Scadenza a 1 anno, puoi modificarla
        }
        couponRepository.save(coupon)
    }

}