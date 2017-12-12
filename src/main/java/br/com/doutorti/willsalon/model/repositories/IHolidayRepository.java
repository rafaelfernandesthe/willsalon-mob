package br.com.doutorti.willsalon.model.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.doutorti.willsalon.model.HolidayEntity;

public interface IHolidayRepository extends JpaRepository<HolidayEntity, Long> {

	@Query( "select h from HolidayEntity h where YEAR(h.initialDate) = ?1" )
	List<HolidayEntity> findByYear( Integer filterYear );

}
