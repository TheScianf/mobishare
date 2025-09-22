package it.uniupo.studenti.mobishare.backend_core.controller

import it.uniupo.studenti.mobishare.backend_core.annotation.ExpectedErrors
import it.uniupo.studenti.mobishare.backend_core.dao.DAOCustomerOnlyNoPassword
import it.uniupo.studenti.mobishare.backend_core.dao.DAORaceOnly
import it.uniupo.studenti.mobishare.backend_core.dao.DAOActiveSuspensionOnly
import it.uniupo.studenti.mobishare.backend_core.dao.DAOTransactionOnly
import it.uniupo.studenti.mobishare.backend_core.exception.ForbiddenDataException
import it.uniupo.studenti.mobishare.backend_core.service.*
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.HttpProtocol
import reactor.netty.http.client.HttpClient

@RestController
@RequestMapping("/customers/")
class CustomerController(
    val transactionService: TransactionService,
    val raceService: RaceService,
    val suspansionService: SuspensionService,
    val customerService: CustomerService,
    val couponService: CouponService,
    val customerReportService: CustomerReportService,
    val vehicleService: VehicleService,
    val reportTypeService: ReportTypeService
) {

    /**
     * Return the last transaction for a given customer, if the customer is the authenticated user.
     *
     * @param username the username of the customer.
     * @param userDetails the authenticated user.
     * @return the last transaction of the customer, null if no transaction exists.
     * @throws ForbiddenDataException if the authenticated user is not the customer.
     * @throws UsernameNotFoundException if the customer does not exist.
     */
    @GetMapping("/{username}/transactions/last")
    @PreAuthorize("hasRole('CUSTOMER')")
    @ExpectedErrors(
        ForbiddenDataException::class,
        UsernameNotFoundException::class
    )
    private fun getLastTransactionByUser(
        @PathVariable("username") username: String,
        @AuthenticationPrincipal userDetails: UserDetails
    ): DAOTransactionOnly? {
        if (userDetails.username == username) {
            return transactionService.getLastByCustomer(username)
        } else {
            throw ForbiddenDataException(username)
        }
    }

    /**
     * Return all suspensions for a given customer, if the customer is the authenticated user.
     *
     * @param username the username of the customer.
     * @param userDetails the authenticated user.
     * @return a ResponseEntity containing the list of active suspensions of the customer, empty list if no suspension exists.
     * @throws ForbiddenDataException if the authenticated user is not the customer.
     * @throws UsernameNotFoundException if the customer does not exist.
     */
    @GetMapping("/{username}/suspansions")
    @PreAuthorize("hasRole('CUSTOMER')")
    @ExpectedErrors(
        ForbiddenDataException::class,
        UsernameNotFoundException::class,
    )
    private fun getSuspensionByUser(
        @PathVariable("username") username: String,
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<List<DAOActiveSuspensionOnly>> {
        if (userDetails.username == username) {
            return ResponseEntity.ok(suspansionService.getAllByUser(username).map {
                DAOActiveSuspensionOnly(it)
            })
        } else {
            throw ForbiddenDataException(username)
        }
    }

    /**
     * Return all races for a given customer, if the customer is the authenticated user.
     *
     * @param username the username of the customer.
     * @param userDetails the authenticated user.
     * @return a ResponseEntity containing the list of races of the customer, empty list if no race exists.
     * @throws ForbiddenDataException if the authenticated user is not the customer.
     * @throws UsernameNotFoundException if the customer does not exist.
     */
    @GetMapping("/{username}/races")
    @PreAuthorize("hasRole('CUSTOMER')")
    @ExpectedErrors(
        ForbiddenDataException::class,
        UsernameNotFoundException::class,
    )
    private fun getRacesByUser(
        @PathVariable("username") username: String,
        @AuthenticationPrincipal userDetails: UserDetails
    ): List<DAORaceOnly>? {
        if (userDetails.username == username) {
            return raceService.findAllRacesByCustomer(username)
        } else {
            throw ForbiddenDataException(username)
        }
    }

    /**
     * Return the customer with the given username, if the customer is the authenticated user.
     *
     * @param username the username of the customer.
     * @param userDetails the authenticated user.
     * @return a ResponseEntity containing the customer, empty body if no customer exists.
     * @throws ForbiddenDataException if the authenticated user is not the customer.
     * @throws UsernameNotFoundException if the customer does not exist.
     */
    @GetMapping("/{username}")
    @PreAuthorize("hasRole('CUSTOMER')")
    @ExpectedErrors(
        ForbiddenDataException::class,
        UsernameNotFoundException::class,
    )
    private fun getCustomer(
        @PathVariable("username") username: String,
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<DAOCustomerOnlyNoPassword> {
        if (userDetails.username == username) {
            return customerService.findCustomerByUsername(username)?.let {
                ResponseEntity.ok(DAOCustomerOnlyNoPassword(it))
            } ?: ResponseEntity.notFound().build()
        } else {
            throw ForbiddenDataException(username)
        }
    }

    /**
     * Return the credit of the customer with the given username, if the customer is the authenticated user.
     *
     * @param username the username of the customer.
     * @param userDetails the authenticated user.
     * @return a ResponseEntity containing the credit of the customer, 0 if no customer exists.
     * @throws ForbiddenDataException if the authenticated user is not the customer.
     * @throws UsernameNotFoundException if the customer does not exist.
     */
    @GetMapping("/{username}/credit")
    @PreAuthorize("hasRole('CUSTOMER')")
    @ExpectedErrors(
        ForbiddenDataException::class,
        UsernameNotFoundException::class,
    )
    private fun getCreditByCustomer(
        @PathVariable("username") username: String,
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<Float> {
        if (userDetails.username == username) {
            return ResponseEntity.ok(transactionService.getCreditByCustomer(username))
        } else {
            throw ForbiddenDataException(username)
        }
    }

/**
 * Retrieve the green points of a customer with the given username, if the customer is the authenticated user.
 *
 * @param username the username of the customer.
 * @param userDetails the authenticated user.
 * @return a ResponseEntity containing the green points of the customer.
 * @throws ForbiddenDataException if the authenticated user is not the customer.
 * @throws UsernameNotFoundException if the customer does not exist.
 */

    @GetMapping("/{username}/greenPoints")
    @PreAuthorize("hasRole('CUSTOMER')")
    @ExpectedErrors(
        ForbiddenDataException::class,
        UsernameNotFoundException::class,
    )
    private fun getGreenPointsByCustomer(
        @PathVariable("username") username: String,
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<Int> {
        if (userDetails.username == username) {
            return ResponseEntity.ok(couponService.getGreenPointsByUsername(username))
        } else {
            throw ForbiddenDataException(username)
        }
    }

    /**
     * Retrieve the payments of a customer with the given username, if the customer is the authenticated user.
     *
     * @param username the username of the customer.
     * @param userDetails the authenticated user.
     * @return a ResponseEntity containing the payments of the customer.
     * @throws ForbiddenDataException if the authenticated user is not the customer.
     * @throws UsernameNotFoundException if the customer does not exist.
     */
    @GetMapping("/{username}/payments")
    @PreAuthorize("hasRole('CUSTOMER')")
    @ExpectedErrors(
        ForbiddenDataException::class,
        UsernameNotFoundException::class,
    )
    private fun getPaymentsByCustomer(
        @PathVariable("username") username: String,
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<List<DAOTransactionOnly>> {
        if (userDetails.username != username) {
            throw ForbiddenDataException(username)
        } else {
            return transactionService.getByUsername(username).let { ResponseEntity.ok(it) }
        }
    }
    /**
    * Submits a new feedback report for a customer on a specific vehicle.
    *
    * @param username the username of the customer submitting the feedback.
    * @param text the feedback text provided by the customer.
    * @param vehicleId the ID of the vehicle related to the feedback.
    * @param token the authorization token of the customer.
    * 
    * The method creates a new customer report and sends the feedback to a Python backend service for formatting.
    * It checks for the existence of the customer, vehicle, and report type before proceeding.
    */
    @PostMapping("/{username}/feedback")
    @PreAuthorize("hasRole('CUSTOMER')")
    private fun newFeedback(
        @PathVariable("username")username: String,
        @RequestParam("text", required = true) text: String,
        @RequestParam("vehicleId")vehicleId:Int,
        @RequestHeader("Authorization")token: String){
        val customer = customerService.findCustomerByUsername(username)
        val vehicle = vehicleService.findById(vehicleId)
        val type = reportTypeService.findById(1)
        customer?.let {
            vehicle?.let { it1 ->
                type?.let {
                        reportType -> customerReportService.newCustomerReport(it, it1, reportType, text)
                    val httpClient = HttpClient.create()
                        .protocol(HttpProtocol.HTTP11) // Force HTTP/1.1
                    val webClient = WebClient.builder()
                        .clientConnector(ReactorClientHttpConnector(httpClient))
                        .build()

                    // Create JSON body
                    val requestBody = mapOf(
                        "feedback" to text,
                        "id_vehicle" to vehicleId
                    )

                    webClient.post()
                        .uri { uriBuilder ->
                            uriBuilder
                                .scheme("http")
                                .host("192.168.0.10")
                                .port(8000)
                                .path("/feedback/format")
                                .queryParam("token", token)  // Keep token as query param
                                .build()
                        }
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(requestBody)
                        .retrieve()
                        .toBodilessEntity()
                        .subscribe(
                            { println("Successfully sent to Python backend") },
                            { error -> println("Error sending to Python backend: ${error.message}") }
                        )
                }
            }
        }
    }
    /**
    * Recharges the wallet of a customer with the specified amount.
    *
    * @param username the username of the customer whose wallet is to be recharged.
    * @param euro the amount in euros to recharge the customer's wallet.
    * @throws UsernameNotFoundException if the customer does not exist.
    */
    @PostMapping("/{username}/wallet/recharge")
    @PreAuthorize("hasRole('CUSTOMER')")
    private fun rechargeWallet(@PathVariable("username")username: String, @RequestParam("euro", required = true)euro: Float){
        val customer = customerService.findCustomerByUsername(username)
        customer?.let {
            transactionService.newTransaction(it, euro)
        }
    }
}