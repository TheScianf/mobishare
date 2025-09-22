package it.uniupo.studenti.mobishare.backend_core.dao

import jakarta.persistence.Column
import jakarta.persistence.UniqueConstraint
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class DAOLoginRequest(val username: String, val password: String)
data class DAOLoginResponse(val token: String)
data class DAORegisterRequest(
    @get:Size(min = 4, max = 50)
    var username: String,

    @get:Size(min = 8, message = "Password non valida, deve essere di almeno 8 caratteri")
    var password: String,

    @get:Size(min = 4, max = 50)
    var name: String,

    @get:Size(min = 4, max = 50)
    var surname: String,

    @get:Pattern(regexp = "[MFX]", message = "Non valido, scegli tra M, F e X")
    var gender: String,

    @get:Size(min = 16, max = 16, message = "CF non valido, deve essere di 16 caratteri")
    var cf: String,

    @get:Email(message = "Formato email non valido")
    var email: String,
)

data class DAORegisterResponse(val username: String)
