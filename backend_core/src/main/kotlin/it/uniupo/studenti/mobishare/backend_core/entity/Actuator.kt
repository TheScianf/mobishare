package it.uniupo.studenti.mobishare.backend_core.entity

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull

@Entity
@Table(name = "ACTUATOR", schema = "mobishare")
class Actuator {
    @EmbeddedId
    lateinit var id: ActuatorId

    @JsonManagedReference
    @MapsId("type")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "type", nullable = false)
    lateinit var type: ActuatorType

    @JsonManagedReference
    @MapsId("idDock")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idDock", nullable = false)
    lateinit var dock: Dock

    @NotNull
    @Column(name = "value", nullable = false)
    var value: String = "NONE"
}