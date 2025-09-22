package it.uniupo.studenti.mobishare.backend_core.handler

import it.uniupo.studenti.mobishare.backend_core.exception.CustomerIsSuspendedException
import it.uniupo.studenti.mobishare.backend_core.exception.DockNotFoundException
import it.uniupo.studenti.mobishare.backend_core.exception.ParkNotFoundException
import it.uniupo.studenti.mobishare.backend_core.exception.SuspensionNotFoundException
import it.uniupo.studenti.mobishare.backend_core.exception.UsernameNotFoundException
import it.uniupo.studenti.mobishare.backend_core.exception.VehicleDockNotExistException
import it.uniupo.studenti.mobishare.backend_core.exception.VehicleNotAvailableException
import it.uniupo.studenti.mobishare.backend_core.exception.VehicleNotExistException
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.NoHandlerFoundException
import org.springframework.web.servlet.resource.NoResourceFoundException


@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadable(ex: HttpMessageNotReadableException): ResponseEntity<String> {
        val errorMessage = "Invalid request body: " + ex.message
        return ResponseEntity<String>(errorMessage, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<String> {
        val errorMessage = "Contact the developer... Exception: " + ex.javaClass.simpleName
        println("ECCEZIONE NON GESTITA")
        ex.printStackTrace()
        return ResponseEntity<String>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR)
    }
 @ExceptionHandler(NoResourceFoundException::class)
    fun noResouceFoundException(ex: NoResourceFoundException): ResponseEntity<String> {
        return ResponseEntity<String>(ex.message, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(NoHandlerFoundException::class)
    fun noResouceFoundException(ex: NoHandlerFoundException): ResponseEntity<String> {
        return ResponseEntity<String>(ex.message, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(UsernameNotFoundException::class)
    fun usernameNotFoundException(ex: UsernameNotFoundException): ResponseEntity<String> {
        return ResponseEntity<String>(ex.message, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(ParkNotFoundException::class)
    fun noParkFoundException(ex: ParkNotFoundException): ResponseEntity<String> {
        return ResponseEntity(ex.message, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(DockNotFoundException::class)
    fun dockNotFoundException(ex: DockNotFoundException): ResponseEntity<String> {
        return ResponseEntity<String>(ex.message, HttpStatus.NOT_FOUND)
    }
    @ExceptionHandler(VehicleNotExistException::class)
    fun vehicleNotExistException(ex: VehicleNotExistException): ResponseEntity<String>{
        return ResponseEntity(ex.message, HttpStatus.valueOf(405))
    }
    @ExceptionHandler(VehicleNotAvailableException::class)
    fun vehicleNotAvailableException(ex: VehicleNotAvailableException): ResponseEntity<String>{
        return ResponseEntity(ex.message, HttpStatus.valueOf(406))
    }
    @ExceptionHandler(CustomerIsSuspendedException::class)
    fun customerIsSuspendedException(ex: CustomerIsSuspendedException): ResponseEntity<String>{
        return ResponseEntity(ex.message, HttpStatus.valueOf(402))
    }
    @ExceptionHandler(VehicleDockNotExistException::class)
    fun vehicleDockNotExistexception(ex: VehicleDockNotExistException): ResponseEntity<String>{
        return ResponseEntity(ex.message, HttpStatus.valueOf(409))
    }
    @ExceptionHandler(DataIntegrityViolationException::class)
    fun dataIntegrityViolationException(ex: DataIntegrityViolationException): ResponseEntity<String> {
        return ResponseEntity<String>("Violazione di un vincolo della base di dati, contatta il dev: " +
                ex.message, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(SuspensionNotFoundException::class)
    fun suspensionNotFoundException(ex: SuspensionNotFoundException): ResponseEntity<String> {
        return ResponseEntity<String>(ex.message, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun missingServletRequestParameterException(ex: MissingServletRequestParameterException): ResponseEntity<String> {
        val errorMessage = "Missing request query (?name=value) parameter: " + ex.parameterName
        return ResponseEntity<String>(errorMessage, HttpStatus.BAD_REQUEST)
    }
}
