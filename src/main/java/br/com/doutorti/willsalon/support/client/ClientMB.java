package br.com.doutorti.willsalon.support.client;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.springframework.context.annotation.Scope;

import br.com.doutorti.willsalon.model.ClientEntity;
import br.com.doutorti.willsalon.model.SchedulingEntity;
import br.com.doutorti.willsalon.model.repositories.IClientRepository;
import br.com.doutorti.willsalon.model.repositories.ISchedulingRepository;
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

	@Inject
	private ISchedulingRepository schedulingRepository;

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
			ClientEntity loadedClient = clientRepository.findOne( getIdToDelete() );
			Long foundClient = clientRepository.findClientWithSamePhoneAndBirthDate( loadedClient.getPhone(), loadedClient.getBirthDate(), loadedClient.getId() );

			// transferindo os agendamentos para o novo cliente
			if ( foundClient != null ) {
				ClientEntity loadedFoundClient = clientRepository.findOne(foundClient);
				List<SchedulingEntity> resultSchedulings = this.schedulingRepository.findByClient( loadedClient.getId() );
				for ( SchedulingEntity s : resultSchedulings ) {
					s.setClient( loadedFoundClient );
				}
				schedulingRepository.save( resultSchedulings );
			}

			try{
				this.clientRepository.delete( getIdToDelete() );
			}catch (org.springframework.dao.DataIntegrityViolationException e){
				RequestContext.getCurrentInstance().execute( "alert('Não é possivel remover o Cliente pois ele possui agendamentos.')" );
			}
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
