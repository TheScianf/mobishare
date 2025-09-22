package it.uniupo.studenti.mobishare.backend_core.exception

import it.uniupo.studenti.mobishare.backend_core.annotation.ExceptionResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.CONFLICT)
@ExceptionResponse(UsernameNotAvailableException::class, "409", "Username già in uso")
class UsernameNotAvailableException(private val username: String): Exception(
    "Username '$username' not available, already in use."
)