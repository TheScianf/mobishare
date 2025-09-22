package it.uniupo.studenti.mobishare.backend_core.annotation

import org.springframework.stereotype.Component
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class ExceptionResponse(
    val exClass: KClass<out Exception>,
    val responseCode: String,
    val description: String
)
