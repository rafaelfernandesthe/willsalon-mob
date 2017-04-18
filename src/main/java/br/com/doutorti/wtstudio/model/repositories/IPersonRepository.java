package br.com.doutorti.wtstudio.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.doutorti.wtstudio.model.PersonEntity;

public interface IPersonRepository extends JpaRepository<PersonEntity, Long> {

}
