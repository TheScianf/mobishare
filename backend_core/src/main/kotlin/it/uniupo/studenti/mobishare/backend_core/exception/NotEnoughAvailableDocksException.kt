package it.uniupo.studenti.mobishare.backend_core.exception

import it.uniupo.studenti.mobishare.backend_core.annotation.ExceptionResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
@ExceptionResponse(NotEnoughAvailableDocksException::class, "400", "Not enough docks")
class NotEnoughAvailableDocksException (parkId: Int): Exception("Not enough docks at park number $parkId") {
}