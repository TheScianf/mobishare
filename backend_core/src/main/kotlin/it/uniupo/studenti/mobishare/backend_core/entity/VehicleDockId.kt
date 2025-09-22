package it.uniupo.studenti.mobishare.backend_core.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.validation.constraints.NotNull
import org.hibernate.Hibernate
import java.io.Serializable
import java.util.*

@Embeddable
class VehicleDockId : Serializable {
    @NotNull
    @Column(name = "idVehicle", nullable = false)
    var idVehicle: Int? = null

    @NotNull
    @Column(name = "idDock", nullable = false)
    var idDock: Int? = null
    override fun hashCode(): Int = Objects.hash(idVehicle, idDock)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false

        other as VehicleDockId

        return idVehicle == other.idVehicle &&
                idDock == other.idDock
    }

    companion object {
        private const val serialVersionUID = 0L
    }
}