package br.com.doutorti.wtstudio.support.clientScheduling;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.application.Application;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.primefaces.context.RequestContext;
import org.springframework.context.annotation.Scope;

import br.com.doutorti.wtstudio.model.ClientEntity;
import br.com.doutorti.wtstudio.model.EmployeeEntity;
import br.com.doutorti.wtstudio.model.ProcedureEntity;
import br.com.doutorti.wtstudio.model.SchedulingEntity;
import br.com.doutorti.wtstudio.model.repositories.IClientRepository;
import br.com.doutorti.wtstudio.model.repositories.IEmployeeRepository;
import br.com.doutorti.wtstudio.model.repositories.IProcedureRepository;
import br.com.doutorti.wtstudio.model.repositories.ISchedulingRepository;
import br.com.doutorti.wtstudio.model.utils.BaseBeans;
import br.com.doutorti.wtstudio.model.utils.HourUtils;

//ConfigurableBeanFactory.SCOPE_SINGLETON, ConfigurableBeanFactory.SCOPE_PROTOTYPE,
//WebApplicationContext.SCOPE_REQUEST, WebApplicationContext.SCOPE_SESSION
@Scope( "view" )
@Named( value = "clientSchedulingAddEditMB" )
public class ClientSchedulingAddEditMB extends BaseBeans {

	private static final long serialVersionUID = 201311132355L;

	@Inject
	private ISchedulingRepository schedulingRepository;

	@Inject
	private IClientRepository clientRepository;

	@Inject
	private IEmployeeRepository employeeRepository;

	@Inject
	private IProcedureRepository procedureRepository;

	@Inject
	private FacesContext context;

	private SchedulingEntity scheduling;

	private String title;

	private List<ProcedureEntity> selectedProcedureList;

	private String dateHour;

	private Boolean isShow;

	private List<String> dateHourList;

	private List<String> dateHourClosedList;

	private Boolean canFinish;

	private List<SchedulingEntity> schedulingResult;

	private String internalNotify;

	private String alertMessage;

	private UISelectItems selectItensDateHourBinding;

	private List<SelectItem> selectItensDateHourList;

	private Integer clientId;

	private String messageNoHasTime;

	public ClientSchedulingAddEditMB() {
		this.scheduling = new SchedulingEntity();
		this.scheduling.setInitialDate( new Date() );
		canFinish = false;
		setInternalNotify( null );
		Calendar c = Calendar.getInstance();
		c.set( Calendar.MINUTE, 0 );
		if ( c.get( Calendar.HOUR_OF_DAY ) < 8 ) {
			c.set( Calendar.HOUR_OF_DAY, 8 );
		}
		if ( c.get( Calendar.HOUR_OF_DAY ) > 18 ) {
			c.add( Calendar.DAY_OF_MONTH, 1 );
			scheduling.setInitialDate( c.getTime() );
			c.set( Calendar.HOUR_OF_DAY, 8 );
		}
		isShow = false;
		dateHourClosedList = new ArrayList<String>();
	}

	@PostConstruct
	public void init() {

	}

	public String getSumary() {
		String result = "";
		if ( scheduling.getClient() != null ) {
			result += "Cliente: " + scheduling.getClient().toString() + "\n";
		}
		if ( scheduling.getEmployee() != null ) {
			result += "Profissional: " + scheduling.getEmployee().getName() + "\n";
		}
		if ( scheduling.getProcedureList() != null ) {
			result += "Serviços: " + scheduling.getProcedureList() + "\n";
		}
		if ( scheduling.getInitialDate() != null ) {
			result += "Dia: " + scheduling.getInitialDateFormatWithoutHours() + "\n";
		}
		if ( dateHour != null ) {
			result += "Horário: " + dateHour;
		}

		return result;
	}

	public String getListEmployeeNames() {
		String result = String.format( "(%s)", employeeRepository.getListName().toString().replaceAll( "\\[", "" ).replaceAll( "\\]", "" ) );
		return result;
	}

