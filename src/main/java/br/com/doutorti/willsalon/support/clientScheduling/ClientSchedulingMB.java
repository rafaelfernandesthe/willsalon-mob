package br.com.doutorti.willsalon.support.clientScheduling;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

import br.com.doutorti.willsalon.model.SchedulingEntity;
import br.com.doutorti.willsalon.model.repositories.ISchedulingRepository;
import br.com.doutorti.willsalon.model.utils.BaseBeans;

// ConfigurableBeanFactory.SCOPE_SINGLETON, ConfigurableBeanFactory.SCOPE_PROTOTYPE,
// WebApplicationContext.SCOPE_REQUEST, WebApplicationContext.SCOPE_SESSION
@Scope( value = WebApplicationContext.SCOPE_SESSION )
@Named( value = "clientSchedulingMB" )
public class ClientSchedulingMB extends BaseBeans {

	private static final long serialVersionUID = 201404221641L;

	Logger logger = Logger.getLogger( ClientSchedulingMB.class );

	@Inject
	private ISchedulingRepository schedulingRepository;

	private List<SchedulingEntity> schedulings;

	private Integer clientId;

	public ClientSchedulingMB() {
		logger.info( "ping" );
	}

	public void onLoad() {
		Calendar c = Calendar.getInstance();
		c.set( Calendar.HOUR_OF_DAY, 0 );
		c.set( Calendar.MINUTE, 0 );
		c.set( Calendar.SECOND, 0 );
		if ( getClientId() != null )
			schedulings = schedulingRepository.findFuturesByClient( getClientId().longValue(), c.getTime() );
	}

	public List<SchedulingEntity> getSchedulings() {
		return schedulings;
	}

	public void setSchedulings( List<SchedulingEntity> schedulings ) {
		this.schedulings = schedulings;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId( Integer clientId ) {
		this.clientId = clientId;
	}

}
