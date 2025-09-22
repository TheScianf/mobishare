package it.uniupo.studenti.mobishare.backend_core.dao;

import it.uniupo.studenti.mobishare.backend_core.entity.Transaction
import java.time.Instant

data class DAOTransactionOnly(
    val id: Int,
    val value: Float,
    val time: Instant
) {
    constructor(transaction: Transaction) : this(
        transaction.id,
        transaction.value,
        transaction.time
    )
}
