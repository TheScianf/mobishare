package it.uniupo.studenti.mobishare.backend_core.service

import it.uniupo.studenti.mobishare.backend_core.dao.DAOCustomerReportOnly
import it.uniupo.studenti.mobishare.backend_core.entity.Customer
import it.uniupo.studenti.mobishare.backend_core.entity.CustomerReport
import it.uniupo.studenti.mobishare.backend_core.entity.CustomerReportId
import it.uniupo.studenti.mobishare.backend_core.entity.ReportType
import it.uniupo.studenti.mobishare.backend_core.entity.Vehicle
import it.uniupo.studenti.mobishare.backend_core.repository.CustomerReportRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.Date

interface CustomerReportService {
    fun getByVehicle(vehicle: Vehicle): List<DAOCustomerReportOnly>
    fun deleteAllByVehicle(vehicle: Vehicle): Boolean
    fun newCustomerReport(customer: Customer, vehicle: Vehicle, reportType: ReportType, text: String)
}

@Service
class CustomerReportServiceImpl: CustomerReportService {

    @Autowired
    private lateinit var customerReportRepository: CustomerReportRepository

    /**
     * Retrieves a list of customer reports for a given vehicle that are in a "PENDING" state.
     *
     * @param vehicle The vehicle for which to retrieve customer reports.
     * @return A list of DAOCustomerReportOnly objects representing the pending reports.
     */

    override fun getByVehicle(vehicle: Vehicle): List<DAOCustomerReportOnly> {
        return customerReportRepository.findByVehicleAndState(vehicle, "PENDING")
            .map { DAOCustomerReportOnly(
                Date(it.id.time!!.toEpochMilli()),
                it.description,
                customer = it.id.idCustomer
            ) }
    }

    /**
     * Deletes all customer reports associated with a given vehicle.
     *
     * @param vehicle The vehicle whose associated customer reports are to be deleted.
     * @return True if one or more customer reports were successfully deleted, false otherwise.
     */

    override fun deleteAllByVehicle(vehicle: Vehicle): Boolean {
        val deletedCount = customerReportRepository.deleteByVehicle(vehicle)
        return deletedCount > 0
    }

    /**
     * Submits a new customer report for a vehicle.
     *
     * @param customer The customer submitting the report.
     * @param vehicle The vehicle for which the report is being submitted.
     * @param reportType The type of report being submitted.
     * @param text The text of the report.
     *
     * This method creates a new customer report and saves it to the database.
     * The report is marked as "PENDING" and the current time is used as the timestamp.
     */
    override fun newCustomerReport(
        customer: Customer,
        vehicle: Vehicle,
        reportType: ReportType,
        text: String
    ) {
        val customerReportId = CustomerReportId().apply {
            time = Instant.now()
            idCustomer = customer.username // Assuming customer.id is String
        }
        val customerReport = CustomerReport().apply {
            id = customerReportId
            this.vehicle = vehicle
            this.reportType = reportType
            description = text
            state = "PENDING"
        }

        customerReportRepository.save(customerReport)
    }

}