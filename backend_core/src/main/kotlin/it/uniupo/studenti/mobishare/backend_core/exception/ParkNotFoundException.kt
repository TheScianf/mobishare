package it.uniupo.studenti.mobishare.backend_core.exception

import it.uniupo.studenti.mobishare.backend_core.annotation.ExceptionResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
@ExceptionResponse(ParkNotFoundException::class, "404", "Parcheggio non trovato")
class ParkNotFoundException(id: Int): Exception("Park with id: $id not found."){
}