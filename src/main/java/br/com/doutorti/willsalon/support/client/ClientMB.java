package br.com.doutorti.willsalon.support.client;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;

import br.com.doutorti.willsalon.model.ClientEntity;
import br.com.doutorti.willsalon.model.repositories.IClientRepository;
import br.com.doutorti.willsalon.model.utils.BaseBeans;

// ConfigurableBeanFactory.SCOPE_SINGLETON, ConfigurableBeanFactory.SCOPE_PROTOTYPE,
// WebApplicationContext.SCOPE_REQUEST, WebApplicationContext.SCOPE_SESSION
//@Scope(value = WebApplicationContext.SCOPE_SESSION)
@Scope( "view" )
@Named( value = "clientMB" )
public class ClientMB extends BaseBeans {

	private static final long serialVersionUID = 201404221641L;

	Logger logger = Logger.getLogger( ClientMB.class );

	@Inject
	private IClientRepository clientRepository;

	private List<ClientEntity> clients;

	private Long id;

	private Long idToDelete;

	private String filterName;

	public ClientMB() {
		logger.info( "ping" );
	}

	public void filterCliente() {
		this.clients = this.clientRepository.findByNameContaining( filterName );
	}

	public void onLoad() {
		// this.clients = this.clientRepository.findAllByOrderByNameAsc();
	}

	public List<ClientEntity> getClients() {
		return this.clients;
	}

	public Long getId() {
		return this.id;
	}

	public void setId( Long id ) {
		this.id = id;
	}

	public void delete() {
		if ( getIdToDelete() != null ) {
			this.clientRepository.delete( getIdToDelete() );
		}
		filterCliente();
	}

	public Long getIdToDelete() {
		return idToDelete;
	}

	public void setIdToDelete( Long idToDelete ) {
		this.idToDelete = idToDelete;
	}

	public String getFilterName() {
		return filterName;
	}

	public void setFilterName( String filterName ) {
		this.filterName = filterName;
	}

}
