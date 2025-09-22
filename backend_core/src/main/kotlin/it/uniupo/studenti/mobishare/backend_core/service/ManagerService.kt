package it.uniupo.studenti.mobishare.backend_core.service

import it.uniupo.studenti.mobishare.backend_core.dao.DAOManagerInsert
import it.uniupo.studenti.mobishare.backend_core.dao.DAOManagerOnlyWithParkCount
import it.uniupo.studenti.mobishare.backend_core.entity.Manager
import it.uniupo.studenti.mobishare.backend_core.exception.EmailAlreadyExistsException
import it.uniupo.studenti.mobishare.backend_core.exception.UsernameNotFoundException
import it.uniupo.studenti.mobishare.backend_core.repository.ManagerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

interface ManagerService {
    fun findByEmail(email: String): Manager?
    fun findById(id: Int): Manager?
    fun add(manager: DAOManagerInsert): Manager
    fun findAll(): List<DAOManagerOnlyWithParkCount>
    fun toggleAdmin(id: Int): Boolean
    fun delete(id: Int, substitutorId: Int)
    fun deleteByEmail(email: String, subEmail: String)
}

@Service
class ManagerServiceImpl : ManagerService {

    @Autowired
    private lateinit var parkService: ParkService

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    lateinit var managerRepository: ManagerRepository

    /**
     * Returns a manager by email.
     *
     * @param email the email
     * @return the manager if found, null otherwise
     */
    override fun findByEmail(email: String): Manager? {
        return managerRepository.findByEmail(email)
    }

    override fun add(manager: DAOManagerInsert): Manager {
        val manager = Manager().also {
            it.email = manager.email
            it.passwordU = passwordEncoder.encode(manager.password)
            it.isAdmin = manager.admin
        }
        if (findByEmail(manager.email) != null) {
            throw EmailAlreadyExistsException(manager.email)
        }
        return managerRepository.save(manager)
    }

    override fun findAll(): List<DAOManagerOnlyWithParkCount> {
        return managerRepository.findAll().map { DAOManagerOnlyWithParkCount(it) }
    }

    override fun delete(id: Int, substitutorId: Int) {
        val subs = findById(substitutorId)
            ?: throw UsernameNotFoundException("Manager with id $substitutorId not found")
        val manager = findById(id)
            ?: throw UsernameNotFoundException("Manager with id $id not found")
        manager.parks.forEach {
            parkService.setManager(it.id, subs)
        }
        managerRepository.delete(manager)
    }
    override fun deleteByEmail(email: String, subEmail: String) {
        val subs = findByEmail(subEmail)
            ?: throw UsernameNotFoundException("Manager with email $subEmail not found")
        val manager = findByEmail(email)
            ?: throw UsernameNotFoundException("Manager with email $email not found")
        manager.parks.forEach {
            parkService.setManager(it.id, subs)
        }
        managerRepository.delete(manager)
    }

    override fun findById(id: Int): Manager? {
        return managerRepository.findByIdOrNull(id)
    }

    /**
     * Permits by an admin to make another manager an admin.
     * Do not permits to remove admin status, it is just an ELEVATION of privileges (not degradation).
     * */
    override fun toggleAdmin(id: Int): Boolean {
        val m = findById(id)
            ?: throw IllegalArgumentException("Manager with id: $id not found")
        if(!m.isAdmin) {
            m.isAdmin = true
            managerRepository.save(m)
            return true
        } else {
            return false
        }
    }
}
