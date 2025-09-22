package it.uniupo.studenti.mobishare.backend_core.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "CUSTOMER", schema = "mobishare")
class Customer: UserDetails{
    @Id
    @Size(max = 50)
    @Column(name = "username", nullable = false, length = 50)
    lateinit var usernameU: String // rename due to userDetails

    @Size(max = 50)
    @NotNull
    @Column(name = "name", nullable = false, length = 50)
    lateinit var name: String

    @Size(max = 50)
    @NotNull
    @Column(name = "surname", nullable = false, length = 50)
    lateinit var surname: String

    @Size(max = 16)
    @NotNull
    @Column(name = "cf", nullable = false, length = 16)
    lateinit var cf: String

    @NotNull
    @Lob
    @Column(name = "gender", nullable = false)
    lateinit var gender: String

    @Size(max = 256)
    @NotNull
    @Column(name = "password", nullable = false, length = 256)
    lateinit var passwordU: String // rename due to userDetails

    @Size(max = 256)
    @Column(name = "email", length = 256, unique = true)
    lateinit var email: String

    @JsonBackReference
    @OneToMany(mappedBy = "customer")
    var coupons: MutableSet<Coupon> = mutableSetOf()

    @JsonBackReference
    @OneToMany(mappedBy = "customer")
    var races: MutableSet<Race> = mutableSetOf()

    @JsonBackReference
    @OneToMany(mappedBy = "customer")
    var suspensions: MutableSet<Suspension> = mutableSetOf()

    @JsonBackReference
    @OneToMany(mappedBy = "customer")
    var transactions: MutableSet<Transaction> = mutableSetOf()

    //USER DETAILS
    override fun getAuthorities(): Collection<GrantedAuthority?>? {
        return listOf(
            ROLE
        )
    }

    override fun getPassword(): String? {
        return passwordU
    }

    override fun getUsername(): String? {
        return usernameU
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
        private val ROLE = GrantedAuthority { "ROLE_CUSTOMER" }
    }
}