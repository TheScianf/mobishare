package it.uniupo.studenti.mobishare.backend_core.controller

import it.uniupo.studenti.mobishare.backend_core.dao.DAOCustomerWithSuspension
import it.uniupo.studenti.mobishare.backend_core.dao.DAOSuspensionRejectStatus
import it.uniupo.studenti.mobishare.backend_core.service.SuspensionService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/suspensions")
class SuspensionController(
    val suspensionService: SuspensionService,
) {

    /**
     * Return a list of all suspensions.
     *
     * @param active filter by active status, if any
     * @return a list of [DAOCustomerWithSuspension] objects, or an empty list if no suspensions are found
     */
    @GetMapping("")
    @PreAuthorize("hasAnyRole('MANAGER')")
    private fun getAll(
        @RequestParam("active", required = false) active: Boolean?,
    ): ResponseEntity<List<DAOCustomerWithSuspension>> {
        return if (active != null) {
            ResponseEntity.ok(
                suspensionService.getAllByActive(active)
            )
        } else {
            ResponseEntity.ok(
                suspensionService.getAll()
                    .map {
                        DAOCustomerWithSuspension(
                            c = it.customer,
                            s = it
                        )
                    }
            )
        }
    }

    /**
     * Checks if a customer with the given username is permanently suspended.
     *
     * @param username the username of the customer to check
     * @return true if the customer is permanently suspended, false otherwise
     */
    @GetMapping("/byUser/{username}/suspended-permanently")
    @PreAuthorize("hasAnyRole('MANAGER', 'CUSTOMER')")
    fun isPermanentSuspended(
        @PathVariable("username") username: String,
    ): ResponseEntity<Boolean> {
        return ResponseEntity.ok(
            suspensionService.isPermanentSuspended(username)
        )
    }

/**
 * Updates the rejection status of the last suspension for a given user.
 *
 * @param username the username of the customer whose last suspension's rejection status is to be updated
 * @param status the new rejection status to be set for the customer's last suspension
 * @return a ResponseEntity with no content if the update is successful
 */

    @PutMapping("/byUser/{username}/last/isRejected")
    @PreAuthorize("hasRole('ADMIN')")
    fun setUserLastSuspensionRejectedStatus(
        @PathVariable("username") username: String,
        @Valid @RequestBody(required = true) status: DAOSuspensionRejectStatus,
        bindingResult: BindingResult
    ): ResponseEntity<*> {
        if (bindingResult.hasErrors()) {
            val errors = bindingResult.fieldErrors.associate { it.field to it.defaultMessage }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errors)
        }
        suspensionService.setUserLastSuspensionRejectedStatus(username, status.isRejected)
        return ResponseEntity.ok().build<Void>()
    }

    //TODO creare endpoint per cronologia sospensioni di utente
}
