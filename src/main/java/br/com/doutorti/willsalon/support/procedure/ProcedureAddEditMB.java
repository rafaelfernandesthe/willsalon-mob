package br.com.doutorti.willsalon.support.procedure;

import java.util.ResourceBundle;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

import br.com.doutorti.willsalon.model.ProcedureEntity;
import br.com.doutorti.willsalon.model.repositories.IProcedureRepository;
import br.com.doutorti.willsalon.model.utils.BaseBeans;

//ConfigurableBeanFactory.SCOPE_SINGLETON, ConfigurableBeanFactory.SCOPE_PROTOTYPE,
//WebApplicationContext.SCOPE_REQUEST, WebApplicationContext.SCOPE_SESSION
@Scope( value = WebApplicationContext.SCOPE_REQUEST )
@Named( value = "procedureAddEditMB" )
public class ProcedureAddEditMB extends BaseBeans {

	private static final long serialVersionUID = 201311132355L;

	Logger logger = Logger.getLogger( ProcedureAddEditMB.class );

	@Inject
	private IProcedureRepository procedureRepository;

	@Inject
	private ProcedureMB mbProcedureBean;

	@Inject
	private FacesContext context;

	private ProcedureEntity procedure;

	private String title;

	private Boolean isShow;

	public ProcedureAddEditMB() {
		logger.info( "ping" );
		this.procedure = new ProcedureEntity();
		isShow = false;
	}

	public ProcedureEntity getProcedure() {
		return this.procedure;
	}

	public void setProcedure( ProcedureEntity procedure ) {
		this.procedure = procedure;
	}

	public void add() {
		this.title = this.getResourceProperty( "labels", "button_add" );
	}

	public void update( Long id ) {
		this.procedure = procedureRepository.findOne( id );
		this.title = this.getResourceProperty( "labels", "button_update" );
	}

	public void show( Long id ) {
		this.procedure = procedureRepository.findOne( id );
		isShow = true;
	}

	public void save() {
		if ( this.procedure != null ) {
			if ( this.procedure.getId() == null ) {
				// Add
				this.procedure.setActive( true );
				this.procedureRepository.save( this.procedure );
			} else {
				// Update
				this.procedureRepository.save( this.procedure );
			}
		}
	}

	public String getTitle() {
		if ( procedure.getId() == null ) {
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

	public Boolean getIsShow() {
		return isShow;
	}

	public void setIsShow( Boolean isShow ) {
		this.isShow = isShow;
	}

}
