package br.com.doutorti.willsalon.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.doutorti.willsalon.model.PersonEntity;

public interface IPersonRepository extends JpaRepository<PersonEntity, Long> {

}
