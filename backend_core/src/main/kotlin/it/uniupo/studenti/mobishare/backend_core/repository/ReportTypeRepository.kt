package it.uniupo.studenti.mobishare.backend_core.repository

import it.uniupo.studenti.mobishare.backend_core.entity.ReportType
import org.springframework.data.jpa.repository.JpaRepository

interface ReportTypeRepository: JpaRepository<ReportType, Long> {
    fun findById(id: Int): ReportType?
}