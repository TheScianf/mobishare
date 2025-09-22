package it.uniupo.studenti.mobishare.backend_core.controller

import io.swagger.v3.oas.annotations.Operation
import it.uniupo.studenti.mobishare.backend_core.annotation.ExpectedErrors
import it.uniupo.studenti.mobishare.backend_core.dao.*
import it.uniupo.studenti.mobishare.backend_core.exception.DockNotFoundException
import it.uniupo.studenti.mobishare.backend_core.exception.NotEnoughAvailableDocksException
import it.uniupo.studenti.mobishare.backend_core.exception.ParkNotFoundException
import it.uniupo.studenti.mobishare.backend_core.service.CustomerReportService
import it.uniupo.studenti.mobishare.backend_core.service.DockService
import it.uniupo.studenti.mobishare.backend_core.service.ParkService
import it.uniupo.studenti.mobishare.backend_core.service.SensorReportService
import it.uniupo.studenti.mobishare.backend_core.service.SensorService
import it.uniupo.studenti.mobishare.backend_core.service.SensorTypeService
import it.uniupo.studenti.mobishare.backend_core.service.VehicleDockService
import it.uniupo.studenti.mobishare.backend_core.service.VehicleService
import it.uniupo.studenti.mobishare.backend_core.service.VehicleTypeService
import it.uniupo.studenti.mobishare.backend_core.exception.UsernameNotFoundException
import it.uniupo.studenti.mobishare.backend_core.service.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.HttpProtocol
import reactor.netty.http.client.HttpClient

