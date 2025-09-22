package it.uniupo.studenti.mobishare.backend_core.controller

import it.uniupo.studenti.mobishare.backend_core.annotation.ExpectedErrors
import it.uniupo.studenti.mobishare.backend_core.dao.DAOVehicleOnly
import it.uniupo.studenti.mobishare.backend_core.dao.DAOActiveMaintenance
import it.uniupo.studenti.mobishare.backend_core.dao.DAOVehicleWithPrice
import it.uniupo.studenti.mobishare.backend_core.exception.DockNotFoundException
import it.uniupo.studenti.mobishare.backend_core.exception.VehicleNotAvailableException
import it.uniupo.studenti.mobishare.backend_core.exception.VehicleNotExistException
import it.uniupo.studenti.mobishare.backend_core.service.CustomerReportService
import it.uniupo.studenti.mobishare.backend_core.service.DockService
import it.uniupo.studenti.mobishare.backend_core.service.VehicleService
import it.uniupo.studenti.mobishare.backend_core.service.MaintenanceService
import it.uniupo.studenti.mobishare.backend_core.service.SensorReportService
import it.uniupo.studenti.mobishare.backend_core.service.SensorService
import it.uniupo.studenti.mobishare.backend_core.service.VehicleDockService
import org.springframework.http.ResponseEntity
import org.springframework.http.client.reactive.ReactorClientHttpConnector

import org.springframework.security.access.prepost.PreAuthorize
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
@RequestMapping("/vehicles")
class VehicleController(
    val vehicleService: VehicleService,
    val maintenanceService: MaintenanceService,
    val vehicleDockService: VehicleDockService,
    val dockService: DockService,
    val customerReportService: CustomerReportService,
    val sensorService: SensorService,
    val sensorReportService: SensorReportService
) {

/**
 * Retrieves a list of all vehicles.
 *
 * @return a list of [DAOVehicleOnly] objects representing all vehicles available
 *         in the system.
 */
    @GetMapping("")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    private fun getAll(): List<DAOVehicleOnly> {
        return vehicleService.findAll().map { DAOVehicleOnly(it) }
    }


    /**
     * Retrieves a list of all active maintenances.
     *
     * @return a list of [DAOActiveMaintenance] objects representing all active
     *         maintenances in the system.
     */
    @GetMapping("/maintenance")
    @PreAuthorize("hasAnyRole('MANAGER')")
    private fun getActiveMaintenances(): List<DAOActiveMaintenance> {
        return maintenanceService.getActiveMaintenances()
    }

    /**
     * Starts a maintenance for a vehicle.
     *
     * @param vehicleId the id of the vehicle to send to maintenance
     * @param description the description of the maintenance, if any
     */
    @PostMapping("/maintenance")
    @PreAuthorize("hasAnyRole('MANAGER')")
    private fun sendVehicleToMaintenance(@RequestParam("vehicleId", required=true)idVehicle:Int, @RequestParam("description", required=false)description:String?){
        val vehicle = vehicleService.findById(idVehicle)

        vehicle?.let {
            vehicleDockService.removeVehicleDock(idVehicle)
            maintenanceService.newMaintenance(vehicle, description) }
    }

    /**
     * Ends a maintenance for a vehicle.
     *
     * @param vehicleId the id of the vehicle to end maintenance
     * @param parkId the id of the park where to put the vehicle
     * @throws DockNotFoundException if there aren't enough free docks in the specified park
     */
    @PostMapping("/maintenance/end")
    @PreAuthorize("hasAnyRole('MANAGER')")
    private fun endingVehicleMaintenance(
        @RequestParam("vehicleId", required=true)vehicleId:Int,
        @RequestParam("parkId", required=true)parkId:Int,
        @RequestHeader("Authorization") token: String){
        val vehicle = vehicleService.findById(vehicleId)

        vehicle?.let {
            maintenanceService.endVehicleMaintenance(vehicle)
            val freedock = dockService.getFreeDockByParkId(parkId)
            if (freedock === null){
                throw DockNotFoundException("There isn't an available dock at park number $parkId")
            }
            else{
                vehicleDockService.addVehicleDock(vehicle, freedock)
                vehicleService.enable(vehicle)
                customerReportService.deleteAllByVehicle(vehicle)
                vehicle.sensors.forEach { sensorReportService.deleteAllBySensor(it) }
                val sensorList = sensorService.findAllByVehicle(vehicle)
                sensorList?.let { sensorList.forEach {sensor -> sensorReportService.deleteAllBySensor(sensor)}}
               // TODO Call python backend and delete formatted feedback of the vehicle
                val httpClient = HttpClient.create()
                    .protocol(HttpProtocol.HTTP11)
                val webClient = WebClient.builder()
                    .clientConnector(ReactorClientHttpConnector(httpClient))
                    .build()
                webClient.delete().uri { uriBuilder ->
                        uriBuilder
                            .scheme("http")
                            .host("localhost")
                            .port(8000)
                            .path("feedback")
                            .queryParam("id_vehicle", vehicleId)
                            .queryParam("token", token)
                            .build()
                    }
                    .retrieve()
                    .toBodilessEntity()
                    .subscribe(
                        { println("Successfully sent to Python backend") },
                        { error -> println("Error sending to Python backend: ${error.message}") }
                    )
           }
        }
    }
        /**
        * Retrieves information about a vehicle, including its availability and price.
        *
        * @param vehicleId the id of the vehicle
        * @return a [DAOVehicleWithPrice] object with information about the vehicle, or an empty object if the vehicle doesn't exist
        * @throws VehicleNotExistException if the given vehicle id does not exist
        * @throws VehicleNotAvailableException if the given vehicle is not available
        */
    @GetMapping("/{vehicleId}/available")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @ExpectedErrors(VehicleNotExistException::class, VehicleNotAvailableException::class)
    private fun isVehicleAvailable(@PathVariable("vehicleId", required=true)vehicleId:Int): ResponseEntity<DAOVehicleWithPrice> {
        val vehicle = vehicleService.findById(vehicleId)
        vehicle?.let {
            if (vehicleService.isAvailable(it)){
                return DAOVehicleWithPrice(id = vehicleId, vehicleType = it.vehicleType.name, it.vehicleType.constantPrice, it.vehicleType.minutePrice).let { ResponseEntity.ok(it) }
            }
            throw VehicleNotAvailableException(vehicleId)
        }
        throw VehicleNotExistException(vehicleId)
    }

}