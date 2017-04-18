package br.com.doutorti.wtstudio.model.repositories;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.com.doutorti.wtstudio.model.SchedulingEntity;

public class ISchedulingRepositoryImpl implements ISchedulingRepositoryCustom {

	@PersistenceContext
	EntityManager em;

	public List<SchedulingEntity> findCustom(SchedulingEntity schedulingFindEntity) {
		Query q = null;
		String queryString = "SELECT s FROM SchedulingEntity s WHERE 1=1 AND s.employee.id = :employeeId ";
		String clientStatement = null;
		String dateStatement = null;
		if (schedulingFindEntity.getClient() != null) {
			clientStatement = "AND s.client.id = :clientId ";
			queryString += clientStatement;
		}
		boolean hasDate = false;
		if (schedulingFindEntity.getInitialDate() != null) {
			hasDate = true;
			dateStatement = "AND s.initialDate BETWEEN :startDay AND :finalDay ";
		} else {
			hasDate = false;
			schedulingFindEntity.setInitialDate(new Date());
			dateStatement = "AND s.initialDate >= :today ";
		}

		queryString += dateStatement;

		queryString += "ORDER BY s.initialDate ASC";
		q = em.createQuery(queryString);

		q.setParameter("employeeId",
				schedulingFindEntity.getEmployee() != null ? schedulingFindEntity.getEmployee().getId() : null);

		if (clientStatement != null) {
			q.setParameter("clientId",
					schedulingFindEntity.getClient() != null ? schedulingFindEntity.getClient().getId() : null);
		}

		if (hasDate) {
			q.setParameter("startDay", getCorrectHourDay(schedulingFindEntity.getInitialDate(), true));
			q.setParameter("finalDay", getCorrectHourDay(schedulingFindEntity.getInitialDate(), false));
		} else {
			q.setParameter("today", getCorrectHourDay(schedulingFindEntity.getInitialDate(), true));
		}

		return q.getResultList();
	}

	private Date getCorrectHourDay(Date date, boolean init) {
		if (date == null)
			return null;
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, init ? 0 : 23);
		c.set(Calendar.MINUTE, init ? 0 : 59);
		c.set(Calendar.SECOND, init ? 0 : 59);
		return c.getTime();
	}

	@Override
	public boolean existEqualsDate(SchedulingEntity scheduling) {
		String queryString = "SELECT count(*) FROM SchedulingEntity s WHERE 1=1 " + "AND YEAR(s.initialDate) = :year "
				+ "AND MONTH(s.initialDate) = :month " + "AND DAY(s.initialDate) = :day "
				+ "AND HOUR(s.initialDate) = :hour " + "AND MINUTE(s.initialDate) = :minute ";
		Query q = em.createQuery(queryString);
		Calendar c = Calendar.getInstance();
		c.setTime(scheduling.getInitialDate());
		q.setParameter("year", c.get(Calendar.YEAR));
		q.setParameter("month", c.get(Calendar.MONTH) + 1);
		q.setParameter("day", c.get(Calendar.DAY_OF_MONTH));
		q.setParameter("hour", c.get(Calendar.HOUR_OF_DAY));
		q.setParameter("minute", c.get(Calendar.MINUTE));

		return ((Number) q.getSingleResult()).intValue() > 0;
	}

}
