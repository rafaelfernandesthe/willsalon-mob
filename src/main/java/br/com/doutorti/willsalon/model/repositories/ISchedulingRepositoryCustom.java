package br.com.doutorti.willsalon.model.repositories;

import java.util.List;

import br.com.doutorti.willsalon.model.SchedulingEntity;
import br.com.doutorti.willsalon.model.enuns.AbsenceTime;

public interface ISchedulingRepositoryCustom {

	public List<SchedulingEntity> findCustom( SchedulingEntity schedulingFindEntity, boolean isHistory );

	public boolean existEqualsDate( SchedulingEntity scheduling );

	List<SchedulingEntity> findByAbsense( AbsenceTime absenceTime );

}
