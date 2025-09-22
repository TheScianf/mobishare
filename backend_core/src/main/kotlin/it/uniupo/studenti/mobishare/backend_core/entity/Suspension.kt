package it.uniupo.studenti.mobishare.backend_core.entity

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import java.time.Instant

@Entity
@Table(name = "SUSPENSION", schema = "mobishare")
class Suspension {
    @EmbeddedId
    lateinit var id: SuspensionId

    @JsonManagedReference
    @MapsId("idCustomer")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idCustomer", nullable = false)
    lateinit var customer: Customer

    @Column(name = "end", nullable = true)
    var end: Instant? = null

    @NotNull
    @Column(name = "value", nullable = false)
    var value: Float = 0f

    @Column(name = "isRejected", nullable = true)
    var isRejected: Boolean? = null
}
