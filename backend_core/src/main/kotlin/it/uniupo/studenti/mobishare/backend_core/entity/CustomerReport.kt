package it.uniupo.studenti.mobishare.backend_core.entity

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.hibernate.annotations.ColumnDefault

@Entity
@Table(
    name = "CUSTOMER_REPORT", schema = "mobishare", indexes = [
        Index(name = "FK_CR_2_CUSTOMER", columnList = "idCustomer")
    ]
)
class CustomerReport {
     @EmbeddedId
    lateinit var id: CustomerReportId

    @JsonManagedReference
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idVehicle", nullable = false)
    lateinit var vehicle: Vehicle

    @JsonManagedReference
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idReportType", nullable = false)
    lateinit var reportType: ReportType

    @Size(max = 300)
    @NotNull
    @Column(name = "description", nullable = false, length = 300)
    lateinit var description: String

    @NotNull
    @ColumnDefault("'PENDING'")
    @Lob
    @Column(name = "state", nullable = false)
    lateinit var state: String
}