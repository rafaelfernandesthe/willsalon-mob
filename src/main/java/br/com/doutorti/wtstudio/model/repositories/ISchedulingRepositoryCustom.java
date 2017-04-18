package br.com.doutorti.wtstudio.model.repositories;

import java.util.List;

import br.com.doutorti.wtstudio.model.SchedulingEntity;

public interface ISchedulingRepositoryCustom {

	public List<SchedulingEntity> findCustom(
			SchedulingEntity schedulingFindEntity);

	public boolean existEqualsDate(SchedulingEntity scheduling);

}
