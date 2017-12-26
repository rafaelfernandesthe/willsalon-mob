package br.com.doutorti.willsalon.support.holiday;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

import br.com.doutorti.willsalon.model.HolidayEntity;
import br.com.doutorti.willsalon.model.repositories.IHolidayRepository;
import br.com.doutorti.willsalon.model.utils.BaseBeans;
import br.com.doutorti.willsalon.model.utils.DateHourUtils;

//ConfigurableBeanFactory.SCOPE_SINGLETON, ConfigurableBeanFactory.SCOPE_PROTOTYPE,
//WebApplicationContext.SCOPE_REQUEST, WebApplicationContext.SCOPE_SESSION
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@Named(value = "holidayAddEditMB")
public class HolidayAddEditMB extends BaseBeans {

	private static final long serialVersionUID = -7885745816896496959L;

	Logger logger = Logger.getLogger(HolidayAddEditMB.class);

	@Inject
	private IHolidayRepository holidayRepository;

	@Inject
	private FacesContext context;

	private HolidayEntity holiday;

	private String title;

	public HolidayAddEditMB() {
		this.holiday = new HolidayEntity();
	}

	public void add() {
		this.title = this.getResourceProperty("labels", "button_add");
	}

	public void update(Long id) {
		this.holiday = holidayRepository.findOne(id);
		this.title = this.getResourceProperty("labels", "button_update");
	}
	
	public void renewHoliday() {
		this.title = this.getResourceProperty("labels", "button_renewHoliday");
	}

	public void save() {
		if (this.holiday != null) {
			holiday.setInitialDate(DateHourUtils.getStartOfDay(holiday.getInitialDate()));
			holiday.setFinalDate(DateHourUtils.getEndOfDay(holiday.getInitialDate()));
			
			if (this.holiday.getId() == null) {
				this.holidayRepository.save(this.holiday);
				try {
					FacesContext.getCurrentInstance().getExternalContext().redirect("list.faces");
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				// Update
				this.holidayRepository.save(this.holiday);
			}
		}
	}

	public String getTitle() {
		if (holiday.getId() == null) {
			this.title = this.getResourceProperty("labels", "button_add");
		}
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	private String getResourceProperty(String resource, String label) {
		Application application = this.context.getApplication();
		ResourceBundle bundle = application.getResourceBundle(this.context, resource);

		return bundle.getString(label);
	}

	public HolidayEntity getHoliday() {
		return holiday;
	}

	public void setHoliday(HolidayEntity holiday) {
		this.holiday = holiday;
	}

}
