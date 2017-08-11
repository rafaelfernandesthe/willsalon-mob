package br.com.doutorti.willsalon.support.client;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

import br.com.doutorti.willsalon.model.ClientEntity;
import br.com.doutorti.willsalon.model.repositories.IClientRepository;
import br.com.doutorti.willsalon.model.utils.BaseBeans;

//ConfigurableBeanFactory.SCOPE_SINGLETON, ConfigurableBeanFactory.SCOPE_PROTOTYPE,
//WebApplicationContext.SCOPE_REQUEST, WebApplicationContext.SCOPE_SESSION
@Scope( value = WebApplicationContext.SCOPE_REQUEST )
@Named( value = "clientAddEditMB" )
public class ClientAddEditMB extends BaseBeans {

	private static final long serialVersionUID = 201311132355L;

	Logger logger = Logger.getLogger( ClientAddEditMB.class );

	@Inject
	private IClientRepository clientRepository;

	@Inject
	private FacesContext context;

	private ClientEntity client;

	private String title;

	private Boolean clientView;

	private String contextPhone;

	private String contextBirthDate;

	public ClientAddEditMB() {
		logger.info( "ping" );
		this.client = new ClientEntity();
	}

	public ClientEntity getClient() {
		return this.client;
	}

	public void setClient( ClientEntity client ) {
		this.client = client;
	}

	public void add() {
		this.title = this.getResourceProperty( "labels", "button_add" );
	}

	public void update( Long id ) {
		this.client = clientRepository.findOne( id );
		this.title = this.getResourceProperty( "labels", "button_update" );
	}

	public void save() {
		if ( this.client != null ) {
			client.setPhone( client.getPhone().replaceAll( "\\(", "" ).replaceAll( "\\)", "" ).replaceAll( "-", "" ).replaceAll( " ", "" ).trim() );
			if ( this.client.getId() == null ) {
				this.clientRepository.save( this.client );
				try {
					if ( clientView != null && clientView ) {
						FacesContext.getCurrentInstance().getExternalContext().redirect( "access.faces?phone=" + client.getPhone() + "&birthDate=" + client.getBirthDateFormat() );
					} else {
						FacesContext.getCurrentInstance().getExternalContext().redirect( "list.faces" );
					}
				} catch ( IOException e ) {
					e.printStackTrace();
				}
			} else {
				// Update
				this.clientRepository.save( this.client );
			}
		}
	}

	public String getTitle() {
		if ( client.getId() == null ) {
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

	public Boolean getClientView() {
		return clientView;
	}

	public void setClientView( Boolean clientView ) {
		this.clientView = clientView;
	}

	public String getContextPhone() {
		return contextPhone;
	}

	public void setContextPhone( String contextPhone ) {
		this.contextPhone = contextPhone;
		client.setPhone( contextPhone );
	}

	public String getContextBirthDate() {
		return contextBirthDate;
	}

	public void setContextBirthDate( String contextBirthDate ) {
		this.contextBirthDate = contextBirthDate;
		Date birthDate = null;
		try {
			birthDate = new SimpleDateFormat( "dd/MM/yyyy" ).parse( contextBirthDate );
		} catch ( ParseException e ) {
			e.printStackTrace();
		}
		client.setBirthDate( birthDate );
	}

}
