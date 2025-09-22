package it.uniupo.studenti.mobishare.backend_core.exception

import it.uniupo.studenti.mobishare.backend_core.annotation.ExceptionResponse

@ExceptionResponse(CustomerIsSuspendedException::class, "402", "User is suspended")
class CustomerIsSuspendedException(username: String): Exception("$username is suspended!"){
}