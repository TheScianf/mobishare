package it.uniupo.studenti.mobishare.backend_core.exception

import it.uniupo.studenti.mobishare.backend_core.annotation.ExceptionResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
@ExceptionResponse(VehicleNotExistException::class, "405", "Vehicle doesn't exist")
class VehicleNotExistException(vehicleId: Int): Exception("Vehicle $vehicleId does not exist") {
}