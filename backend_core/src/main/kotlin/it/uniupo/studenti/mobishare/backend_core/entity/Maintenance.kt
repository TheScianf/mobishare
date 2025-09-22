package it.uniupo.studenti.mobishare.backend_core.entity

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDate

@Entity
@Table(name = "MAINTENANCE", schema = "mobishare")
class Maintenance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Int = 0

    @JsonManagedReference
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idVehicle", nullable = false)
    lateinit var vehicle: Vehicle

    @Size(max = 300)
    @NotNull
    @Column(name = "description", nullable = false, length = 300)
    lateinit var description: String

    @NotNull
    @Column(name = "start", nullable = false)
    lateinit var start: LocalDate

    @Column(name = "end", nullable = true)
    var end: LocalDate? = null
}