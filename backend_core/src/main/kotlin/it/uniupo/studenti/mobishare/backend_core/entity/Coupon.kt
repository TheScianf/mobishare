package it.uniupo.studenti.mobishare.backend_core.entity

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.time.LocalDate

@Entity
@Table(name = "COUPON", schema = "mobishare")
class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Int? = null

    @JsonManagedReference
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "idCustomer", nullable = false)
    lateinit var customer: Customer

    @NotNull
    @Column(name = "used", nullable = false)
    var used: Boolean = false

    @NotNull
    @Column(name = "expiration", nullable = false)
    lateinit var experiation: LocalDate

    @NotNull
    @Column(name = "value", nullable = false)
    var value: Int = 0
}