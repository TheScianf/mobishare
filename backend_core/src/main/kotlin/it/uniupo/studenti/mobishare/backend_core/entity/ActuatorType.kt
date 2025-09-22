package it.uniupo.studenti.mobishare.backend_core.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import jakarta.validation.constraints.Size

@Entity
@Table(name = "ACTUATOR_TYPE", schema = "mobishare")
class ActuatorType {
    @Id
    @Size(max = 50)
    @Column(name = "type", nullable = false, length = 50)
    lateinit var type: String

    @JsonBackReference
    @OneToMany(mappedBy = "type", cascade = [CascadeType.ALL])
    var actuators: MutableSet<Actuator> = mutableSetOf()
}