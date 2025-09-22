package it.uniupo.studenti.mobishare.backend_core

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["it.uniupo.studenti.mobishare.backend_core"])
class BackendCoreApplication

/**
 * The entry point of the application.
 *
 * @param args command-line arguments passed to the application.
 */
fun main(args: Array<String>) {
    runApplication<BackendCoreApplication>(*args)
}

/**
 * Converts the given object to its JSON representation.
 *
 * @param obj the object to convert
 * @return the JSON representation of the object
 */
fun toJson(obj: Any): String {
    return jacksonObjectMapper().writeValueAsString(obj)

}
inline fun <reified T> fromJson(json: String): T {
    return jacksonObjectMapper().readValue(json)
}
/**
 * Returns the sum of the results of applying the given selector function to each element in the iterable.
 *
 * @param selector a function that takes an element of the iterable and returns a Float.
 * @return the sum of the Float values returned by the selector function for each element.
 */
fun <T> Iterable<T>.sumOf(selector: (T) -> Float): Float {
    return this.map { selector(it) }.sum()
}