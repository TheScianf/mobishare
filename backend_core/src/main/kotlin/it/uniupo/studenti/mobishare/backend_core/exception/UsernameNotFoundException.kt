package it.uniupo.studenti.mobishare.backend_core.exception

import it.uniupo.studenti.mobishare.backend_core.annotation.ExceptionResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
@ExceptionResponse(UsernameNotFoundException::class,"404", "Username non trovato tra i Customer")
class UsernameNotFoundException(username: String) : Exception(
    "Username '$username' not found."
)