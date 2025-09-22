package it.uniupo.studenti.mobishare.backend_core.entity

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import java.time.Instant

@Entity
@Table(name = "TRANSACTION", schema = "mobishare")
class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Int = 0

    @JsonManagedReference
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idCustomer", nullable = false)
    lateinit var customer: Customer

    @NotNull
    @Column(name = "value", nullable = false)
    var value: Float = 0f

    @NotNull
    @Column(name = "time", nullable = false)
    lateinit var time: Instant
}