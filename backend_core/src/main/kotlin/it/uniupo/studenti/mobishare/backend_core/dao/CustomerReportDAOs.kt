package it.uniupo.studenti.mobishare.backend_core.dao

import java.util.Date

data class DAOCustomerReportOnly(
    val time: Date,
    val description: String,
    val customer: String?,
    )