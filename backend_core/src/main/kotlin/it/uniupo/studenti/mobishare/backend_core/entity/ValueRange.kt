package it.uniupo.studenti.mobishare.backend_core.entity

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*

@Entity
@Table(name = "VALUE_RANGE", schema = "mobishare")
class ValueRange {
    @EmbeddedId
    lateinit var id: ValueRangeId

    @JsonManagedReference
    @MapsId("idVehicleType")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idVehicleType", nullable = false)
    lateinit var vehicleType: VehicleType

    @JsonManagedReference
    @MapsId("idSensorType")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idSensorType", nullable = false)
    lateinit var sensorType: SensorType

    @Column(name = "min")
    var min: Int = 0

    @Column(name = "max")
    var max: Int = 0
}