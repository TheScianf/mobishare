package it.uniupo.studenti.mobishare.backend_core.dao

import it.uniupo.studenti.mobishare.backend_core.entity.Vehicle
import java.time.LocalDate

data class DAOVehicleOnly(
    val id: Int,
    val immissionDate: LocalDate,
    val dismissionDate: LocalDate?,
    val vehicleType: String
) {
    constructor(vehicle: Vehicle) : this(
        vehicle.id,
        vehicle.immissionDate,
        vehicle.dismissionDate,
        vehicle.vehicleType.name
    )
}
 data class DAOVehicleSmallWithStatus(
    val id: Int,
    val vehicleType: String,
    val status: Boolean,
    val sensorReports: List<DAOSensorReportOnly>,
    val customerReports: List<DAOCustomerReportOnly>
)

data class DAOVehicleWithStatus(
    val id: Int,
    val immissionDate: LocalDate,
    val dismissionDate: LocalDate?,
    val vehicleType: String,
    val status: VehicleStatus
) {
    constructor(vehicle: Vehicle, status: VehicleStatus) : this(
        vehicle.id,
        vehicle.immissionDate,
        vehicle.dismissionDate,
        vehicle.vehicleType.name,
        status
    )
}
data class DAOVehicleWithPrice(
    val id: Int,
    val vehicleType: String,
    val constantPrice: Float,
    val minutePrice: Float
)
enum class VehicleStatus(val isAvailable: Boolean) {
    AVAILABLE(true),
    NOT_AVAILABLE(false),
    UNDER_MAINTENANCE(false),
    ABANDONED(false),
    DISABLED(false);

    companion object {
        fun fromString(s: String): VehicleStatus {
            return when (s) {
                "NOT_AVAILABLE" -> NOT_AVAILABLE
                "UNDER_MAINTENANCE" -> UNDER_MAINTENANCE
                "ABANDONED" -> ABANDONED
                "DISABLED" -> DISABLED
                else -> AVAILABLE
            }
        }
    }
}