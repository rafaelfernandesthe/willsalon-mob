package br.com.doutorti.willsalon.support.holiday;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;

import br.com.doutorti.willsalon.model.HolidayEntity;
import br.com.doutorti.willsalon.model.repositories.IHolidayRepository;
import br.com.doutorti.willsalon.model.utils.BaseBeans;

@Scope( "view" )
@Named( value = "holidayMB" )
public class HolidayMB extends BaseBeans {

	private static final long serialVersionUID = 538532308955296799L;

	Logger logger = Logger.getLogger( HolidayMB.class );

	@Inject
	private IHolidayRepository holidayRepository;

	private List<HolidayEntity> holidays;

	private Long id;

	private Long idToDelete;

	private Integer filterYear;

	public HolidayMB() {
		logger.info( "ping" );
	}

	public void filterHoliday() {
		if ( filterYear == null ) {
			filterYear = Calendar.getInstance().get( Calendar.YEAR );
		}
		this.holidays = this.holidayRepository.findByYear( filterYear );
	}

	public void onLoad() {
		filterHoliday();
	}

	public Long getId() {
		return this.id;
	}

	public void setId( Long id ) {
		this.id = id;
	}

	public void delete() {
		if ( getIdToDelete() != null ) {
			this.holidayRepository.delete( getIdToDelete() );
		}
		filterHoliday();
	}

	public Long getIdToDelete() {
		return idToDelete;
	}

	public void setIdToDelete( Long idToDelete ) {
		this.idToDelete = idToDelete;
	}

	public List<HolidayEntity> getHolidays() {
		return holidays;
	}

	public void setHolidays( List<HolidayEntity> holidays ) {
		this.holidays = holidays;
	}

	public Integer getFilterYear() {
		return filterYear;
	}

	public void setFilterYear( Integer filterYear ) {
		this.filterYear = filterYear;
	}

}
