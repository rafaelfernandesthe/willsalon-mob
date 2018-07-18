package br.com.doutorti.willsalon.model.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.doutorti.willsalon.model.ClientEntity;

public interface IClientRepository extends JpaRepository<ClientEntity, Long> {

	@Query( "select c from ClientEntity c where upper(c.name) like upper(concat('%',?1,'%')) order by c.name asc" )
	public List<ClientEntity> findByNameContaining( String name );

	@Query( "select c1 from ClientEntity c1 where c1.id = (select min(c.id) from ClientEntity c where c.phone like concat('%',?1,'%') and DATE(c.birthDate) = DATE(?2))" )
	public ClientEntity findByPhoneContainingAndBirthDate( String phone, Date birthDate );

	public List<ClientEntity> findAllByOrderByNameAsc();

	@Query( "select min(c.id) from ClientEntity c where c.phone = ?1 and DATE(c.birthDate) = DATE(?2) and c.id != ?3" )
	public Long findClientWithSamePhoneAndBirthDate( String phone, Date birthDate, Long id );
	
	//@formatter:off
	@Query( nativeQuery = true, value = "select person.pk_id_person, person.name, person.phone," + 
			"(select max(sin.initialDate) from scheduling sin where sin.fk_id_client = person.pk_id_person and sin.fk_id_employee = ?1 and sin.initialDate < now()+1) as dt_ulti," + 
			"rq.qtd " + 
			"from (select p.pk_id_person as pk,count(*) as qtd from scheduling s,client c,person p where s.fk_id_client = c.pk_id_person and c.pk_id_person = p.pk_id_person and s.fk_id_employee = ?1 group by p.pk_id_person having qtd > 1) as rq, person person where rq.pk = person.pk_id_person and upper(person.name) not like '%INDISPONIVEL%' and upper(person.name) not like '%INDISPONÍVEL%' order by rq.qtd desc,dt_ulti asc" )
	//@formatter:on
	public List<Object[]> findClientWalletReport( Long employeeId );
	
	//@formatter:off
	@Query( nativeQuery = true, value = "select person.pk_id_person, person.name, person.phone," + 
			"(select max(sin.initialDate) from scheduling sin where sin.fk_id_client = person.pk_id_person and sin.fk_id_employee = ?1 and sin.initialDate < now()+1) as dt_ulti," + 
			"rq.qtd " + 
			"from (select p.pk_id_person as pk,count(*) as qtd from scheduling s,client c,person p where s.fk_id_client = c.pk_id_person and c.pk_id_person = p.pk_id_person and s.fk_id_employee = ?1 group by p.pk_id_person having qtd = 1) as rq, person person where rq.pk = person.pk_id_person and upper(person.name) not like '%INDISPONIVEL%' and upper(person.name) not like '%INDISPONÍVEL%' order by dt_ulti asc" )
	//@formatter:on
	public List<Object[]> findClientWalletReportOneTime( Long employeeId );

}
