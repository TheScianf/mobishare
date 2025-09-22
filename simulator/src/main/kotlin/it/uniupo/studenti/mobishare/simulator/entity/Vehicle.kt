package it.uniupo.studenti.mobishare.backend_core.entity

import it.uniupo.studenti.mobishare.simulator.entity.Sensor

class Vehicle(
    val id: Int,
    val sensors: MutableSet<Sensor>,
    val type: String
)