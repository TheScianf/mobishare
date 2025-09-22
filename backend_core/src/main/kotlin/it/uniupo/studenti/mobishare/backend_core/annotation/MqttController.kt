package it.uniupo.studenti.mobishare.backend_core.annotation

import org.springframework.stereotype.Component


@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Component
annotation class MqttController()

interface MqttGenericController
