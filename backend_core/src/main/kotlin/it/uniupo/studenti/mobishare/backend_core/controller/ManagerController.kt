package it.uniupo.studenti.mobishare.backend_core.controller

import it.uniupo.studenti.mobishare.backend_core.dao.DAOManagerInsert
import it.uniupo.studenti.mobishare.backend_core.dao.DAOManagerOnlyWithParkCount
import it.uniupo.studenti.mobishare.backend_core.exception.EmailAlreadyExistsException
import it.uniupo.studenti.mobishare.backend_core.service.ManagerService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.*
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/managers")
class ManagerController(
    val managerService: ManagerService
) {

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    fun getAllManagers(): ResponseEntity<List<DAOManagerOnlyWithParkCount>> {
        return ok(managerService.findAll())
    }

    @PutMapping("/{id}/toggleAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    fun toggleAdmin(
        @PathVariable id: Int,
    ): ResponseEntity<Void> {
        val ret = managerService.toggleAdmin(id)
        return if (ret) {
            ok().build()
        } else {
            status(HttpStatus.UNAUTHORIZED).build()
        }
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    fun addManager(
        @Valid @RequestBody manager: DAOManagerInsert,
        bindingResult: BindingResult
    ): ResponseEntity<*> {
        if (bindingResult.hasErrors()) {
            val errors = bindingResult.fieldErrors.associate { it.field to it.defaultMessage }
            return status(HttpStatus.BAD_REQUEST)
                .body(errors)
        }
        managerService.add(manager)
        return status(HttpStatus.CREATED).body("created manager with email ${manager.email}")
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteManager(
        @PathVariable id: Int,
        @RequestParam(required = true) substitutorId: Int
    ): ResponseEntity<Void> {
        managerService.delete(id, substitutorId)
        return ok().build()
    }

    @DeleteMapping("/email")
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteManagerByName(
        @RequestParam(required = true) email: String,
        @RequestParam(required = true) subEmail: String
    ): ResponseEntity<Void> {
        managerService.deleteByEmail(email, subEmail)
        return ok().build()
    }
}
