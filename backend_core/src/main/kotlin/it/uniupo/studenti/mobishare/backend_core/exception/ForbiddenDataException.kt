package it.uniupo.studenti.mobishare.backend_core.exception

import it.uniupo.studenti.mobishare.backend_core.annotation.ExceptionResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.FORBIDDEN)
@ExceptionResponse(ForbiddenDataException::class, "403", "Accesso negato per la risorsa richiesta in quanto non posseduta")
class ForbiddenDataException(
    username: String,
): Exception(
    "The requested data is not owned by '$username'."
)