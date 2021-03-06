package br.com.doutorti.willsalon.support.employee;

import java.util.ResourceBundle;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

import br.com.doutorti.willsalon.model.EmployeeEntity;
import br.com.doutorti.willsalon.model.repositories.IEmployeeRepository;
import br.com.doutorti.willsalon.model.utils.BaseBeans;

//ConfigurableBeanFactory.SCOPE_SINGLETON, ConfigurableBeanFactory.SCOPE_PROTOTYPE,
//WebApplicationContext.SCOPE_REQUEST, WebApplicationContext.SCOPE_SESSION
@Scope( value = WebApplicationContext.SCOPE_REQUEST )
@Named( value = "employeeAddEditMB" )
public class EmployeeAddEditMB extends BaseBeans {

	private static final long serialVersionUID = 201311132355L;

	Logger logger = Logger.getLogger( EmployeeAddEditMB.class );

	@Inject
	private IEmployeeRepository employeeRepository;

	@Inject
	private FacesContext context;

	private EmployeeEntity employee;

	private String title;

	public EmployeeAddEditMB() {
		logger.info( "ping" );
		this.employee = new EmployeeEntity();
		this.employee.setMeetByOrder( false );
		this.employee.setActive( true );
	}

	public EmployeeEntity getEmployee() {
		return this.employee;
	}

	public void setEmployee( EmployeeEntity employee ) {
		this.employee = employee;
	}

	public void add() {
		this.title = this.getResourceProperty( "labels", "button_add" );
	}

	public void update( Long id ) {
		this.employee = employeeRepository.findOne( id );
		this.title = this.getResourceProperty( "labels", "button_update" );
	}

	public void save() {
		if ( this.employee != null ) {
			if ( this.employee.getId() == null ) {
				// Add
				this.employeeRepository.save( this.employee );
			} else {
				// Update
				this.employeeRepository.save( this.employee );
			}
		}
	}

	public String getTitle() {
		if ( employee.getId() == null ) {
			this.title = this.getResourceProperty( "labels", "button_add" );
		}
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

}
