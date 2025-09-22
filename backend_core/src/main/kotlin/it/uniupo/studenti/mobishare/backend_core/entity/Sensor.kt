package it.uniupo.studenti.mobishare.backend_core.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull

@Entity
@Table(
    name = "SENSOR", schema = "mobishare", uniqueConstraints = [
        UniqueConstraint(name = "UK_SENSOR_1", columnNames = ["idSensorType", "idVehicle"])
    ]
)
class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Int = 0

    @JsonManagedReference
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idSensorType", nullable = false)
    lateinit var sensorType: SensorType

    @JsonManagedReference
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idVehicle", nullable = false)
    lateinit var vehicle: Vehicle

    @JsonBackReference
    @OneToMany(mappedBy = "sensor")
    var sensorReports: MutableSet<SensorReport> = mutableSetOf()
}