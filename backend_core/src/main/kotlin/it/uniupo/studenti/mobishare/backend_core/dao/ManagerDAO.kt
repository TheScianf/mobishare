package it.uniupo.studenti.mobishare.backend_core.dao

import it.uniupo.studenti.mobishare.backend_core.entity.Manager
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size

data class DAOManagerInsert(
    @get:Email(message = "Formato email non valido")
    val email: String,
    @get:Size(min = 8, message = "Password non valida, deve essere di almeno 8 caratteri")
    val password: String,
    val admin: Boolean
)

data class DAOManagerOnlyWithParkCount(
    val id: Int,
    val email: String,
    val admin: Boolean,
    val parkCount: Int
) {
    constructor(manager: Manager) : this(
        manager.id,
        manager.email,
        manager.isAdmin,
        manager.parks.size
    )
}