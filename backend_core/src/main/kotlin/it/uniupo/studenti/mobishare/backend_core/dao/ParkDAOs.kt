package it.uniupo.studenti.mobishare.backend_core.dao

data class DAOParkWithVehicles(
    val id: Int,
    val name: String,
    val dockCount: Int,
    val vehicles: List<DAOVehicleSmallWithStatus>,
    val docks: List<DAODockWithIdVehicle>
)

data class DAOParkWithIdAndName(
    val id: Int,
    val name: String
)
data class DAOParkWithVehicleTypeInfo(
    val id: Int,
    val name: String,
    val latitude: Float,
    val longitude: Float,
    val bikeCount: Int,
    val eBikeCount: Int,
    val scooterCount: Int
)
data class DAOParkReportAI(
    val state: Int,
    val summary: String,
    val priorities: List<Int>
)