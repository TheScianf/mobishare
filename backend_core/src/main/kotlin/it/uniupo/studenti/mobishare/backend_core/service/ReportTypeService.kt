package it.uniupo.studenti.mobishare.backend_core.service

import it.uniupo.studenti.mobishare.backend_core.entity.ReportType
import it.uniupo.studenti.mobishare.backend_core.repository.ReportTypeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

interface ReportTypeService{
    fun findById(id: Int) : ReportType?


}
@Service
class ReportTypeServiceImpl: ReportTypeService {
    @Autowired
    private lateinit var reportTypeRepository: ReportTypeRepository
    /**
     * Finds a report type by its ID.
     *
     * @param id The ID of the report type to be found.
     * @return The report type with the given ID, or null if no such report type exists.
     */

    override fun findById(id: Int): ReportType? {
        return reportTypeRepository.findById(id)
    }
}