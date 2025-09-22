package it.uniupo.studenti.mobishare.backend_core.controller

import it.uniupo.studenti.mobishare.backend_core.annotation.ExpectedErrors
import it.uniupo.studenti.mobishare.backend_core.dao.DAOLoginRequest
import it.uniupo.studenti.mobishare.backend_core.dao.DAOLoginResponse
import it.uniupo.studenti.mobishare.backend_core.dao.DAORegisterRequest
import it.uniupo.studenti.mobishare.backend_core.dao.DAORegisterResponse
import it.uniupo.studenti.mobishare.backend_core.entity.Customer
import it.uniupo.studenti.mobishare.backend_core.exception.UsernameNotAvailableException
import it.uniupo.studenti.mobishare.backend_core.service.CustomerService
import it.uniupo.studenti.mobishare.backend_core.service.JwtService
import it.uniupo.studenti.mobishare.backend_core.service.SuspensionService
import jakarta.validation.Valid
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authorization.AuthorizationDeniedException
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(
    val customerService: CustomerService,
    val passwordEncoder: PasswordEncoder,
    val authenticationManager: AuthenticationManager,
    val jwtService: JwtService,
    val suspensionService: SuspensionService,
) {

    /**
     * Registers a new customer
     *
     * @param registerRequest the request to register a new customer
     * @param bindingResult the binding result of the request
     * @return a ResponseEntity containing the username of the newly registered customer
     * @throws HttpMessageNotReadableException if the request body is not readable
     * @throws UsernameNotAvailableException if the username is already taken
     * @throws DataIntegrityViolationException if the request is not valid
     */
    @PostMapping("/register")
    @ExpectedErrors(
        HttpMessageNotReadableException::class,
        UsernameNotAvailableException::class,
        DataIntegrityViolationException::class
    )
    fun customerRegister(
        @Valid @RequestBody registerRequest: DAORegisterRequest,
        bindingResult: BindingResult
    ): ResponseEntity<*> {
        if (bindingResult.hasErrors()) {
            val errors = bindingResult.fieldErrors.associate { it.field to it.defaultMessage }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errors)
        }

        val newCustomer = Customer()
            .apply {
                usernameU = registerRequest.username
                name = registerRequest.name
                surname = registerRequest.surname
                cf = registerRequest.cf
                gender = registerRequest.gender
                passwordU = passwordEncoder.encode(registerRequest.password)
                email = registerRequest.email
            }

        if (customerService.existsByUsername(newCustomer.usernameU)) {
            throw UsernameNotAvailableException(registerRequest.username)
        }

        val saved = customerService.save(newCustomer)
        return ResponseEntity.status(HttpStatus.CREATED).body(DAORegisterResponse(saved.usernameU))
    }

    /**
     * Handles the customer login request.
     *
     * @param loginRequest the login request containing the username and password.
     * @param bindingResult the result of the validation checks on the login request.
     * @return a ResponseEntity containing the JWT token if authentication is successful,
     *         or a BAD_REQUEST status with validation errors if the request is invalid,
     *         or an UNAUTHORIZED status if authentication fails.
     * @throws HttpMessageNotReadableException if the request body is not readable.
     */
    @PostMapping("/login")
    @ExpectedErrors(
        HttpMessageNotReadableException::class,
    )
    fun customerLogin(
        @Valid @RequestBody loginRequest: DAOLoginRequest,
        bindingResult: BindingResult
    ): ResponseEntity<*> {
        if (bindingResult.hasErrors()) {
            val errors = bindingResult.fieldErrors.associate { it.field to it.defaultMessage }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errors)
        }

        val auth = try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    loginRequest.username,
                    loginRequest.password
                )
            )
        } catch (_: Exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username or Password not correct")
        }

        SecurityContextHolder.getContext().authentication = auth
        val userDetails = auth.principal as UserDetails
        val jwToken = jwtService.generateToken(userDetails)
        return ResponseEntity.ok(DAOLoginResponse(token = jwToken))
    }

    /**
     * special method that returns ok with manager authorization needed.
     * required by the ai_backend to authenticate the requests
     * */
    @GetMapping("manager-login")
    @PreAuthorize("hasRole('MANAGER')")
    fun managerLogin(): ResponseEntity<String> {
        return ResponseEntity.ok("authenticated and authorized")
    }

    /**
     * special method that returns ok with admin authorization needed.
     * required by the ai_backend to authenticate the requests
     * */
    @GetMapping("admin-login")
    @PreAuthorize("hasRole('ADMIN')")
    fun adminLogin(): ResponseEntity<String> {
        return ResponseEntity.ok("authenticated and authorized")
    }

    @GetMapping("customer-login")
    @PreAuthorize("hasRole('CUSTOMER')")
    fun customerLogin(): ResponseEntity<String> {
        return ResponseEntity.ok("authenticated and authorized")
    }

    @GetMapping("token-check")
    @PreAuthorize("hasAnyRole('MANAGER', 'CUSTOMER', 'ADMIN')")
    @ExpectedErrors(AuthorizationDeniedException::class)
    fun tokenCheck(): ResponseEntity<String> {
        return ResponseEntity.ok("")
    }

    /**
     * Check if the customer account is suspended.
     *
     * @param permanently if true, checks for permanent suspension, otherwise for temporary suspension
     * @param userDetails the user details of the customer
     * @return a ResponseEntity containing the result of the check. If the account is suspended, the
     *         response status is OK and the response body is "Your account is [permanently ]suspended".
     *         If the account is not suspended, the response status is UNAUTHORIZED and the response body
     *         is "Account not [permanently ]suspended".
     */
    @GetMapping("suspended-login")
    @PreAuthorize("hasRole('CUSTOMER')")
    fun suspendedLogin(
        @RequestParam("permanently", required = false, defaultValue = "false") permanently: Boolean = false,
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<String> {
        val customer =
            customerService.findCustomerByUsername(userDetails.username) ?: return ResponseEntity.notFound().build()
        if ((if (permanently)
                suspensionService.isPermanentSuspended(customer.usernameU)
            else suspensionService.isSuspended(customer.usernameU))
        ) {
            return ResponseEntity.status(HttpStatus.OK)
                .body("Your account is ${if (permanently) "permanently " else ""}suspended")
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body("Account not ${if (permanently) "permanently " else ""}suspended")
    }
}
