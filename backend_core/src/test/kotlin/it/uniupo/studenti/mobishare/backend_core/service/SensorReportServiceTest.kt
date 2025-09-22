package it.uniupo.studenti.mobishare.backend_core.service

import it.uniupo.studenti.mobishare.backend_core.entity.Sensor
import it.uniupo.studenti.mobishare.backend_core.entity.SensorReport
import it.uniupo.studenti.mobishare.backend_core.entity.Vehicle
import it.uniupo.studenti.mobishare.backend_core.repository.SensorReportRepository
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import org.mockito.Mockito.`when` as quando
import java.time.LocalDate
import java.util.*

@SpringBootTest
class SensorReportServiceTest {

    @MockBean
    lateinit var sensorReportRepository: SensorReportRepository

    @Autowired
    lateinit var sensorReportService: SensorReportService

    @Test
    fun deleteAllBySensor() {
        val sensor = Sensor().apply {
            id = 1
        }

        sensorReportService.deleteAllBySensor(sensor)

        Mockito.verify(sensorReportRepository).deleteAllBySensor(sensor)
    }

    @Test
    fun addSensorReport() {
        val time = Date()
        val sensor = Sensor().apply {
            id = 1
        }
        val value = 25.5f
        val state = "P"

        sensorReportService.addSensorReport(time, sensor, value, state)

        Mockito.verify(sensorReportRepository).save(Mockito.any(SensorReport::class.java))
    }

    @Test
    fun setPendingToDeprecatedBySensor() {
        val sensor = Sensor().apply {
            id = 1
        }

        sensorReportService.setPendingToDeprecatedBySensor(sensor)

        Mockito.verify(sensorReportRepository).updatePendingToDeprecatedBySensor(sensor)
    }
}