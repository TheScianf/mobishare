package it.uniupo.studenti.mobishare.backend_core.annotation

import java.lang.annotation.ElementType
import java.lang.annotation.RetentionPolicy
import kotlin.reflect.KClass


@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class ExpectedErrors(vararg val value: KClass<out Exception>)
