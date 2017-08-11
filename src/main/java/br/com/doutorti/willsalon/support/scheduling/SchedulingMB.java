package br.com.doutorti.willsalon.support.scheduling;

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
import br.com.doutorti.willsalon.model.EmployeeEntity;
import br.com.doutorti.willsalon.model.SchedulingEntity;
import br.com.doutorti.willsalon.model.repositories.IClientRepository;
import br.com.doutorti.willsalon.model.repositories.IEmployeeRepository;
import br.com.doutorti.willsalon.model.repositories.ISchedulingRepository;
import br.com.doutorti.willsalon.model.utils.BaseBeans;
import br.com.doutorti.willsalon.model.utils.HourUtils;

// ConfigurableBeanFactory.SCOPE_SINGLETON, ConfigurableBeanFactory.SCOPE_PROTOTYPE,
// WebApplicationContext.SCOPE_REQUEST, WebApplicationContext.SCOPE_SESSION
@Scope( "view" )
@Named( value = "schedulingMB" )
public class SchedulingMB extends BaseBeans {

	private static final long serialVersionUID = 201404221641L;

	Logger logger = Logger.getLogger( SchedulingMB.class );

	@Inject
	private ISchedulingRepository schedulingRepository;

	private List<SchedulingEntity> schedulings;

	private Long id;

	private Long idToDelete;

	private SchedulingEntity schedulingShow;

	private SchedulingEntity itemRowSelected;

	private EmployeeEntity employeeFilter;

	private ClientEntity clientFilter;

	private Date dateFilter;

	private List<SchedulingEntity> schedulingRepeatedList;

	private Boolean isDeleteMany;

	@Inject
	private IEmployeeRepository employeeRepository;

	@Inject
	private IClientRepository clientRepository;

	public SchedulingMB() {
		logger.info( "ping" );
		clientFilter = null;
		employeeFilter = null;
		dateFilter = null;
		setIsDeleteMany( false );
	}

	@PostConstruct
	public void onLoad() {
		// nothing
	}

	public void find() {
		SchedulingEntity schedulingFindEntity = new SchedulingEntity();
		schedulingFindEntity.setClient( clientFilter );
		schedulingFindEntity.setEmployee( employeeFilter );
		schedulingFindEntity.setInitialDate( dateFilter );

		schedulings = schedulingRepository.findCustom( schedulingFindEntity );
		if ( dateFilter != null ) {
			ArrayList<String> dateHourClosedList = new ArrayList<String>();
			List<String> closedList = new ArrayList<String>();
			for ( SchedulingEntity e : schedulings ) {
				Calendar cTmp = Calendar.getInstance();
				cTmp.setTime( e.getInitialDate() );
				closedList.add( HourUtils.getCorrectHourOrMinute( cTmp.get( Calendar.HOUR_OF_DAY ) ) + ":" + HourUtils.getCorrectHourOrMinute( cTmp.get( Calendar.MINUTE ) ) );
				String out = String.format( "%s - %s até %s", e.getClient().getName(), e.getInitialDateFormatWithoutDay(), e.getFinalDatePrevisionFormatWithoutDay() );
				dateHourClosedList.add( out );

				Calendar cTmp2 = Calendar.getInstance();
				cTmp2.setTime( e.getFinalDatePrevision() );

				for ( ; cTmp.compareTo( cTmp2 ) < 0; ) {
					cTmp.add( Calendar.MINUTE, 1 );
					if ( cTmp.compareTo( cTmp2 ) == 0 )
						break;
					closedList.add( HourUtils.getCorrectHourOrMinute( cTmp.get( Calendar.HOUR_OF_DAY ) ) + ":" + HourUtils.getCorrectHourOrMinute( cTmp.get( Calendar.MINUTE ) ) );
				}

			}

			List<String> completeList = HourUtils.completeListHours();

			completeList.removeAll( closedList );

			for ( String item : completeList ) {
				SchedulingEntity fakeItem = new SchedulingEntity();
				ClientEntity clientFake = new ClientEntity();
				clientFake.setName( "DISPONÍVEL" );
				clientFake.setPhone( " - " );
				fakeItem.setClient( clientFake );
				fakeItem.setId( 0l );
				Calendar c = Calendar.getInstance();
				c.setTime( dateFilter );
				c.set( Calendar.HOUR_OF_DAY, Integer.parseInt( item.split( ":" )[0] ) );
				c.set( Calendar.MINUTE, Integer.parseInt( item.split( ":" )[1] ) );
				fakeItem.setInitialDate( c.getTime() );
				schedulings.add( fakeItem );
			}
		}

		Comparator<? super SchedulingEntity> comparator = new Comparator<SchedulingEntity>() {
			@Override
			public int compare( SchedulingEntity o1, SchedulingEntity o2 ) {
				if ( o1.getInitialDate().before( o2.getInitialDate() ) )
					return -1;
				else if ( o1.getInitialDate().after( o2.getInitialDate() ) ) {
					return 1;
				} else
					return 0;
			}
		};

		Collections.sort( schedulings, comparator );
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
		this.setSchedulingShow( this.schedulingRepository.findOne( id ) );
		RequestContext.getCurrentInstance().execute( "PF('dialog_show').show()" );
		RequestContext.getCurrentInstance().update( "dialog_show" );
	}

