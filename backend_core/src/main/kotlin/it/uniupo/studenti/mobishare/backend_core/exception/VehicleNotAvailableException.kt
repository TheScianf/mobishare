package it.uniupo.studenti.mobishare.backend_core.exception

import it.uniupo.studenti.mobishare.backend_core.annotation.ExceptionResponse

@ExceptionResponse(VehicleNotAvailableException::class, "406", "Vehicle isn' available")
class VehicleNotAvailableException(vehicleId: Int): Exception("Vehicle $vehicleId isn' available") {
}
