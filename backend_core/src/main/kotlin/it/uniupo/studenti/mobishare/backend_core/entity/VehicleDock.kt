package it.uniupo.studenti.mobishare.backend_core.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*

@Entity
@Table(
    name = "VEHICLE_DOCK", schema = "mobishare", uniqueConstraints = [
        UniqueConstraint(name = "UK_VE_1_VEHICLE", columnNames = ["idVehicle"]),
        UniqueConstraint(name = "UK_VE_2_DOCK", columnNames = ["idDock"])
    ]
)
class VehicleDock {
    @EmbeddedId
    lateinit var id: VehicleDockId

    @JsonBackReference
    @MapsId("idVehicle")
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idVehicle", nullable = false)
    lateinit var vehicle: Vehicle

    @JsonBackReference
    @MapsId("idDock")
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idDock", nullable = false)
    lateinit var dock: Dock


}