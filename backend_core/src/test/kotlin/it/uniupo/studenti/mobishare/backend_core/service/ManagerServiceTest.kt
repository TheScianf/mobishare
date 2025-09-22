package it.uniupo.studenti.mobishare.backend_core.service

import it.uniupo.studenti.mobishare.backend_core.entity.Manager
import it.uniupo.studenti.mobishare.backend_core.repository.ManagerRepository
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import org.mockito.Mockito.`when` as quando

@SpringBootTest
class ManagerServiceTest {

    @MockBean
    lateinit var managerRepository: ManagerRepository

    @Autowired
    lateinit var managerService: ManagerService

    @Test
    fun findByEmail_existingManager() {
        val email = "test@example.com"
        val manager = Manager().apply {
            id = 1
            this.email = email
            passwordU = "encodedPassword"
            isAdmin = false
        }

        quando(managerRepository.findByEmail(email)).thenReturn(manager)

        val result = managerService.findByEmail(email)

        assertEquals(manager, result)
        Mockito.verify(managerRepository).findByEmail(email)
    }

    @Test
    fun findByEmail_nonExistentManager() {
        val email = "nonexistent@example.com"

        quando(managerRepository.findByEmail(email)).thenReturn(null)

        val result = managerService.findByEmail(email)

        assertNull(result)
        Mockito.verify(managerRepository).findByEmail(email)
    }
}