package it.uniupo.studenti.mobishare.backend_core.exception

import it.uniupo.studenti.mobishare.backend_core.annotation.ExceptionResponse

@ExceptionResponse(UsernameNotAvailableException::class, "409", "Vehicle not parked in the dock")
class VehicleDockNotExistException(vehicleId: Int): Exception("Vehicle number $vehicleId isn't parked in the dock!") {
}