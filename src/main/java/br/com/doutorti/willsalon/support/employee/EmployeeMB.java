package br.com.doutorti.willsalon.support.employee;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

import br.com.doutorti.willsalon.model.EmployeeEntity;
import br.com.doutorti.willsalon.model.repositories.IEmployeeRepository;
import br.com.doutorti.willsalon.model.utils.BaseBeans;

// ConfigurableBeanFactory.SCOPE_SINGLETON, ConfigurableBeanFactory.SCOPE_PROTOTYPE,
// WebApplicationContext.SCOPE_REQUEST, WebApplicationContext.SCOPE_SESSION
@Scope( value = WebApplicationContext.SCOPE_SESSION )
@Named( value = "employeeMB" )
public class EmployeeMB extends BaseBeans {

	private static final long serialVersionUID = 201404221641L;

	Logger logger = Logger.getLogger( EmployeeMB.class );

	@Inject
	private IEmployeeRepository employeeRepository;

	private List<EmployeeEntity> employees;

	private Long id;

	private Long idToDelete;

	public EmployeeMB() {
		logger.info( "ping" );
	}

	public void onLoad() {
		this.employees = this.employeeRepository.findAll();
	}

	public List<EmployeeEntity> getEmployees() {
		return this.employees;
	}

	public Long getId() {
		return this.id;
	}

	public void setId( Long id ) {
		this.id = id;
	}

	public void delete() {
		if ( getIdToDelete() != null ) {
			this.employeeRepository.delete( getIdToDelete() );
		}
	}

	public Long getIdToDelete() {
		return idToDelete;
	}

	public void setIdToDelete( Long idToDelete ) {
		this.idToDelete = idToDelete;
	}

}
