package br.com.doutorti.willsalon.model.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.doutorti.willsalon.model.EmployeeEntity;

public interface IEmployeeRepository extends
		JpaRepository<EmployeeEntity, Long> {
	@Query("select c from EmployeeEntity c where upper(c.name) like upper(concat('%',?1,'%'))")
	public List<EmployeeEntity> findByNameContaining(String query);
	
	@Query("select c from EmployeeEntity c where upper(c.name) like upper(concat('%',?1,'%')) and c.active = true ")
	public List<EmployeeEntity> findByNameActivesContaining(String query);

	@Query("select c.name from EmployeeEntity c")
	public List<String> getListName();
	
	@Query("select c.name from EmployeeEntity c where c.active = true")
	public List<String> getListNameActives();

}
