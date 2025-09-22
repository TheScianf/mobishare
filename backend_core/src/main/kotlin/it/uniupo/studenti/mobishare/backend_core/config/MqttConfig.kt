package it.uniupo.studenti.mobishare.backend_core.config

import it.uniupo.studenti.mobishare.backend_core.annotation.MqttGenericController
import it.uniupo.studenti.mobishare.backend_core.annotation.MqttMapping
import jakarta.annotation.PreDestroy
import org.eclipse.paho.client.mqttv3.IMqttMessageListener
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.beans.factory.getBean
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.functions
import kotlin.reflect.full.isSubclassOf

@Configuration
class MqttConfig {

    @Value("\${mqtt.addr}")
    private lateinit var addr: String

    @Value("\${mqtt.port}")
    private lateinit var port: String

    @Value("\${mqtt.clientID}")
    private lateinit var clientId: String

    private val logger = LoggerFactory.getLogger(MqttConfig::class.java)

    private var mqttClient: MqttClient? = null

    @Bean
    fun getClient(): MqttClient {
        if (mqttClient == null) {
            val client = MqttClient("tcp://$addr:$port", clientId, MemoryPersistence())
            client.connect()
            mqttClient = client
            logger.info("MQTT client connected")
        }

        return mqttClient!!
    }

//    @PreDestroy
//    fun cleanup() {
//        mqttClient?.disconnect()
//        mqttClient?.close()
//        logger.info("Disconnected from MQTT server.")
//    }
}

@Component
class MqttSubscriber {
    @Autowired
    private lateinit var client: MqttClient

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    private val logger: Logger = LoggerFactory.getLogger(MqttSubscriber::class.java)

    @EventListener(ApplicationReadyEvent::class)
    fun onApplicationReadyEvent() {
        applicationContext.beanDefinitionNames
            .map { applicationContext.getType(it)?.kotlin }
            .filterNotNull()
            .filter { it.findAnnotation<it.uniupo.studenti.mobishare.backend_core.annotation.MqttController>() != null }
            .forEach {
                if (!it.isSubclassOf(MqttGenericController::class)) {
                    logger.error("${it.simpleName} MUST implement MqttGenericController, ignored.")
                } else {
                    applicationContext.getBean<MqttGenericController>(it).let { controllerBean ->
                        controllerBean::class
                            .functions
                            .map { Pair(it, it.findAnnotation<MqttMapping>()) }
                            .filter { it.second != null }
                            .forEach { method ->
                                if (method.first.returnType.classifier != IMqttMessageListener::class) {
                                    logger.error(
                                        "In class '${controllerBean::class.simpleName}' the method ${method.first}" +
                                                " does not return IMqttMessageListener, ignored."
                                    )
                                } else {
                                    logger.info("MQTT: method ${controllerBean::class.simpleName}.${method.first.name} subscribed as listener to ${method.second!!.topic}.")
                                    client.subscribe(
                                        method.second!!.topic,
                                        method.second!!.qos,
                                        method.first.call(controllerBean) as IMqttMessageListener
                                    )
                                }
                            }
                    }
                }
            }
        logger.info("Mqtt subscribed to every needed topic")
    }
}
