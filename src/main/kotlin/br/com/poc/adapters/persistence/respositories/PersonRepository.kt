package br.com.poc.adapters.persistence.respositories

import br.com.poc.adapters.persistence.entities.PersonEntity
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository

@Repository
interface PersonRepository : CrudRepository<PersonEntity, Long> {
}