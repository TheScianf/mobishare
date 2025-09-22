package it.uniupo.studenti.mobishare.backend_core.repository

import it.uniupo.studenti.mobishare.backend_core.entity.Park
import it.uniupo.studenti.mobishare.backend_core.entity.VehicleType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface VehicleTypeRepository: JpaRepository<VehicleType, Int>{
    @Query("""
        SELECT COUNT(vd.vehicle) 
        FROM VehicleDock vd 
        WHERE vd.dock.park = :park AND vd.vehicle.vehicleType.id = :vehicleTypeId
    """)
    fun countVehiclesByParkAndVehicleType(park: Park, vehicleTypeId: Int): Int

}