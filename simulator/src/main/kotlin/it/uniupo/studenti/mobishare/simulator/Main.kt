package it.uniupo.studenti.mobishare.simulator

import it.uniupo.studenti.mobishare.backend_core.entity.Vehicle
import it.uniupo.studenti.mobishare.simulator.entity.Dock
import it.uniupo.studenti.mobishare.simulator.entity.Sensor
import it.uniupo.studenti.mobishare.simulator.ui.MainWindow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.eclipse.paho.client.mqttv3.IMqttMessageListener
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.sql.Connection
import javax.swing.JOptionPane
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.system.exitProcess

fun main() {
    if (!setupDb()) {
        println("Database connection failed.")
        return
    }

    if (!setupSimulator()) {
        println("Simulator setup failed.")
        return
    }

    if (!setupMqtt()) {
        println("MQTT setup failed.")
        return
    }

    setupMqttListening()

    MainWindow().isVisible = true
}

object SimulatorContext {
    lateinit var docks: List<Dock>
    lateinit var vehicles: List<Vehicle>
    lateinit var mqttConnection: MqttClient
    lateinit var dbConnection: Connection
}

private lateinit var dbConnection: Connection

fun setupMqtt(): Boolean {
    try {
        val options = MqttConnectOptions()
        SimulatorContext.mqttConnection = MqttClient("tcp://localhost:1883", "mobishare_simulator")
        SimulatorContext.mqttConnection.connect()
    } catch (e: MqttException) {
        return false
    }
    return true
}

fun setupDb(): Boolean {
    dbConnection = java.sql.DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/mobishare",
        "core_backend",
        "core_backend"
    )
    SimulatorContext.dbConnection = dbConnection
    return dbConnection.isValid(5)
}

fun setupSimulator(): Boolean {
    val queryActuators = "SELECT idDock, type FROM ACTUATOR"
    val querySensors = "SELECT id, idSensorType, idVehicle FROM SENSOR"
    val querySensorTypes = "SELECT id, name FROM SENSOR_TYPE"
    val queryDocks =
        "SELECT id, number, idPark, idVehicle FROM DOCK LEFT JOIN VEHICLE_DOCK ON DOCK.id = VEHICLE_DOCK.idDock"
    val queryVehicles = "SELECT VEHICLE.id as id, TYPE_VEHICLE.name as type FROM VEHICLE " +
            "INNER JOIN TYPE_VEHICLE ON VEHICLE.idVehicleType = TYPE_VEHICLE.id"

    try {
        val sensorTypes: MutableMap<Int, String> = mutableMapOf()
        val statement = dbConnection.prepareStatement(querySensorTypes)
        val resultSet = statement.executeQuery()
        while (resultSet.next()) {
            sensorTypes.put(
                resultSet.getInt("id"),
                resultSet.getString("name")
            )
            println("Sensor Type: ${resultSet.getInt("id")} - ${resultSet.getString("name")}")
        }
        resultSet.close()
        statement.close()

        val sensors: MutableMap<Int, MutableList<Sensor>> = mutableMapOf() //key is the vehicleId
        val sensorStatement = dbConnection.prepareStatement(querySensors)
        val sensorResultSet = sensorStatement.executeQuery()
        while (sensorResultSet.next()) {
            val idVehicle = sensorResultSet.getInt("idVehicle")
            if (!sensors.keys.contains(idVehicle)) {
                sensors[idVehicle] = mutableListOf()
            }
            val sensor = Sensor(
                sensorResultSet.getInt("id"),
                sensorTypes[sensorResultSet.getInt("idSensorType")] ?: "Unknown"
            )
            sensors[idVehicle]?.add(sensor)
            println("Sensor: ${sensor.id} - (${sensor.type})")
        }
        resultSet.close()
        statement.close()

        val vehicles: MutableList<Vehicle> = mutableListOf()
        val vehicleStatement = dbConnection.prepareStatement(queryVehicles)
        val vehicleResultSet = vehicleStatement.executeQuery()
        while (vehicleResultSet.next()) {
            val vehicle = Vehicle(
                vehicleResultSet.getInt("id"),
                sensors[vehicleResultSet.getInt("id")]?.toMutableSet() ?: mutableSetOf(),
                vehicleResultSet.getString("type")
            )
            vehicles.add(vehicle)
            println(vehicle.sensors.size)
            println("Vehicle: ${vehicle.id} - (${vehicle.type})")
        }
        resultSet.close()
        statement.close()

        val actuators = mutableMapOf<Int, String>()
        val actuatorStatement = dbConnection.prepareStatement(queryActuators)
        val actuatorResultSet = actuatorStatement.executeQuery()
        while (actuatorResultSet.next()) {
            actuators[actuatorResultSet.getInt("idDock")] = actuatorResultSet.getString("type")
        }
        resultSet.close()
        statement.close()

        val docks = mutableListOf<Dock>()
        val dockStatement = dbConnection.prepareStatement(queryDocks)
        val dockResultSet = dockStatement.executeQuery()
        while (dockResultSet.next()) {
            val idDock = dockResultSet.getInt("id")
            val vehicleId: Int? = dockResultSet.getInt("idVehicle")
            val vehicle = vehicles.find { it.id == vehicleId }
            val dock = Dock(
                idDock,
                dockResultSet.getInt("number"),
                dockResultSet.getInt("idPark"),
                actuators.filter { it.key == idDock }.map { Pair(it.value, "") }.toMap(),
                vehicle
            )
            docks.add(dock)
            println("Dock: ${dock.id} - Number: ${dock.number}, Park: ${dock.park}, Vehicle: ${vehicle?.id ?: "None"}")
        }
        resultSet.close()
        statement.close()

        SimulatorContext.docks = docks
        SimulatorContext.vehicles = vehicles
    } catch (e: Exception) {
        e.printStackTrace()
        return false
    }
    return true
}

@Serializable private data class ActuatorMessage(val actuator: String, val operation: String)

@OptIn(ExperimentalAtomicApi::class)
fun setupMqttListening() {
    try {
        val mqttClient = SimulatorContext.mqttConnection

        mqttClient.subscribe(
            "parks/+/docks/+/out",
            IMqttMessageListener { topic, message ->
                try {
                    println(topic)
                    println(message.payload.decodeToString())
                    val dockPark = topic.split("/")[1].toInt()
                    val dockNumber = topic.split("/")[3].toInt()
                    val dock = SimulatorContext.docks.find { it.number == dockNumber && it.park == dockPark }
                    if (dock != null) {
                        val msg = message.payload.decodeToString()
                        val actMsg = Json.decodeFromString<ActuatorMessage>(msg)
                        if(actMsg.actuator == "lock" && actMsg.operation == "open") {
                            dock.vehicle = null
                        }
                        dock.attuators[actMsg.actuator.capitalize()]?.store(actMsg.operation)
                        println("Dock ${dock.id} Lock set to ${actMsg.operation}")
                    } else {
                        println("Dock with ID $dockNumber not found.")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        )
    } catch (e: Exception) {
        e.printStackTrace()
        exitProcess(1)
    }
}
