package it.uniupo.studenti.mobishare.backend_core.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.validation.constraints.NotNull
import org.hibernate.Hibernate
import java.io.Serializable
import java.util.*

@Embeddable
class ValueRangeId : Serializable {
    @NotNull
    @Column(name = "idVehicleType", nullable = false)
    var idVehicleType: Int? = null

    @NotNull
    @Column(name = "idSensorType", nullable = false)
    var idSensorType: Int? = null
    override fun hashCode(): Int = Objects.hash(idVehicleType, idSensorType)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false

        other as ValueRangeId

        return idVehicleType == other.idVehicleType &&
                idSensorType == other.idSensorType
    }

    companion object {
        private const val serialVersionUID = 0L
    }
}