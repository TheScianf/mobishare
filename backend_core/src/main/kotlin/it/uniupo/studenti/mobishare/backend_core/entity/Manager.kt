package it.uniupo.studenti.mobishare.backend_core.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(
    name = "MANAGER", schema = "mobishare", uniqueConstraints = [
        UniqueConstraint(name = "UK_MANAGER_1_EMAIL", columnNames = ["email"])
    ]
)
class Manager : UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Int = 0

    @Size(max = 256)
    @NotNull
    @Column(name = "email", nullable = false, length = 256)
    lateinit var email: String

    @Size(max = 256)
    @NotNull
    @Column(name = "password", nullable = false, length = 256)
    lateinit var passwordU: String

    @NotNull
    @Column(name = "isAdmin", nullable = false)
    var isAdmin: Boolean = false

    @JsonBackReference
    @OneToMany(mappedBy = "manager")
    var parks: MutableSet<Park> = mutableSetOf()

    //USER DETAILS
    override fun getAuthorities(): Collection<GrantedAuthority?>? {
        val ret = mutableListOf(MANAGER)
        if(isAdmin) {
            ret += ADMIN
        }
        return ret
    }

    override fun getPassword(): String? {
        return passwordU
    }

    override fun getUsername(): String? {
        return email
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    companion object {

        val MANAGER = GrantedAuthority { "ROLE_MANAGER" }
        val ADMIN = GrantedAuthority { "ROLE_ADMIN" }
    }
}
