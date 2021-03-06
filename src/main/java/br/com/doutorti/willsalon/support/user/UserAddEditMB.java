package br.com.doutorti.willsalon.support.user;

import java.util.ResourceBundle;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

import br.com.doutorti.willsalon.model.UserEntity;
import br.com.doutorti.willsalon.model.repositories.IUserRepository;
import br.com.doutorti.willsalon.model.utils.BaseBeans;

//ConfigurableBeanFactory.SCOPE_SINGLETON, ConfigurableBeanFactory.SCOPE_PROTOTYPE,
//WebApplicationContext.SCOPE_REQUEST, WebApplicationContext.SCOPE_SESSION
@Scope( value = WebApplicationContext.SCOPE_REQUEST )
@Named( value = "userAddEditMB" )
public class UserAddEditMB extends BaseBeans {

	private static final long serialVersionUID = 201311132355L;

	Logger logger = Logger.getLogger( UserAddEditMB.class );

	@Inject
	private IUserRepository userRepository;

	@Inject
	private UserMB mbUserBean;

	@Inject
	private FacesContext context;

	private UserEntity user;

	private String title;

	public UserAddEditMB() {
		logger.info( "ping" );
		this.user = new UserEntity();
	}

	public UserEntity getUser() {
		return this.user;
	}

	public void setUser( UserEntity user ) {
		this.user = user;
	}

	public void add() {
		this.title = this.getResourceProperty( "labels", "user_add" );
	}

	public void update( Long id ) {
		this.user = userRepository.findOne( id );
		this.title = this.getResourceProperty( "labels", "user_update" );
	}

	public void cancel() {
		this.mbUserBean.unselectUser();
	}

	public void save() {
		if ( this.user != null ) {
			if ( this.user.getId() == null ) {
				// Add
				this.userRepository.save( this.user );
			} else {
				// Update
				this.userRepository.save( this.user );
			}
		}
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

}
