package br.com.doutorti.willsalon.model.repositories;

import java.util.List;

import br.com.doutorti.willsalon.model.SchedulingEntity;

public interface ISchedulingRepositoryCustom {

	public List<SchedulingEntity> findCustom(
			SchedulingEntity schedulingFindEntity);

	public boolean existEqualsDate(SchedulingEntity scheduling);

}
