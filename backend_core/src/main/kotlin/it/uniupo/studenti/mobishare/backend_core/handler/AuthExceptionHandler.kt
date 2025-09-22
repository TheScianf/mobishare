package it.uniupo.studenti.mobishare.backend_core.handler

import it.uniupo.studenti.mobishare.backend_core.exception.EmailAlreadyExistsException
import it.uniupo.studenti.mobishare.backend_core.exception.ForbiddenDataException
import it.uniupo.studenti.mobishare.backend_core.exception.UsernameNotAvailableException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authorization.AuthorizationDeniedException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class AuthExceptionHandler {

    @ExceptionHandler(UsernameNotAvailableException::class)
    private fun usernameNotAvailableException(
        ex: UsernameNotAvailableException
    ): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
            "Error creating customer:  " + ex.message
        )
    }

    @ExceptionHandler(AuthorizationDeniedException::class)
    private fun authorizationDeniedException(
        ex: AuthorizationDeniedException
    ): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied for your role")
    }

    @ExceptionHandler(ForbiddenDataException::class)
    private fun forbiddenDataException(ex: ForbiddenDataException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.message)
    }

    @ExceptionHandler(EmailAlreadyExistsException::class)
    private fun emailAlreadyExistsException(
        ex: EmailAlreadyExistsException
    ): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
            ex.message
        )
    }
}