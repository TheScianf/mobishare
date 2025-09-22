package it.uniupo.studenti.mobishare.backend_core.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.hibernate.Hibernate
import java.io.Serializable
import java.util.*

@Embeddable
class ActuatorId : Serializable {
    @Size(max = 50)
    @NotNull
    @Column(name = "type", nullable = false, length = 50)
    var type: String? = null

    @NotNull
    @Column(name = "idDock", nullable = false)
    var idDock: Int? = null
    override fun hashCode(): Int = Objects.hash(type, idDock)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false

        other as ActuatorId

        return type == other.type &&
                idDock == other.idDock
    }

    companion object {
        private const val serialVersionUID = 0L
    }
}