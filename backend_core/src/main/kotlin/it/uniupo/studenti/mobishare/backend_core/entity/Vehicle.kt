package it.uniupo.studenti.mobishare.backend_core.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.ColumnDefault
import java.time.LocalDate

@Entity
@Table(name = "VEHICLE", schema = "mobishare")
class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Int = 0

    @NotNull
    @Column(name = "immissionDate", nullable = false)
    lateinit var immissionDate: LocalDate

    @Column(name = "dismissionDate")
    var dismissionDate: LocalDate? = null

    @ColumnDefault("false")
    @Column(name="disabled", nullable = false)
    var disabled: Boolean = false

    @JsonManagedReference
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idVehicleType", nullable = false)
    lateinit var vehicleType: VehicleType

    @JsonBackReference
    @OneToMany(mappedBy = "vehicle")
    var customerReports: MutableSet<CustomerReport> = mutableSetOf()

    @JsonBackReference
    @OneToMany(mappedBy = "vehicle")
    var maintainances: MutableSet<Maintenance> = mutableSetOf()

    @JsonBackReference
    @OneToMany(mappedBy = "vehicle")
    var sensors: MutableSet<Sensor> = mutableSetOf()

    @JsonManagedReference
    @OneToOne(mappedBy = "vehicle", optional = true)
    var dock: VehicleDock? = null
}