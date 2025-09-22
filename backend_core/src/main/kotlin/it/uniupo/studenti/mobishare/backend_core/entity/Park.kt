package it.uniupo.studenti.mobishare.backend_core.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Entity
@Table(name = "PARK", schema = "mobishare")
class Park {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Int = 0

    @Size(max = 100)
    @NotNull
    @Column(name = "address", nullable = false, length = 100)
    lateinit var address: String

    @NotNull
    @Column(name = "latitude", nullable = false)
    var latitude: Float = 0f

    @NotNull
    @Column(name = "longitude", nullable = false)
    var longitude: Float = 0f

    @JsonManagedReference
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idManager", nullable = false)
    lateinit var manager: Manager

    @JsonBackReference
    @OneToMany(mappedBy = "park")
    var docks: MutableSet<Dock> = mutableSetOf()
}