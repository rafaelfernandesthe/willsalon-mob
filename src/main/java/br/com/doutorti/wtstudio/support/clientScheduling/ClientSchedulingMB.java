package br.com.doutorti.wtstudio.support.clientScheduling;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

import br.com.doutorti.wtstudio.model.SchedulingEntity;
import br.com.doutorti.wtstudio.model.repositories.ISchedulingRepository;
import br.com.doutorti.wtstudio.model.utils.BaseBeans;

// ConfigurableBeanFactory.SCOPE_SINGLETON, ConfigurableBeanFactory.SCOPE_PROTOTYPE,
// WebApplicationContext.SCOPE_REQUEST, WebApplicationContext.SCOPE_SESSION
@Scope(value = WebApplicationContext.SCOPE_SESSION)
@Named(value = "clientSchedulingMB")
public class ClientSchedulingMB extends BaseBeans {

	private static final long serialVersionUID = 201404221641L;

	private static final Logger logger = Logger
			.getLogger(ClientSchedulingMB.class);

	@Inject
	private ISchedulingRepository schedulingRepository;

	private List<SchedulingEntity> schedulings;

	private Integer clientId;

	public ClientSchedulingMB() {

	}

	public void onLoad() {
		if (getClientId() != null)
			schedulings = schedulingRepository.findByClient(getClientId()
					.longValue());
	}

	public List<SchedulingEntity> getSchedulings() {
		return schedulings;
	}

	public void setSchedulings(List<SchedulingEntity> schedulings) {
		this.schedulings = schedulings;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

}
