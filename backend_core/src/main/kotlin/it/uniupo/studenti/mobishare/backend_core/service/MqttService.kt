package it.uniupo.studenti.mobishare.backend_core.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import it.uniupo.studenti.mobishare.backend_core.dao.VehicleStatus
import it.uniupo.studenti.mobishare.backend_core.toJson
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.text.SimpleDateFormat
import java.util.*

interface MqttService {
    fun updateVehicleSensor(
        mqttMsg: String,
    )

    fun vehiclePluggedIn(mqttMsg: String, dockId: Int) // this parameter type will be changed
    fun setDockLock(park: Int, dock: Int, operation: String)
    fun setDockLight(park: Int, dock: Int, operation: String)
}

@Service
class MqttServiceImpl(
    private val sensorService: SensorService,
    private val sensorReportService: SensorReportService,
    private val vehicleDockService: VehicleDockService,
    private val vehicleService: VehicleService,
    private val actuatorService: ActuatorService,
    private val dockService: DockService,
    private val valueRangeService: ValueRangeService
) : MqttService {

    @Autowired
    private lateinit var mqttClient: MqttClient


    override fun updateVehicleSensor(
        mqttMsg: String,
    ) {
        val mapper = ObjectMapper().registerModule(KotlinModule.Builder().build())
        val message = mapper.readTree(mqttMsg)
        val sensorId = message["sensorId"].asInt()
        val vehicleId = message["vehicleId"].asInt()
        val sensor = sensorService.findById(sensorId)
        println("Sensor: $sensor, Vehicle ID: $vehicleId, Message: $mqttMsg")
        sensor?.let {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val time: Date = dateFormat.parse(message["time"].asText())!!
            val vehicle = vehicleService.findById(vehicleId)
            vehicle?.let { vehicle ->
                val range = valueRangeService.findByVehicleTypeAndSensorType(vehicle.vehicleType, it.sensorType)
                range?.let { r ->
                    val value = message["value"].floatValue()
                    val state = if (value > r.min.toFloat() && value < r.max.toFloat()) "S" else "P"
                    if (state == "S") {
                        sensorReportService.setPendingToDeprecatedBySensor(it)
                    }
                    sensorReportService.addSensorReport(time, it, value, state)
                } ?: sensorReportService.addSensorReport(time, it, message["value"].floatValue(), "S")
            }
        }

    }

    override fun vehiclePluggedIn(mqttMsg: String, dockId: Int) {
        val mapper = ObjectMapper().registerModule(KotlinModule.Builder().build())
        val message = mapper.readTree(mqttMsg)
        val vehicleId = message["vehicleId"].asInt()
        val vehicle = vehicleService.findById(vehicleId)
        vehicle?.let {
            val dock = dockService.findById(dockId)
            dock?.let { it1 ->
                vehicleDockService.addVehicleDock(vehicle, it1)
                val actuators = actuatorService.findAllByDock(it1)
                actuators?.forEach {
                    if (it.type.type == "Lock")
                        it.value = "close"
                    else
                        it.value = "green"
                    setDockLock(dock.park.id, dock.number, "close")
                    val status = vehicleService.getStatus(vehicle)
                    val color = if (status == VehicleStatus.AVAILABLE) "green" else "red"
//                    println("COLOR: " + color)
                    setDockLight(dock.park.id, dock.number, color)
                    actuatorService.save(it)
                }
            }
        }
    }

    override fun setDockLock(park: Int, dock: Int, operation: String) {
        val msg = toJson(
            object {
                val actuator = "lock"
                val operation = operation
            }
        )
        val mqttMsg = MqttMessage(msg.encodeToByteArray())
        mqttMsg.qos = 2
        mqttClient.publish("""parks/$park/docks/$dock/out""", mqttMsg)
    }

    override fun setDockLight(park: Int, dock: Int, operation: String) {
        val msg = toJson(
            object {
                val actuator = "light"
                val operation = operation
            }
        )

        val mqttMsg = MqttMessage(msg.encodeToByteArray())
        mqttMsg.qos = 2
        mqttClient.publish("""parks/$park/docks/$dock/out""", mqttMsg)
    }
}

