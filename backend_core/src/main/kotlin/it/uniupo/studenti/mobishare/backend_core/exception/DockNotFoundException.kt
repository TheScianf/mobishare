package it.uniupo.studenti.mobishare.backend_core.exception

import it.uniupo.studenti.mobishare.backend_core.annotation.ExceptionResponse

@ExceptionResponse(DockNotFoundException::class, "404", "Dock non trovata")
class DockNotFoundException(message: String) : Exception(message) {

    companion object {
        fun fromParkAndNumber(park: Int, number: Int): DockNotFoundException {
            return DockNotFoundException("Dock with number $number not found in the park $park.")
        }

        fun fromId(dockId: Int): DockNotFoundException {
            return DockNotFoundException("Dock with id $dockId not found.")
        }
    }
}
