package it.uniupo.studenti.mobishare.backend_core.repository

import it.uniupo.studenti.mobishare.backend_core.entity.Coupon
import org.springframework.data.jpa.repository.JpaRepository

interface CouponRepository : JpaRepository<Coupon, Int> {
    fun findAllByCustomerUsernameUAndUsed(username: String, used: Boolean): List<Coupon>

}