	public void loadSchedulingRepeatedList( Long id ) {
		setIsDeleteMany( true );
		schedulingRepeatedList = schedulingRepository.findByWasRepetitionAndHour( getItemRowSelected().getInitialDate(), getItemRowSelected().getClient().getId(), getItemRowSelected().getEmployee().getId() );
		this.setSchedulingShow( this.schedulingRepository.findOne( id ) );
		RequestContext.getCurrentInstance().execute( "PF('dialog_delete').show()" );
		RequestContext.getCurrentInstance().update( "dialog_delete" );
	}

	public Long getId() {
		return this.id;
	}

	public void setId( Long id ) {
		this.id = id;
	}

	public void delete() {
		if ( this.idToDelete != null ) {
			this.schedulingRepository.delete( this.idToDelete );
			find();
		}
	}

	public void deleteMany() {
		if ( this.schedulingRepeatedList != null && !this.schedulingRepeatedList.isEmpty() ) {
			this.schedulingRepository.delete( schedulingRepeatedList );
			find();
		}
	}

	public void showQuestionDialog() {
		idToDelete = getItemRowSelected().getId();
		// se for fake nao mostra
		if ( idToDelete == 0 ) {
			return;
		}
		setIsDeleteMany( false );
		schedulingShow = getItemRowSelected();
		RequestContext.getCurrentInstance().update( "dialog_question" );
		RequestContext.getCurrentInstance().update( "dialog_delete" );
		RequestContext.getCurrentInstance().execute( "PF('dialog_question').show()" );
	}

	public List<EmployeeEntity> autocompleteEmployee( String query ) {
		return employeeRepository.findByNameContaining( query );
	}

	public List<ClientEntity> autocompleteClient( String query ) {
		return clientRepository.findByNameContaining( query );
	}

	public List<SchedulingEntity> getSchedulings() {
		return schedulings;
	}

	public void setSchedulings( List<SchedulingEntity> schedulings ) {
		this.schedulings = schedulings;
	}

	public Long getIdToDelete() {
		return idToDelete;
	}

	public void setIdToDelete( Long idToDelete ) {
		this.idToDelete = idToDelete;
	}

	public SchedulingEntity getSchedulingShow() {
		return schedulingShow;
	}

	public void setSchedulingShow( SchedulingEntity schedulingShow ) {
		this.schedulingShow = schedulingShow;
	}

	public SchedulingEntity getItemRowSelected() {
		return itemRowSelected;
	}

	public void setItemRowSelected( SchedulingEntity itemRowSelected ) {
		this.itemRowSelected = itemRowSelected;
	}

	public EmployeeEntity getEmployeeFilter() {
		return employeeFilter;
	}

	public void setEmployeeFilter( EmployeeEntity employeeFilter ) {
		this.employeeFilter = employeeFilter;
	}

	public Date getDateFilter() {
		return dateFilter;
	}

	public void setDateFilter( Date dateFilter ) {
		this.dateFilter = dateFilter;
	}

	public ClientEntity getClientFilter() {
		return clientFilter;
	}

	public void setClientFilter( ClientEntity clientFilter ) {
		this.clientFilter = clientFilter;
	}

	public List<SchedulingEntity> getSchedulingRepeatedList() {
		return schedulingRepeatedList;
	}

	public void setSchedulingRepeatedList( List<SchedulingEntity> schedulingRepeatedList ) {
		this.schedulingRepeatedList = schedulingRepeatedList;
	}

	public Boolean getIsDeleteMany() {
		return isDeleteMany;
	}

	public void setIsDeleteMany( Boolean isDeleteMany ) {
		this.isDeleteMany = isDeleteMany;
	}

}
