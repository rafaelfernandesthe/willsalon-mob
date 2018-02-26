package br.com.doutorti.willsalon.model.repositories;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import br.com.doutorti.willsalon.model.ClientEntity;
import br.com.doutorti.willsalon.model.SchedulingEntity;
import br.com.doutorti.willsalon.model.enuns.AbsenceTime;

public class ISchedulingRepositoryImpl implements ISchedulingRepositoryCustom {

	@PersistenceContext
	EntityManager em;

	@Inject
	IClientRepository clientRepository;

	public List<SchedulingEntity> findCustom( SchedulingEntity schedulingFindEntity, boolean isHistory ) {
		Query q = null;
		String queryString = "SELECT s FROM SchedulingEntity s WHERE 1=1 ";
		String employeeStatement = null;
		String clientStatement = null;
		String dateStatement = null;
		if ( schedulingFindEntity.getEmployee() != null ) {
			employeeStatement = "AND s.employee.id = :employeeId ";
			queryString += employeeStatement;
		}
		if ( schedulingFindEntity.getClient() != null ) {
			clientStatement = "AND s.client.id = :clientId ";
			queryString += clientStatement;
		}
		boolean hasDate = false;

		if ( !isHistory ) {
			if ( schedulingFindEntity.getInitialDate() != null ) {
				hasDate = true;
				dateStatement = "AND s.initialDate BETWEEN :startDay AND :finalDay ";
			} else {
				hasDate = false;
				schedulingFindEntity.setInitialDate( new Date() );
				dateStatement = "AND s.initialDate >= :today ";
			}
			queryString += dateStatement;
		}

		queryString += "ORDER BY s.initialDate ASC";
		q = em.createQuery( queryString );

		if ( employeeStatement != null ) {
			q.setParameter( "employeeId", schedulingFindEntity.getEmployee() != null ? schedulingFindEntity.getEmployee().getId() : null );
		}

		if ( clientStatement != null ) {
			q.setParameter( "clientId", schedulingFindEntity.getClient() != null ? schedulingFindEntity.getClient().getId() : null );
		}

		if ( !isHistory ) {
			if ( hasDate ) {
				q.setParameter( "startDay", getCorrectHourDay( schedulingFindEntity.getInitialDate(), true ) );
				q.setParameter( "finalDay", getCorrectHourDay( schedulingFindEntity.getInitialDate(), false ) );
			} else {
				q.setParameter( "today", getCorrectHourDay( schedulingFindEntity.getInitialDate(), true ) );
			}
		}

		return q.getResultList();
	}

	@Override
	public List<SchedulingEntity> findByAbsense( AbsenceTime absenceTime ) {
		String queryStringMax = "SELECT sMax.fk_id_client as c,max(sMax.initialDate) as initialDate FROM scheduling sMax GROUP BY sMax.fk_id_client";
		String queryString = String.format( "SELECT DISTINCT s.c FROM (%s) s WHERE 1=1 ", queryStringMax );
		Query q = null;
		Calendar date1 = null;
		Calendar date2 = null;
		switch ( absenceTime ) {
			case _0:
				queryString = "SELECT c.pk_id_person FROM client c WHERE c.pk_id_person NOT IN (SELECT DISTINCT s.fk_id_client FROM scheduling s)";
				break;
			case _15_30:
				queryString += "AND s.initialDate <= :date1 AND s.initialDate > :date2";
				date1 = Calendar.getInstance();
				date1.add( Calendar.DAY_OF_MONTH, -15 );
				date2 = Calendar.getInstance();
				date2.add( Calendar.DAY_OF_MONTH, -30 );
				break;
			case _30_60:
				queryString += "AND s.initialDate <= :date1 AND s.initialDate > :date2";
				date1 = Calendar.getInstance();
				date1.add( Calendar.DAY_OF_MONTH, -30 );
				date2 = Calendar.getInstance();
				date2.add( Calendar.DAY_OF_MONTH, -60 );
				break;
			case _60_90:
				queryString += "AND s.initialDate <= :date1 AND s.initialDate > :date2";
				date1 = Calendar.getInstance();
				date1.add( Calendar.DAY_OF_MONTH, -60 );
				date2 = Calendar.getInstance();
				date2.add( Calendar.DAY_OF_MONTH, -90 );
				break;
			case _90_N:
				queryString += "AND s.initialDate <= :date1";
				date1 = Calendar.getInstance();
				date1.add( Calendar.DAY_OF_MONTH, -90 );
				break;
			default:
				;
		}

		List<SchedulingEntity> result = new ArrayList<SchedulingEntity>();
//		if ( !AbsenceTime._0.equals( absenceTime ) ) {
//			queryString += " ORDER BY s.initialDate ASC";
//		}
		q = em.createNativeQuery( queryString );
		if ( date1 != null )
			q.setParameter( "date1", date1.getTime(), TemporalType.DATE );
		if ( date2 != null )
			q.setParameter( "date2", date2.getTime(), TemporalType.DATE );

		List<BigInteger> result1 = q.getResultList();
		List<ClientEntity> clients = new ArrayList<ClientEntity>();
		for ( BigInteger o : result1 ) {
			clients.add( clientRepository.findOne( o.longValue() ) );
		}

		for ( ClientEntity c : clients ) {
			result.add( new SchedulingEntity( c.getId(), c ) );
		}
		return result;
	}

	private Date getCorrectHourDay( Date date, boolean init ) {
		if ( date == null )
			return null;
		Calendar c = Calendar.getInstance();
		c.setTime( date );
		c.set( Calendar.HOUR_OF_DAY, init ? 0 : 23 );
		c.set( Calendar.MINUTE, init ? 0 : 59 );
		c.set( Calendar.SECOND, init ? 0 : 59 );
		return c.getTime();
	}

	@Override
	public boolean existEqualsDate( SchedulingEntity scheduling ) {
		String queryString = "SELECT count(*) FROM SchedulingEntity s WHERE 1=1 " + "AND YEAR(s.initialDate) = :year " + "AND MONTH(s.initialDate) = :month " + "AND DAY(s.initialDate) = :day " + "AND HOUR(s.initialDate) = :hour " + "AND MINUTE(s.initialDate) = :minute " + "AND s.employee.id = :employeeId";
		Query q = em.createQuery( queryString );
		Calendar c = Calendar.getInstance();
		c.setTime( scheduling.getInitialDate() );
		q.setParameter( "year", c.get( Calendar.YEAR ) );
		q.setParameter( "month", c.get( Calendar.MONTH ) + 1 );
		q.setParameter( "day", c.get( Calendar.DAY_OF_MONTH ) );
		q.setParameter( "hour", c.get( Calendar.HOUR_OF_DAY ) );
		q.setParameter( "minute", c.get( Calendar.MINUTE ) );
		q.setParameter( "employeeId", scheduling.getEmployee().getId() );
		return ( (Number) q.getSingleResult() ).intValue() > 0;
	}

}
