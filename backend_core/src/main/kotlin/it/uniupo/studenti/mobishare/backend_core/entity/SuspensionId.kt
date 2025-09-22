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
class SuspensionId : Serializable {
    @Size(max = 50)
    @NotNull
    @Column(name = "idCustomer", nullable = false, length = 50)
    lateinit var idCustomer: String

    @NotNull
    @Column(name = "start", nullable = false)
    lateinit var start: Instant
    override fun hashCode(): Int = Objects.hash(idCustomer, start)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false

        other as SuspensionId

        return idCustomer == other.idCustomer &&
                start == other.start
    }

    companion object {
        private const val serialVersionUID = 0L
    }
}
