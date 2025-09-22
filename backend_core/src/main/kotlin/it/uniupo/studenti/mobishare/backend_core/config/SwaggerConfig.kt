package it.uniupo.studenti.mobishare.backend_core.config

import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.media.Content
import io.swagger.v3.oas.models.media.MediaType
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.oas.models.responses.ApiResponses
import it.uniupo.studenti.mobishare.backend_core.annotation.ExceptionResponse
import it.uniupo.studenti.mobishare.backend_core.annotation.ExpectedErrors
import org.reflections.Reflections
import org.reflections.util.ConfigurationBuilder
import org.springdoc.core.customizers.OperationCustomizer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.NoHandlerFoundException
import org.springframework.web.servlet.resource.NoResourceFoundException
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.isSubclassOf


@Configuration
class SpringDocConfig {

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    @Bean
    fun customizeOperation(): OperationCustomizer {
        return OperationCustomizer { operation: Operation, handlerMethod: HandlerMethod ->

            val exceptionMap: MutableMap<KClass<out Exception>, ApiDefinition> = HashMap()

            // Aggiungo qui tutte le tue eccezioni custom e le loro definizioni Swagger
            exceptionMap.put(
                DataIntegrityViolationException::class,
                ApiDefinition("400", "Violazione di un vincolo nella base di dati")
            )
            exceptionMap.put(
                NoResourceFoundException::class,
                ApiDefinition("404", "La risorsa richiesta non è stata trovata")
            )
            exceptionMap.put(
                HttpMessageNotReadableException::class,
                ApiDefinition("400", "Sintassi dell'input non valida")
            )
            exceptionMap.put(
                NoHandlerFoundException::class,
                ApiDefinition("404", "Nessuna route trovata per la richiesta")
            )
            Reflections(ConfigurationBuilder().forPackages("it.uniupo.studenti.mobishare.backend_core.exception"))
                .getTypesAnnotatedWith(ExceptionResponse::class.java)
                .map { it.kotlin }
                .filter {
                    val ann = handlerMethod.getMethodAnnotation(ExpectedErrors::class.java)
                    ann?.value?.contains(it) ?: false
                }
                .forEach {
                    if (it.isSubclassOf(Exception::class)) {
                        println("adding exception: ${it.simpleName} with annotation ${it.findAnnotation<ExceptionResponse>()!!.responseCode} and ${it.findAnnotation<ExceptionResponse>()!!.description}")
                        @Suppress("UNCHECKED_CAST")
                        exceptionMap.put(
                            it as KClass<out Exception>,
                            ApiDefinition(
                                it.findAnnotation<ExceptionResponse>()!!.responseCode,
                                it.findAnnotation<ExceptionResponse>()!!.description
                            )
                        )
                    }
                }

            // Aggiungo un 500 e un 403_role generico a tutte le operazioni per sicurezza
            if (operation.responses == null) {
                operation.responses = ApiResponses()
            }
            if (operation.responses.get("500") == null) {
                val api500: ApiResponse = ApiResponse()
                    .description("Errore interno del server")
                    .content(
                        Content().addMediaType(
                            "application/text",
                            MediaType().schema(Schema<String>())
                        )
                    )
                operation.responses.addApiResponse("500", api500)
            }
            if (operation.responses.get("403") == null) {
                if (handlerMethod.getMethodAnnotation(PreAuthorize::class.java) != null) {
                    val api500: ApiResponse = ApiResponse()
                        .description("Accesso negato per il ruolo fornito")
                        .content(
                            Content().addMediaType(
                                "application/text",
                                MediaType().schema(Schema<String>())
                            )
                        )
                    operation.responses.addApiResponse("403", api500)
                }
            }

            val expectedErrors: ExpectedErrors? = handlerMethod.getMethodAnnotation(ExpectedErrors::class.java)
            if (expectedErrors != null) {
                for (exceptionClass in expectedErrors.value) {
                    val def: ApiDefinition? = exceptionMap[exceptionClass]
                    if (def != null) {
                        val apiResponse: ApiResponse = ApiResponse()
                            .description(def.description)
                            .content(
                                Content().addMediaType(
                                    "application/text",
                                    MediaType().schema(Schema<String>())
                                )
                            )
                        operation.responses.addApiResponse(def.responseCode, apiResponse)
                    }
                }
            }
            operation
        }
    }
    //TODO: sistemare nei controller con responseEntity<*>, documentare a mano
}

// Classe helper per la definizione dell'API
data class ApiDefinition(var responseCode: String, var description: String)
