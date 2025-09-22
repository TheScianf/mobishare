package it.uniupo.studenti.mobishare.backend_core.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.hibernate.Hibernate
import java.io.Serializable
import java.time.Instant
import java.util.*

@Embeddable
class CustomerReportId : Serializable {
    @NotNull
    @Column(name = "time", nullable = false)
    var time: Instant? = null

    @Size(max = 50)
    @NotNull
    @Column(name = "idCustomer", nullable = false, length = 50)
    var idCustomer: String? = null
    override fun hashCode(): Int = Objects.hash(time, idCustomer)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false

        other as CustomerReportId

        return time == other.time &&
                idCustomer == other.idCustomer
    }

    companion object {
        private const val serialVersionUID = 0L
    }
}