package it.uniupo.studenti.mobishare.backend_core.service

import it.uniupo.studenti.mobishare.backend_core.dao.DAORaceOnly
import it.uniupo.studenti.mobishare.backend_core.entity.Customer
import it.uniupo.studenti.mobishare.backend_core.entity.Race
import it.uniupo.studenti.mobishare.backend_core.entity.RaceId
import it.uniupo.studenti.mobishare.backend_core.entity.Vehicle
import it.uniupo.studenti.mobishare.backend_core.exception.UsernameNotFoundException
import it.uniupo.studenti.mobishare.backend_core.repository.RaceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDate

interface RaceService {
    fun findAllRacesByCustomer(username: String): List<DAORaceOnly>
    fun startRace(vehicle: Vehicle, customer: Customer): Instant
    fun endRace(vehicle: Vehicle, customer: Customer, start: Instant,  minuteDuration:Int)
}

@Service
class RaceServiceImpl : RaceService {
    @Autowired
    private lateinit var customerService: CustomerService

    @Autowired
    private lateinit var raceRepository: RaceRepository

    /**
     * Finds all races for a given customer.
     *
     * @param username the username of the customer
     * @return a list of DAORaceOnly, empty list if no race exists
     * @throws UsernameNotFoundException if the customer does not exist
     */
    override fun findAllRacesByCustomer(username: String): List<DAORaceOnly> {
        val customer: Customer? = customerService.findCustomerByUsername(username)
        if (customer == null) {
            throw UsernameNotFoundException(username)
        } else {
            val races: List<Race> = customer.let { raceRepository.findAllByCustomer(it) } ?: listOf()
            return races.map { race -> DAORaceOnly(race) }
        }
    }

    /**
     * Starts a race for a given vehicle and customer.
     *
     * @param vehicle the vehicle being used for the race
     * @param customer the customer starting the race
     * @return the start time of the race as an Instant
     */

    override fun startRace(
        vehicle: Vehicle,
        customer: Customer
    ): Instant{
        val instant = Instant.now()
        val raceId = RaceId().apply {
            start = instant
            idCustomer = customer.username
            idVehicle = vehicle.id
        }
        val race = Race().apply {
            id = raceId
            this.customer = customer
            minDuration = 0
        }
        raceRepository.save(race)
        return instant
    }

    /**
     * Ends a race for a given vehicle and customer.
     *
     * @param vehicle the vehicle that was used for the race
     * @param customer the customer that started the race
     * @param start the exact start time of the race
     * @param minuteDuration the total duration of the race in minutes
     *
     * @throws NullPointerException if the race does not exist
     */
    override fun endRace(
        vehicle: Vehicle,
        customer: Customer,
        start: Instant,
        minuteDuration: Int
    ) {
        // Create the composite key with the exact start time
        val raceId = RaceId().apply {
            this.start = start
            this.idCustomer = customer.username
            this.idVehicle = vehicle.id
        }

        // Find the specific race record
        val race = raceRepository.findById(raceId).orElse(null)
        race?.let {
            it.minDuration = minuteDuration
            raceRepository.save(it)
        }
    }
}