	public void preSave() {
		scheduling.setProcedureList( null );
		for ( Object p : getSelectedProcedureList() ) {
			scheduling.getProcedureList().add( procedureRepository.findOne( new Long( (String) p ) ) );
		}
		if ( scheduling.getInitialDate() != null && dateHour != null ) {
			Calendar c1 = Calendar.getInstance();
			c1.setTime( scheduling.getInitialDate() );
			c1.set( Calendar.HOUR_OF_DAY, 0 );
			c1.set( Calendar.MINUTE, 0 );
			c1.set( Calendar.SECOND, 0 );
			Calendar c2 = Calendar.getInstance();
			Date dateHourF = new Date();
			try {
				dateHourF = new SimpleDateFormat( "HH:mm" ).parse( dateHour );
			} catch ( ParseException e ) {
				e.printStackTrace();
			}
			c2.setTime( dateHourF );
			c1.set( Calendar.HOUR_OF_DAY, c2.get( Calendar.HOUR_OF_DAY ) );
			c1.set( Calendar.MINUTE, c2.get( Calendar.MINUTE ) );
			scheduling.setInitialDate( c1.getTime() );
		}
	}

	public void cleanFields() {
		scheduling.setInitialDate( new Date() );
		scheduling.setProcedureList( getProcedureList() );
		setDateHourList( new ArrayList<String>() );
	}

	public void loadDateHourList( String accordionToOpen ) {
		if ( !getEmployeeCanSchedule() )
			return;

		Calendar c = Calendar.getInstance();
		c.setTime( scheduling.getInitialDate() );
		c.set( Calendar.HOUR_OF_DAY, 0 );
		c.set( Calendar.MINUTE, 0 );
		c.set( Calendar.SECOND, 0 );
		Date dateI = c.getTime();
		c = Calendar.getInstance();
		c.setTime( scheduling.getInitialDate() );
		c.set( Calendar.HOUR_OF_DAY, 23 );
		c.set( Calendar.MINUTE, 59 );
		c.set( Calendar.SECOND, 59 );
		Date dateF = c.getTime();
		try {
			schedulingResult = schedulingRepository.findByDayAndEmployee( dateI, dateF, scheduling.getEmployee().getId() );
		} catch ( NullPointerException e ) {
			String script = "" + " jQuery(document).ready(function(){" + "	if(location.pathname.endsWith('pages/clientScheduling/addScheduling.faces')){" + " $(window).scrollTop(0);" + "	alert('Ops, ocorreu um erro. Você será direcionado para a tela inicial...');" + " setTimeout(function(){location.href='http://www.willsalon.com/#SCHEDULING'},2000)}});";
			RequestContext.getCurrentInstance().execute( script );
		}

		ArrayList<String> closedList = new ArrayList<String>();
		Calendar cTmp = Calendar.getInstance();
		Calendar cTmp2 = Calendar.getInstance();
		dateHourClosedList = new ArrayList<String>();

		if ( schedulingResult != null && !schedulingResult.isEmpty() ) {
			for ( SchedulingEntity e : schedulingResult ) {
				cTmp.setTime( e.getInitialDate() );
				closedList.add( HourUtils.getCorrectHourOrMinute( cTmp.get( Calendar.HOUR_OF_DAY ) ) + ":" + HourUtils.getCorrectHourOrMinute( cTmp.get( Calendar.MINUTE ) ) );
				String out = String.format( "%s - %s até %s", e.getClient().getName(), e.getInitialDateFormatWithoutDay(), e.getFinalDatePrevisionFormatWithoutDay() );
				dateHourClosedList.add( out );

				cTmp2.setTime( e.getFinalDatePrevision() );

				for ( ; cTmp.compareTo( cTmp2 ) < 0; ) {
					cTmp.add( Calendar.MINUTE, 1 );
					if ( cTmp.compareTo( cTmp2 ) == 0 )
						break;
					closedList.add( HourUtils.getCorrectHourOrMinute( cTmp.get( Calendar.HOUR_OF_DAY ) ) + ":" + HourUtils.getCorrectHourOrMinute( cTmp.get( Calendar.MINUTE ) ) );
				}

			}
		}

		List<String> completeList = HourUtils.completeListHours();

		// setDateHourList(completeList);
		selectItensDateHourList = new ArrayList<SelectItem>();

		int minutesToservice = 0;
		if ( !getSelectedProcedureList().isEmpty() ) {
			for ( Object p : getSelectedProcedureList() ) {
				minutesToservice += procedureRepository.findMinutesByProcedure( new Long( (String) p ) );
			}
		}

		for ( String item : completeList ) {
			if ( closedList.contains( item ) ) {
				continue;
			}

			if ( closedList.contains( HourUtils.sumMinutes( item, minutesToservice ) ) ) {
				continue;
			}

			SelectItem newItem = new SelectItem( item );
			selectItensDateHourList.add( newItem );
		}
		selectItensDateHourBinding.setValue( selectItensDateHourList );

		if ( selectItensDateHourList.isEmpty() )
			messageNoHasTime = "NÃO HÁ HORÁRIO DISPONÍVEL PARA ESSA DATA. ESCOLHA OUTRA DATA OU OUTRO PROFISSIONAL";
		else
			messageNoHasTime = "";

		if ( accordionToOpen.equals( "null" ) )
			return;

		RequestContext.getCurrentInstance().execute( String.format( "PF('accordion_1').select(%s)", accordionToOpen ) );
	}

