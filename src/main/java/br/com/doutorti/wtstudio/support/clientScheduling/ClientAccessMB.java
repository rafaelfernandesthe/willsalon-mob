package br.com.doutorti.wtstudio.support.clientScheduling;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;

import br.com.doutorti.wtstudio.model.ClientEntity;
import br.com.doutorti.wtstudio.model.repositories.IClientRepository;
import br.com.doutorti.wtstudio.model.utils.BaseBeans;

//ConfigurableBeanFactory.SCOPE_SINGLETON, ConfigurableBeanFactory.SCOPE_PROTOTYPE,
//WebApplicationContext.SCOPE_REQUEST, WebApplicationContext.SCOPE_SESSION
@Scope("view")
@Named(value = "clientAccessMB")
public class ClientAccessMB extends BaseBeans {
	
	private static final long serialVersionUID = 201311132355L;

	@Inject
	private FacesContext context;

	@Inject
	private IClientRepository clientRepository;
	
	private ClientEntity client;

	private String title;

	private String contextPhone;

	private String contextBirthDate;

	private ClientEntity loadedClient;

	public ClientAccessMB() {
		this.client = new ClientEntity();
	}

	@PostConstruct
	public void init() {
	}
	
	private void verifyClient() {
		if (client.getPhone() != null && client.getBirthDate() != null) {
			setLoadedClient(clientRepository.findByPhoneContainingAndBirthDate(
					client.getPhone(), client.getBirthDate()));

			try {
				if (loadedClient != null) {
					context.getExternalContext().redirect(
							"addScheduling.faces?clientId="
									+ loadedClient.getId());
				} else {
					context.getExternalContext().redirect(
							"newClient.faces?phone=" + client.getPhone()
									+ "&birthDate="
									+ client.getBirthDateFormat()
									+"&clientView=true");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void add() {
		this.title = this.getResourceProperty("labels", "button_add");
	}

	public void save() {

	}

	public String getTitle() {
		if (client.getId() == null) {
			this.title = this.getResourceProperty("labels", "button_add");
		}
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	private String getResourceProperty(String resource, String label) {
		Application application = this.context.getApplication();
		ResourceBundle bundle = application.getResourceBundle(this.context,
				resource);

		return bundle.getString(label);
	}

	public ClientEntity getClient() {
		return client;
	}

	public void setClient(ClientEntity client) {
		this.client = client;
	}

	public String getContextPhone() {
		return contextPhone;
	}

	public void setContextPhone(String contextPhone) {

		if (contextPhone != null && !contextPhone.isEmpty()) {
			contextPhone = contextPhone.replaceAll("\\(", "")
					.replaceAll("\\)", "").replaceAll("-", "")
					.replaceAll(" ", "").trim();
			contextPhone = contextPhone.length() <= 9 ? contextPhone
					: contextPhone.substring(2);
			client.setPhone(contextPhone);
		}

		this.contextPhone = contextPhone;

		verifyClient();
	}

	public String getContextBirthDate() {
		return contextBirthDate;
	}

	public void setContextBirthDate(String contextBirthDate) {

		if (contextBirthDate != null && !contextBirthDate.isEmpty()) {
			try {
				client.setBirthDate(new SimpleDateFormat("dd/MM/yyyy")
						.parse(contextBirthDate.trim()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		this.contextBirthDate = contextBirthDate;

		verifyClient();
	}

	public ClientEntity getLoadedClient() {
		return loadedClient;
	}

	public void setLoadedClient(ClientEntity loadedClient) {
		this.loadedClient = loadedClient;
	}

}
