package it.uniupo.studenti.mobishare.backend_core.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Entity
@Table(
    name = "TYPE_VEHICLE", schema = "mobishare", uniqueConstraints = [
        UniqueConstraint(name = "UK_TV_1", columnNames = ["name"])
    ]
)
class VehicleType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Int = 0

    @Size(max = 50)
    @NotNull
    @Column(name = "name", nullable = false, length = 50)
    lateinit var name: String

    @Size(max = 300)
    @NotNull
    @Column(name = "description", nullable = false, length = 300)
    lateinit var description: String

    @NotNull
    @Column(name = "constantPrice", nullable = false)
    var constantPrice: Float = 0f

    @NotNull
    @Column(name = "minutePrice", nullable = false)
    var minutePrice: Float = 0f

    @JsonBackReference
    @OneToMany(mappedBy = "vehicleType")
    var vehicles: MutableSet<Vehicle> = mutableSetOf()

    @JsonBackReference
    @OneToMany(mappedBy = "vehicleType")
    var valueRanges: MutableSet<ValueRange> = mutableSetOf()
}