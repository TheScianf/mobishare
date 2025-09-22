package it.uniupo.studenti.mobishare.backend_core.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Entity
@Table(name = "REPORT_TYPE", schema = "mobishare")
class ReportType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Int = 0

    @Size(max = 50)
    @NotNull
    @Column(name = "name", nullable = false, length = 50)
    lateinit var name: String

    @JsonBackReference
    @NotNull
    @OneToMany(mappedBy = "reportType")
    var customerReports: MutableList<CustomerReport> = mutableListOf()
}