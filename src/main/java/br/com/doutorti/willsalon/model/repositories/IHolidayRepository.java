package br.com.doutorti.willsalon.model.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.doutorti.willsalon.model.HolidayEntity;

public interface IHolidayRepository extends JpaRepository<HolidayEntity, Long> {

	@Query( "select h from HolidayEntity h where YEAR(h.initialDate) = ?1" )
	List<HolidayEntity> findByYear( Integer filterYear );

	@Query( "select h from HolidayEntity h where YEAR(h.initialDate) = ?1 and MONTH(h.initialDate) = ?2" )
	List<HolidayEntity> findByYearAndMonth( Integer filterYear, Integer month );

	@Query( "select h from HolidayEntity h where YEAR(h.initialDate) = ?1 and MONTH(h.initialDate) = ?2 and DAY(h.initialDate) = ?3" )
	List<HolidayEntity> findByYearAndMonthAndDay( Integer filterYear, Integer month, Integer day );

}
