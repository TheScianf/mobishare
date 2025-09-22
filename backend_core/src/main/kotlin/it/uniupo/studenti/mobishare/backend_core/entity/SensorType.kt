package it.uniupo.studenti.mobishare.backend_core.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Entity
@Table(
    name = "SENSOR_TYPE", schema = "mobishare", uniqueConstraints = [
        UniqueConstraint(name = "UK_ST_1", columnNames = ["name"])
    ]
)
class SensorType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Int = 0

    @Size(max = 35)
    @NotNull
    @Column(name = "name", nullable = false, length = 35)
    lateinit var name: String

    @Size(max = 300)
    @NotNull
    @Column(name = "description", nullable = false, length = 300)
    lateinit var description: String

    @NotNull
    @Column(name = "sendPeriodMin", nullable = false)
    var sendPeriodMin: Int = 0

    @JsonBackReference
    @NotNull
    @OneToMany(mappedBy = "sensorType")
    var sensors: MutableSet<Sensor> = mutableSetOf()

    @JsonBackReference
    @OneToMany(mappedBy = "sensorType")
    var valueRanges: MutableSet<ValueRange> = mutableSetOf()
}