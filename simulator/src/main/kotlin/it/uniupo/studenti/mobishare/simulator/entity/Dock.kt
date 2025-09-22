package it.uniupo.studenti.mobishare.simulator.entity

import it.uniupo.studenti.mobishare.backend_core.entity.Vehicle
import it.uniupo.studenti.mobishare.simulator.SimulatorContext
import java.sql.PreparedStatement
import kotlin.concurrent.atomics.AtomicReference
import kotlin.concurrent.atomics.ExperimentalAtomicApi


@OptIn(ExperimentalAtomicApi::class)
class Dock(
    val id: Int,
    val number: Int,
    val park: Int,
    attuators: Map<String, String>,
    var vehicle: Vehicle?
) {
    val attuators: MutableMap<String, AtomicReference<String>> = mutableMapOf()

    init {
        val ps = SimulatorContext.dbConnection.prepareStatement("SELECT type, value FROM ACTUATOR WHERE idDock = ?");
        ps.setInt(1, id)
        val rs = ps.executeQuery()
        while (rs.next()) {
            val value = rs.getString("value")
            val type = rs.getString("type")
            this.attuators[type] = AtomicReference(value)
            println("Dock $id: Attuator $type initialized with value $value")
        }

    }
}
