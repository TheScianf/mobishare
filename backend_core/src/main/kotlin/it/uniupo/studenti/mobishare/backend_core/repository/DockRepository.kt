package it.uniupo.studenti.mobishare.backend_core.repository

import it.uniupo.studenti.mobishare.backend_core.entity.Dock
import it.uniupo.studenti.mobishare.backend_core.entity.Park
import org.springframework.data.jpa.repository.JpaRepository

interface DockRepository: JpaRepository<Dock, Int> {
    fun getDockByNumberAndPark_Id(Park: Int, Number: Int):  Dock?
    fun getDockById(id: Int): Dock?
    fun findAllByPark(park: Park): List<Dock>
    fun findAllByParkIdAndNumber(parkId: Int, number: Int): Dock?
}
