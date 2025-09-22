package it.uniupo.studenti.mobishare.backend_core.dao

import it.uniupo.studenti.mobishare.backend_core.entity.Maintenance
import it.uniupo.studenti.mobishare.backend_core.entity.Vehicle
import java.time.LocalDate

data class DAOActiveMaintenance(
    val idMaintenance: Int,
    val vehicle: DAOVehicleOnly,
    val description: String,
    val start: LocalDate,
) {
    constructor(maintenance: Maintenance) : this(
        idMaintenance = maintenance.id,
        vehicle = DAOVehicleOnly(maintenance.vehicle),
        description = maintenance.description,
        start = maintenance.start
    )
}