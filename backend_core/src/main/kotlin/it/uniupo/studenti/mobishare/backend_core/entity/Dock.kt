package it.uniupo.studenti.mobishare.backend_core.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull

@Entity
@Table(
    name = "DOCK", schema = "mobishare", uniqueConstraints = [
        UniqueConstraint(name = "UK_DOCK_1", columnNames = ["number", "idPark"])
    ]
)
class Dock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Int = 0

    @NotNull
    @Column(name = "number", nullable = false)
    var number: Int = 0

    @JsonManagedReference
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idPark", nullable = false)
    lateinit var park: Park

    @JsonBackReference
    @OneToMany(mappedBy = "dock")
    var attuators: MutableSet<Actuator> = mutableSetOf()

    @JsonManagedReference
    @OneToOne(mappedBy = "dock", optional = true)
    var vehicle: VehicleDock? = null
}