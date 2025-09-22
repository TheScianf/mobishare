package it.uniupo.studenti.mobishare.backend_core.entity

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull

@Entity
@Table(
    name = "RACE", schema = "mobishare", indexes = [
        Index(name = "FK_RACE_2_VEHICLE", columnList = "idVehicle")
    ]
)
class Race {
    @EmbeddedId
    lateinit var id: RaceId

    @JsonManagedReference
    @MapsId("idCustomer")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idCustomer", nullable = false)
    lateinit var customer: Customer

    @NotNull
    @Column(name = "minDuration", nullable = false)
    var minDuration: Int = 0
}
