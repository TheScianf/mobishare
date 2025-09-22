package it.uniupo.studenti.mobishare.backend_core.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.hibernate.Hibernate
import org.hibernate.annotations.ColumnDefault
import java.io.Serializable
import java.time.Instant
import java.util.*

@Embeddable
class RaceId : Serializable {
    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "start", nullable = false)
    var start: Instant? = null

    @Size(max = 50)
    @NotNull
    @Column(name = "idCustomer", nullable = false, length = 50)
    var idCustomer: String? = null

    @NotNull
    @Column(name = "idVehicle", nullable = false)
    var idVehicle: Int? = null
    override fun hashCode(): Int = Objects.hash(start, idCustomer, idVehicle)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false

        other as RaceId

        return start == other.start &&
                idCustomer == other.idCustomer &&
                idVehicle == other.idVehicle
    }

    companion object {
        private const val serialVersionUID = 0L
    }
}