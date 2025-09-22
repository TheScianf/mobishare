package it.uniupo.studenti.mobishare.backend_core.dao

import java.util.Date

data class DAOSensorReportOnly(
    val time: Date,
    val value: Float,
)