	public List<ClientEntity> autocompleteClient( String query ) {
		return clientRepository.findByNameContaining( query );
	}

	public List<EmployeeEntity> autocompleteEmployee( String query ) {
		return employeeRepository.findByNameContaining( query );
	}

	public SchedulingEntity getScheduling() {
		return this.scheduling;
	}

	public void setScheduling( SchedulingEntity scheduling ) {
		this.scheduling = scheduling;
	}

	public void add() {
		this.title = this.getResourceProperty( "labels", "button_add" );
	}

	public void update( Long id ) {
		this.scheduling = this.schedulingRepository.findOne( id );
		this.title = this.getResourceProperty( "labels", "button_update" );
	}

	public void prepareFinalDateProvision() {
		setInternalNotify( null );
		canFinish = false;
		preSave();
		if ( !scheduling.getProcedureList().isEmpty() ) {
			if ( schedulingResult == null || schedulingResult.isEmpty() ) {
				canFinish = true;
			}
			scheduling.setInitialDate( HourUtils.zeroMilli( scheduling.getInitialDate() ) );
			scheduling.setFinalDatePrevision( HourUtils.zeroMilli( scheduling.getFinalDatePrevision() ) );
			for ( SchedulingEntity scheduled : schedulingResult ) {
				scheduled.setInitialDate( HourUtils.zeroMilli( scheduled.getInitialDate() ) );
				if ( scheduling.getInitialDate().before( scheduled.getInitialDate() ) && scheduling.getFinalDatePrevision().after( scheduled.getInitialDate() ) ) {
					setInternalNotify( "Não existe tempo suficiente para finalizar todos os serviços desse agendamento, tente outro horário." );
					RequestContext.getCurrentInstance().execute( "PF('dialog_erro').show()" );
					RequestContext.getCurrentInstance().update( "formScheduling:dialog_erro" );
					canFinish = false;
					return;
				} else {
					canFinish = true;
				}
			}
			RequestContext.getCurrentInstance().execute( "PF('accordion_1').unselect(2)" );
		}
	}

	public void save() throws IOException {
		try {
			preSave();
			if ( this.scheduling != null ) {
				scheduling.setFinished( false );
				scheduling.setWasRepetition( false );
				scheduling.setHistory( getHistory() );

				if ( schedulingRepository.existEqualsDate( scheduling ) ) {
					RequestContext.getCurrentInstance().execute( "PF('dialog_conflit').show()" );
					RequestContext.getCurrentInstance().update( "formScheduling:dialog_conflit" );
					return;
				}

				this.schedulingRepository.save( scheduling );

				try {
					SimpleEmail mail = new SimpleEmail();
					mail.setFrom( "willsalon@willsalon.com", "Willsalon.com" );
					mail.setCharset( "utf8" );
					mail.setSubject( "Novo Agendamento de Cliente" );
					mail.setMsg( "O Cliente realizou um novo agendamento: " + scheduling.toString() );
					mail.setSSLOnConnect( true );
					mail.setAuthentication( "willsalon@willsalon.com", "atendimentoWillSalon1" );
					mail.setHostName( "smtp.willsalon.com" );
					mail.setSmtpPort( 587 );
					mail.addTo( "willsalon@willsalon.com", "Willsalon.com" );
					mail.send();

				} catch ( EmailException e ) {
					e.printStackTrace();
				}

				RequestContext.getCurrentInstance().execute( "PF('dialog_saved').show()" );
			}
		} catch ( Exception e ) {
			String script = "" + " jQuery(document).ready(function(){" + "	if(location.pathname.endsWith('pages/clientScheduling/addScheduling.faces')){" + " $(window).scrollTop(0);" + "	alert('Ops, ocorreu um erro. Você será direcionado para a tela inicial...');" + " setTimeout(function(){location.href='http://www.willsalon.com/#SCHEDULING'},2000)}});";
			RequestContext.getCurrentInstance().execute( script );
		}
	}

