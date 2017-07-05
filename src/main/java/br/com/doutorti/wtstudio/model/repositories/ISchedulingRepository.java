package br.com.doutorti.wtstudio.model.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.doutorti.wtstudio.model.SchedulingEntity;

public interface ISchedulingRepository extends
		JpaRepository<SchedulingEntity, Long>, ISchedulingRepositoryCustom {

	@Query("SELECT s FROM SchedulingEntity s ORDER BY s.initialDate ASC")
	public List<SchedulingEntity> findAllOrderByInitialDateAsc();

	@Query("SELECT s FROM SchedulingEntity s WHERE s.initialDate BETWEEN ?1 AND ?2 and s.employee.id = ?3 ORDER BY s.initialDate")
	public List<SchedulingEntity> findByDayAndEmployee(Date dateI, Date dateF,
			Long employeeId);

	@Query("SELECT s FROM SchedulingEntity s WHERE s.wasRepetition = true AND s.initialDate >= CURRENT_DATE AND HOUR(s.initialDate) = HOUR(?1) AND s.client.id = ?2 AND s.employee.id = ?3 AND WEEKDAY(s.initialDate) = WEEKDAY(?1) ORDER BY s.initialDate")
	public List<SchedulingEntity> findByWasRepetitionAndHour(Date initialDate,
			Long clientId, Long employeeId);

	@Query("SELECT s FROM SchedulingEntity s WHERE s.client.id = ?1 AND s.initialDate > ?2 ORDER BY s.initialDate DESC")
	public List<SchedulingEntity> findFuturesByClient(Long clientId, Date brokerDate);

}