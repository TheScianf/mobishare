package it.uniupo.studenti.mobishare.backend_core.annotation

import org.eclipse.paho.client.mqttv3.IMqttMessageListener
import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class MqttMapping(
    val topic: String,
    val qos: Int = 2
)
