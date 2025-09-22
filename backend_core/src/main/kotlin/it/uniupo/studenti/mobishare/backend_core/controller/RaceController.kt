package it.uniupo.studenti.mobishare.backend_core.controller

import it.uniupo.studenti.mobishare.backend_core.annotation.ExpectedErrors
import it.uniupo.studenti.mobishare.backend_core.exception.CustomerIsSuspendedException
import it.uniupo.studenti.mobishare.backend_core.exception.VehicleDockNotExistException
import it.uniupo.studenti.mobishare.backend_core.exception.VehicleNotExistException
import it.uniupo.studenti.mobishare.backend_core.service.CouponService
import it.uniupo.studenti.mobishare.backend_core.service.CustomerService
import it.uniupo.studenti.mobishare.backend_core.service.MqttService
import it.uniupo.studenti.mobishare.backend_core.service.RaceService
import it.uniupo.studenti.mobishare.backend_core.service.SuspensionService
import it.uniupo.studenti.mobishare.backend_core.service.TransactionService
import it.uniupo.studenti.mobishare.backend_core.service.VehicleDockService
import it.uniupo.studenti.mobishare.backend_core.service.VehicleService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Duration
import java.time.Instant

@RestController
@RequestMapping("/race")
class RaceController(
    val raceService: RaceService,
    val vehicleService: VehicleService,
    val customerService: CustomerService,
    val mqttService: MqttService,
    val vehicleDockService: VehicleDockService,
    val couponService: CouponService,
    val transactionService: TransactionService,
    val suspensionService: SuspensionService
) {
    /**
     * Starts a new race.
     *
     * @param vehicleId the id of the vehicle that is being used for the race
     * @param username the username of the customer that is starting the race
     *
     * @return the start time of the race
     *
     * @throws VehicleNotExistException if the given vehicle does not exist
     */
    @PostMapping("")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @ExpectedErrors(VehicleNotExistException::class)
    private fun newRace(@RequestParam("vehicleId", required=true)vehicleId:Int, @RequestParam("username", required = true)username: String): Instant{
        val vehicle = vehicleService.findById(vehicleId)
        vehicle?.let {
            val customer = customerService.findCustomerByUsername(username)
            customer?.let {
                // TODO maybe remove from dock
               val parkId = vehicle.dock?.dock?.park?.id!!
               val dockId =  vehicle.dock!!.dock.number
               mqttService.setDockLock(parkId, dockId, "open")
               mqttService.setDockLight(parkId, dockId,"off" )
               vehicleDockService.removeVehicleDock(vehicleId)
               return raceService.startRace(vehicle, it)
            }
        }
       throw VehicleNotExistException(vehicleId)
    }
    /**
     * Ends a race.
     *
     * @param vehicleId the id of the vehicle that was used for the race
     * @param username the username of the customer that started the race
     * @param start the start time of the race
     *
     * @throws CustomerIsSuspendedException if the customer does not have enough credit
     * @throws VehicleDockNotExistException if the given vehicle is not docked
     */
    @PostMapping("/end")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @ExpectedErrors(CustomerIsSuspendedException::class, VehicleDockNotExistException::class)
    private fun endRace(
        @RequestParam("vehicleId", required=true)vehicleId:Int,
        @RequestParam("username", required = true)username: String,
        @RequestParam("start", required = true)start: Instant){
        val vehicle = vehicleService.findById(vehicleId)
        vehicle?.let {
            val vehicleDock =  vehicleDockService.getVehicleDockByVehicle(vehicle)
            vehicleDock?.let {
                val customer = customerService.findCustomerByUsername(username)
                customer?.let {
                    val minuteDuration = Duration.between(start, Instant.now()).toMinutes().toInt()
                    raceService.endRace(vehicle, customer, start, minuteDuration)
                    var bill: Float = vehicle.vehicleType.constantPrice + (vehicle.vehicleType.minutePrice * minuteDuration)
                    val discount = couponService.useCoupons(username, bill) / 100f
                    bill -= discount
                    transactionService.newTransaction(customer, -bill)

                    if (vehicle.vehicleType.id == 1) {
                        val greenPoints = minuteDuration * 2
                       couponService.addGreenPoints(customer, greenPoints)
                    }

                    if (transactionService.getCreditByCustomer(username) < 0){
                        suspensionService.newSuspension(customer, bill)
                        throw CustomerIsSuspendedException(username)
                    }
                    return
                }
            }
            throw VehicleDockNotExistException(vehicleId)
        }
    }
}