@RestController
@RequestMapping("/parks")
class ParkController(
    val vehicleService: VehicleService,
    val parkService: ParkService,
    val sensorReportService: SensorReportService,
    val customeReportService: CustomerReportService,
    val dockService: DockService,
    val vehicleDockService: VehicleDockService,
    val vehicleTypeService: VehicleTypeService,
    val sensorTypeService: SensorTypeService,
    val sensorService: SensorService,
    val managerService: ManagerService,
) {

    /**
     * Returns a list of all vehicles in a park with a given status.
     *
     * @param parkId the id of the park
     * @param status the status to filter by, if any
     * @return a list of [DAOVehicleWithStatus] objects, or an empty list if no vehicles are found
     * @throws ParkNotFoundException if the park does not exist
     */
    @GetMapping("/{parkId}/vehicles")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @ExpectedErrors(
        ParkNotFoundException::class
    )
    private fun getVehiclesByPark(
        @PathVariable("parkId") parkId: Int,
        @RequestParam("status", required = false) status: String?,
    ): ResponseEntity<List<DAOVehicleWithStatus>> {
        var vs: List<DAOVehicleWithStatus> = vehicleService.findAllByPark(parkId)
            .map {
                vehicleService.addStatus(it)
            }

        if (status != null) {
            val status = VehicleStatus.fromString(status)
            vs = vs.filter { it.status == status }
        }

        return ResponseEntity.ok(vs)
    }

    /**
     * Returns a list of parks with their docks and vehicles, filtered by given park ids.
     *
     * @param parkId the id of the park
     * @return a list of [DAOParkWithVehicles] objects, or an empty list if no parks are found
     * @throws ParkNotFoundException if any of the given park ids do not exist
     */
    @GetMapping("/vehiclesWithStatusAndReports")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    @Operation(
        summary = "Get parks with vehicles, status and reports",
        description = "Returns a list of parks with their docks and vehicles information including status and reports. " +
                "For managers, returns only their assigned parks. For admins, returns all parks."
    )
    @ExpectedErrors(
        UsernameNotFoundException::class
    )
    private fun getVehicles(
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<List<DAOParkWithVehicles>> {
        var username: String? = null
        if (userDetails.authorities.none { it.authority == "ROLE_ADMIN" }) {
            // if ur not an admin
            username = userDetails.username
        }

        val parks = username?.let {
//            parkService.findById(it) ?: throw ParkNotFoundException(it)
            (managerService.findByEmail(username) ?: throw UsernameNotFoundException(username))
                .parks
        } ?: parkService.findAll()

        return parks.map { it ->
            val vehicles = it.docks.filter { it.vehicle != null }
                .map { it.vehicle!!.vehicle }
                .map {
                    val status = vehicleService.addStatus(it).status == VehicleStatus.AVAILABLE
                    val sensorReport = sensorReportService.getPendingByVehicle(it)
                    val customerReport = customeReportService.getByVehicle(it)
                    DAOVehicleSmallWithStatus(
                        it.id,
                        it.vehicleType.name,
                        status,
                        sensorReport,
                        customerReport
                    )
                }
            val dockCount = it.docks.size
            val docks = it.docks.map {
                DAODockWithIdVehicle(
                    it.number,
                    it.vehicle?.vehicle?.id
                )
            }
            DAOParkWithVehicles(
                it.id,
                it.address,
                dockCount,
                vehicles,
                docks
            )
        }.toList().let { ResponseEntity.ok(it) }
    }

    /**
     * Adds a new vehicle of a specified type to a park and assigns it to a free dock.
     *
     * @param parkId the ID of the park where the vehicle will be added.
     * @param type the vehicle type ID to be added.
     * @throws DockNotFoundException if there is no available dock in the specified park.
     */

    @PostMapping("/{parkId}/vehicle")
    @PreAuthorize("hasAnyRole('MANAGER')")
    @ExpectedErrors(DockNotFoundException::class)
    private fun addVehicle(@PathVariable("parkId") parkId: Int, @RequestParam("vehicleType") type: Int) {
        val freedock = dockService.getFreeDockByParkId(parkId)
        if (freedock === null) {
            throw DockNotFoundException("There isn't an available dock at park number $parkId")
        } else {
            val vehicle = vehicleService.addVehicle(type)
            val sensorTypes = sensorTypeService.findAll()
            sensorTypes.forEach { sensorService.addSensor(it, vehicle) }
            vehicleDockService.addVehicleDock(vehicle, freedock)
        }
    }

    /**
     * Returns a list of all parks with their id and address.
     *
     * @return a list of [DAOParkWithIdAndName] objects, or an empty list if no parks are found
     */
    @GetMapping("")
    @PreAuthorize("hasAnyRole('MANAGER')")
    private fun getParks(): ResponseEntity<List<DAOParkWithIdAndName>> {
        return parkService.findAll().map { DAOParkWithIdAndName(it.id, it.address) }.toList()
            .let { ResponseEntity.ok(it) }
    }

    /**
     * Moves a list of vehicles to a specified park.
     *
     * @param vehicleIds the IDs of the vehicles to be moved.
     * @param parkId the ID of the park where the vehicles will be moved.
     * @throws NotEnoughAvailableDocksException if there aren't enough available docks in the specified park.
     */
    @PostMapping("/vehicles/move")
    @PreAuthorize("hasAnyRole('MANAGER')")
    @ExpectedErrors(NotEnoughAvailableDocksException::class)
    private fun getVehicles(
        @RequestParam("vehicleIds", required = true) vehicleIds: List<Int>,
        @RequestParam("parkId", required = true) parkId: Int
    ) {
        val vehicles = vehicleIds.map { id -> vehicleService.findById(id) }
        vehicles.forEach { vehicle ->
            vehicle?.let {
                val dock = dockService.getFreeDockByParkId(parkId)
                dock?.let {
                    vehicleDockService.updateVehicleDock(vehicle, it)
                }
            }
        }
    }

    /**
     * Returns a list of all parks with their id, address, latitude, longitude and counts of each vehicle type.
     *
     * @return a list of [DAOParkWithVehicleTypeInfo] objects, or an empty list if no parks are found
     */
    @GetMapping("/info")
    @PreAuthorize("hasAnyRole('MANAGER')")
    private fun getParksInfo(): ResponseEntity<List<DAOParkWithVehicleTypeInfo>> {

        val parks = parkService.findAll()
        return parks.map { it ->
            val bikeCount = vehicleTypeService.numberOfVehicleByParkAndId(it, 1)
            val eBikeCount = vehicleTypeService.numberOfVehicleByParkAndId(it, 2)
            val scooterCount = vehicleTypeService.numberOfVehicleByParkAndId(it, 3)
            DAOParkWithVehicleTypeInfo(
                it.id,
                it.address,
                it.latitude,
                it.longitude,
                bikeCount,
                eBikeCount,
                scooterCount

            )
        }.toList().let { ResponseEntity.ok(it) }

    }
    @GetMapping("/{parkId}/report")
    @PreAuthorize("hasAnyRole('MANAGER')")
    private fun getReportAI(@PathVariable("parkId") parkId: Int, @RequestHeader("Authorization") token: String): ResponseEntity<DAOParkReportAI> {
        val park = parkService.findById(parkId)
        return park?.let {
            val idVehicles = it.docks.mapNotNull { dock -> dock.vehicle?.vehicle?.id }
            val httpClient = HttpClient.create()
                .protocol(HttpProtocol.HTTP11)
            val webClient = WebClient.builder()
                .clientConnector(ReactorClientHttpConnector(httpClient))
                .build()
            webClient.post()
                .uri { uriBuilder ->
                    uriBuilder
                        .scheme("http")
                        .host("192.168.0.10")
                        .port(8000)
                        .path("/maintenance/plan")
                        .queryParam("token", token)
                        .build()
                }
                .header("Authorization", token)
                .bodyValue(mapOf("id_vehicles" to idVehicles))
                .retrieve()
                .bodyToMono(DAOParkReportAI::class.java) // Changed here
                .map { ResponseEntity.ok(it) }
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build())
                .toFuture().get()
        } ?: ResponseEntity.notFound().build()
    }
}
