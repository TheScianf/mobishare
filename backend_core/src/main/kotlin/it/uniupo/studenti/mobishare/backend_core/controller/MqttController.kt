package it.uniupo.studenti.mobishare.backend_core.controller

import it.uniupo.studenti.mobishare.backend_core.annotation.MqttGenericController
import it.uniupo.studenti.mobishare.backend_core.annotation.MqttMapping
import it.uniupo.studenti.mobishare.backend_core.fromJson
import it.uniupo.studenti.mobishare.backend_core.service.DockService
import it.uniupo.studenti.mobishare.backend_core.service.MqttService
import org.eclipse.paho.client.mqttv3.IMqttMessageListener

@it.uniupo.studenti.mobishare.backend_core.annotation.MqttController
class MqttController(
    private val mqttService: MqttService,
    private val dockService: DockService,
) : MqttGenericController {

    @MqttMapping("vehicle/+")
    fun battery() = IMqttMessageListener { topic, message ->
        mqttService.updateVehicleSensor(message.payload.decodeToString())
    }

    @MqttMapping("parks/+/docks/+/in")
    fun dock() = IMqttMessageListener{ topic, message ->
        val substrings = topic.split("/")
        val parkId = substrings[1].toInt()
        val dockNumber = substrings[3].toInt()
        val dockId = dockService.findByParkIdAndNumber(parkId, dockNumber)?.id
            ?: throw IllegalArgumentException("Dock not found for parkId: $parkId and dockId: $dockNumber")
        println("message: ${message.payload.decodeToString()}")
        mqttService.vehiclePluggedIn(message.payload.decodeToString(), dockId)
    }
}
