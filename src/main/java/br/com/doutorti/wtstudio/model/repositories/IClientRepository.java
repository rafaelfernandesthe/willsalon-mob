package br.com.doutorti.wtstudio.model.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.doutorti.wtstudio.model.ClientEntity;

public interface IClientRepository extends JpaRepository<ClientEntity, Long> {

	@Query("select c from ClientEntity c where upper(c.name) like upper(concat('%',?1,'%')) order by c.name asc")
	public List<ClientEntity> findByNameContaining(String name);

	@Query("select c from ClientEntity c where c.phone like concat('%',?1,'%') and DATE(c.birthDate) = DATE(?2)")
	public ClientEntity findByPhoneContainingAndBirthDate(String phone, Date birthDate);

	public List<ClientEntity> findAllByOrderByNameAsc();

}
