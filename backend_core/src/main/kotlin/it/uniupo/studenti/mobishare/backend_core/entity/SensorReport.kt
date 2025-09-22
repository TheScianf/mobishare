package it.uniupo.studenti.mobishare.backend_core.entity

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.ColumnDefault
import java.util.Date

@Entity
@Table(name = "SENSOR_REPORT", schema = "mobishare")
class SensorReport {
    @Id
    @Column(name = "time", nullable = false)
    lateinit var time: Date

    @JsonManagedReference
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idSensor", nullable = false)
    lateinit var sensor: Sensor

    @NotNull
    @Column(name = "value", nullable = false)
    var value: Float = 0f

    @NotNull
    @ColumnDefault("P")
    @Lob
    @Column(name = "state", nullable = false)
    lateinit var state: String
}