package br.com.doutorti.willsalon.support.clientHistory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.springframework.context.annotation.Scope;

import br.com.doutorti.willsalon.model.ClientEntity;
import br.com.doutorti.willsalon.model.SchedulingEntity;
import br.com.doutorti.willsalon.model.enuns.AbsenceTime;
import br.com.doutorti.willsalon.model.repositories.IClientRepository;
import br.com.doutorti.willsalon.model.repositories.ISchedulingRepository;
import br.com.doutorti.willsalon.model.utils.BaseBeans;

@Scope( "view" )
@Named( value = "clientHistoryMB" )
public class ClientHistoryMB extends BaseBeans {

	private static final long serialVersionUID = -8716744236556383338L;

	Logger logger = Logger.getLogger( ClientHistoryMB.class );

	@Inject
	private ISchedulingRepository schedulingRepository;

	private List<SchedulingEntity> clientHistories;

	private Long id;

	private SchedulingEntity clientHistoryShow;

	private SchedulingEntity itemRowSelected;

	private ClientEntity clientFilter;

	private AbsenceTime absenceTime;

	@Inject
	private IClientRepository clientRepository;

	public ClientHistoryMB() {
		clientFilter = null;
		clientHistories = new ArrayList<SchedulingEntity>();
	}

	@PostConstruct
	public void onLoad() {
		// nothing
	}

	public void find() {
		SchedulingEntity schedulingFindEntity = new SchedulingEntity();
		schedulingFindEntity.setClient( clientFilter );

		if ( absenceTime != null ) {
			clientFilter = null;
			clientHistories = schedulingRepository.findByAbsense( absenceTime );
		} else if ( clientFilter != null ) {
			clientHistories = schedulingRepository.findCustom( schedulingFindEntity, true );
		} else {
			clientHistories = new ArrayList<SchedulingEntity>();
			return;
		}

		Comparator<? super SchedulingEntity> comparator = new Comparator<SchedulingEntity>() {
			@Override
			public int compare( SchedulingEntity o1, SchedulingEntity o2 ) {
				if ( o1.getViewHistory() ) {
					return o1.getInitialDateFormatWithDayAndHours().compareTo( o2.getInitialDateFormatWithDayAndHours() );
				} else {
					if ( o1.getInitialDate().before( o2.getInitialDate() ) )
						return -1;
					else if ( o1.getInitialDate().after( o2.getInitialDate() ) ) {
						return 1;
					} else
						return 0;
				}
			}
		};

		Collections.sort( clientHistories, comparator );
	}

	public Date getCorrectHourDay( Date date, boolean init ) {
		if ( date == null )
			return null;
		Calendar c = Calendar.getInstance();
		c.setTime( date );
		c.set( Calendar.HOUR_OF_DAY, init ? 0 : 23 );
		c.set( Calendar.MINUTE, init ? 0 : 59 );
		c.set( Calendar.SECOND, init ? 0 : 59 );
		return c.getTime();
	}

	public void show( Long id ) {
		this.setClientHistoryShow( this.schedulingRepository.findOne( id ) );
		RequestContext.getCurrentInstance().execute( "PF('dialog_show').show()" );
		RequestContext.getCurrentInstance().update( "dialog_show" );
	}

	public AbsenceTime[] getAbsenceTimeList() {
		return AbsenceTime.values();
	}

	public Long getId() {
		return this.id;
	}

	public void setId( Long id ) {
		this.id = id;
	}

	public void showQuestionDialog() {
		clientHistoryShow = getItemRowSelected();
		if ( !clientHistoryShow.getViewHistory() ) {
			RequestContext.getCurrentInstance().update( "dialog_question" );
			RequestContext.getCurrentInstance().execute( "PF('dialog_question').show()" );
		}
	}

	public List<ClientEntity> autocompleteClient( String query ) {
		return clientRepository.findByNameContaining( query );
	}

	public SchedulingEntity getItemRowSelected() {
		return itemRowSelected;
	}

	public void setItemRowSelected( SchedulingEntity itemRowSelected ) {
		this.itemRowSelected = itemRowSelected;
	}

	public ClientEntity getClientFilter() {
		return clientFilter;
	}

	public void setClientFilter( ClientEntity clientFilter ) {
		this.clientFilter = clientFilter;
	}

	public List<SchedulingEntity> getClientHistories() {
		return clientHistories;
	}

	public void setClientHistories( List<SchedulingEntity> clientHistories ) {
		this.clientHistories = clientHistories;
	}

	public SchedulingEntity getClientHistoryShow() {
		return clientHistoryShow;
	}

	public void setClientHistoryShow( SchedulingEntity clientHistoryShow ) {
		this.clientHistoryShow = clientHistoryShow;
	}

	public AbsenceTime getAbsenceTime() {
		return absenceTime;
	}

	public void setAbsenceTime( AbsenceTime absenceTime ) {
		this.absenceTime = absenceTime;
	}

}