	private String getHistory() {
		String actualDate = new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" ).format( new Date() );
		return String.format( "Operação realizada pelo próprio usuário às %s", actualDate );
	}

	public Date getCorrectHourDay( Date date, boolean init ) {
		Calendar c = Calendar.getInstance();
		c.setTime( date );
		c.set( Calendar.HOUR_OF_DAY, init ? 0 : 23 );
		c.set( Calendar.MINUTE, init ? 0 : 59 );
		c.set( Calendar.SECOND, init ? 0 : 59 );
		return c.getTime();
	}

	public Boolean getEmployeeCanSchedule() {
		return getScheduling().getEmployee() == null || ( getScheduling().getEmployee() != null && !getScheduling().getEmployee().getMeetByOrder() );
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle( String title ) {
		this.title = title;
	}

	private String getResourceProperty( String resource, String label ) {
		Application application = this.context.getApplication();
		ResourceBundle bundle = application.getResourceBundle( this.context, resource );

		return bundle.getString( label );
	}

	public List<ProcedureEntity> getProcedureList() {
		return procedureRepository.findAllActiviesAndNotAdministrative();
	}

	public String getDateHour() {
		return dateHour;
	}

	public List<String> getDateHourList() {
		if ( dateHourList == null ) {
			dateHourList = new ArrayList<String>();
		}
		return dateHourList;
	}

	public void setDateHour( String dateHour ) {
		this.dateHour = dateHour;
	}

	public String getMaxDate() {
		return new SimpleDateFormat( "dd/MM/yyyy" ).format( new Date() );
	}

	public List<ProcedureEntity> getSelectedProcedureList() {
		if ( selectedProcedureList == null ) {
			selectedProcedureList = new ArrayList<ProcedureEntity>();
		}
		return selectedProcedureList;
	}

	public void setSelectedProcedureList( List<ProcedureEntity> selectedProcedureList ) {
		this.selectedProcedureList = selectedProcedureList;
	}

	public Boolean getIsShow() {
		return isShow;
	}

	public void setIsShow( Boolean isShow ) {
		this.isShow = isShow;
	}

	public void setDateHourList( List<String> dateHourList ) {
		this.dateHourList = dateHourList;
	}

	public List<String> getDateHourClosedList() {
		return dateHourClosedList;
	}

	public void setDateHourClosedList( List<String> dateHourClosedList ) {
		this.dateHourClosedList = dateHourClosedList;
	}

	public Boolean getCanFinish() {
		return canFinish;
	}

	public void setCanFinish( Boolean canFinish ) {
		this.canFinish = canFinish;
	}

	public String getInternalNotify() {
		return internalNotify;
	}

	public void setInternalNotify( String internalNotify ) {
		this.internalNotify = internalNotify;
	}

	public String getAlertMessage() {
		return alertMessage;
	}

	public void setAlertMessage( String alertMessage ) {
		this.alertMessage = alertMessage;
	}

	public List<SelectItem> getSelectItensDateHourList() {
		return selectItensDateHourList;
	}

	public void setSelectItensDateHourList( List<SelectItem> selectItensDateHourList ) {
		this.selectItensDateHourList = selectItensDateHourList;
	}

	public UISelectItems getSelectItensDateHourBinding() {
		return selectItensDateHourBinding;
	}

	public void setSelectItensDateHourBinding( UISelectItems selectItensDateHourBinding ) {
		this.selectItensDateHourBinding = selectItensDateHourBinding;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId( Integer clientId ) {
		this.clientId = clientId;
		this.scheduling.setClient( clientRepository.findOne( clientId.longValue() ) );
	}

	public String getMessageNoHasTime() {
		return messageNoHasTime;
	}

	public void setMessageNoHasTime( String messageNoHasTime ) {
		this.messageNoHasTime = messageNoHasTime;
	}

}
