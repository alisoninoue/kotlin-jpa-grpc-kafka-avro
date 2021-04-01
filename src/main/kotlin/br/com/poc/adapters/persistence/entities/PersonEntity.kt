package br.com.poc.adapters.persistence.entities

import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "person")
data class PersonEntity(
    @Id
    val cpf: Long? = null,

    @NotNull
    @Column(name = "name", nullable = false)
    val name: String? = null
)