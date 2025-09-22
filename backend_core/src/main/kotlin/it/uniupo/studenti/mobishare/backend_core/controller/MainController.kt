package it.uniupo.studenti.mobishare.backend_core.controller

import it.uniupo.studenti.mobishare.backend_core.repository.VehicleTypeRepository
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
class MainController(
    val vehicleTypeRepository: VehicleTypeRepository,
    val mqttClient: MqttClient
) {

    @GetMapping("")
    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER')")
    fun index(): ResponseEntity<String> {
        val topic = "prova/"
        val qos = 2
        val msg = "hello from spring"
        val mqttMessage = MqttMessage(msg.toByteArray())
        mqttMessage.qos = qos
        mqttClient.publish(topic, mqttMessage)
        return ResponseEntity.ok("done")
    }
}