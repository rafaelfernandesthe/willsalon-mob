package br.com.doutorti.wtstudio.support.scheduling;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.application.Application;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.doutorti.wtstudio.model.ClientEntity;
import br.com.doutorti.wtstudio.model.EmployeeEntity;
import br.com.doutorti.wtstudio.model.ProcedureEntity;
import br.com.doutorti.wtstudio.model.SchedulingEntity;
import br.com.doutorti.wtstudio.model.enuns.RepeatRule;
import br.com.doutorti.wtstudio.model.repositories.IClientRepository;
import br.com.doutorti.wtstudio.model.repositories.IEmployeeRepository;
import br.com.doutorti.wtstudio.model.repositories.IProcedureRepository;
import br.com.doutorti.wtstudio.model.repositories.ISchedulingRepository;
import br.com.doutorti.wtstudio.model.utils.BaseBeans;
import br.com.doutorti.wtstudio.model.utils.HourUtils;

//ConfigurableBeanFactory.SCOPE_SINGLETON, ConfigurableBeanFactory.SCOPE_PROTOTYPE,
//WebApplicationContext.SCOPE_REQUEST, WebApplicationContext.SCOPE_SESSION
@Scope("view")
@Named(value = "schedulingAddEditMB")
public class SchedulingAddEditMB extends BaseBeans {

	private static final long serialVersionUID = 201311132355L;

	@Inject
	private ISchedulingRepository schedulingRepository;

	@Inject
	private IClientRepository clientRepository;

	@Inject
	private IEmployeeRepository employeeRepository;

	@Inject
	private IProcedureRepository procedureRepository;

	@Inject
	private FacesContext context;

	private SchedulingEntity scheduling;

	private String title;

	private List<ProcedureEntity> selectedProcedureList;

	private String dateHour;

	private Boolean isShow;

	private List<String> dateHourList;

	private List<String> dateHourClosedList;

	private Boolean canFinish;

	private List<SchedulingEntity> schedulingResult;

	private String internalNotify;

	private RepeatRule repeatRule;

	private String alertMessage;

	private UISelectItems selectItensDateHourBinding;

	private List<SelectItem> selectItensDateHourList;

	private Boolean showButtonNewClient;

	private ClientEntity newClient;

	private int dayBirthDateClient;
	private int monthBirthDateClient;
	private int yearBirthDateClient;

	public SchedulingAddEditMB() {
		this.scheduling = new SchedulingEntity();
		this.newClient = new ClientEntity();
		this.scheduling.setInitialDate(new Date());
		canFinish = false;
		setInternalNotify(null);
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MINUTE, 0);
		if (c.get(Calendar.HOUR_OF_DAY) < 8) {
			c.set(Calendar.HOUR_OF_DAY, 8);
		}
		if (c.get(Calendar.HOUR_OF_DAY) > 18) {
			c.add(Calendar.DAY_OF_MONTH, 1);
			scheduling.setInitialDate(c.getTime());
			c.set(Calendar.HOUR_OF_DAY, 8);
		}
		isShow = false;
		dateHourClosedList = new ArrayList<String>();
		showButtonNewClient = false;
	}

	@PostConstruct
	public void init() {

	}

	public void preSave() {
		if (scheduling.getClient() == null && newClient != null) {
			scheduling.setClient(newClient);
		}
		scheduling.setProcedureList(null);
		for (Object p : getSelectedProcedureList()) {
			scheduling.getProcedureList().add(
					procedureRepository.findOne(new Long((String) p)));
		}
		if (scheduling.getInitialDate() != null && dateHour != null) {
			Calendar c1 = Calendar.getInstance();
			c1.setTime(scheduling.getInitialDate());
			c1.set(Calendar.HOUR_OF_DAY, 0);
			c1.set(Calendar.MINUTE, 0);
			c1.set(Calendar.SECOND, 0);
			Calendar c2 = Calendar.getInstance();
			Date dateHourF = new Date();
			try {
				dateHourF = new SimpleDateFormat("HH:mm").parse(dateHour);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			c2.setTime(dateHourF);
			c1.set(Calendar.HOUR_OF_DAY, c2.get(Calendar.HOUR_OF_DAY));
			c1.set(Calendar.MINUTE, c2.get(Calendar.MINUTE));
			scheduling.setInitialDate(c1.getTime());
		}
	}

	public void cleanFields() {
		scheduling.setInitialDate(new Date());
		scheduling.setProcedureList(getProcedureList());
		setDateHourList(new ArrayList<String>());
	}

	public void loadDateHourList(String accordionToOpen) {
		if (!getEmployeeCanSchedule())
			return;

		Calendar c = Calendar.getInstance();
		c.setTime(scheduling.getInitialDate());
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		Date dateI = c.getTime();
		c = Calendar.getInstance();
		c.setTime(scheduling.getInitialDate());
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		Date dateF = c.getTime();
		try{
		schedulingResult = schedulingRepository.findByDayAndEmployee(dateI,
				dateF, scheduling.getEmployee().getId());
		}catch(NullPointerException e){
			String script = ""
			+ " jQuery(document).ready(function(){"
			+ "	if(location.pathname.endsWith('pages/clientScheduling/addScheduling.faces')){"
			+ " $(window).scrollTop(0);"
			+ "	alert('Ops, ocorreu um erro. Você será direcionado para a tela inicial...');"
			+ " setTimeout(function(){location.href='http://104.236.67.194/admin'},2000)}});";
			RequestContext.getCurrentInstance().execute( script );
		}

		ArrayList<String> closedList = new ArrayList<String>();
		Calendar cTmp = Calendar.getInstance();
		Calendar cTmp2 = Calendar.getInstance();
		dateHourClosedList = new ArrayList<String>();
		for (SchedulingEntity e : schedulingResult) {
			cTmp.setTime(e.getInitialDate());
			closedList.add(HourUtils.getCorrectHourOrMinute(cTmp
					.get(Calendar.HOUR_OF_DAY))
					+ ":"
					+ HourUtils.getCorrectHourOrMinute(cTmp
							.get(Calendar.MINUTE)));
			String out = String.format("%s - %s até %s", e.getClient()
					.getName(), e.getInitialDateFormatWithoutDay(), e
					.getFinalDatePrevisionFormatWithoutDay());
			dateHourClosedList.add(out);

			cTmp2.setTime(e.getFinalDatePrevision());

			for (; cTmp.compareTo(cTmp2) < 0;) {
				cTmp.add(Calendar.MINUTE, 1);
				if (cTmp.compareTo(cTmp2) == 0)
					break;
				closedList.add(HourUtils.getCorrectHourOrMinute(cTmp
						.get(Calendar.HOUR_OF_DAY))
						+ ":"
						+ HourUtils.getCorrectHourOrMinute(cTmp
								.get(Calendar.MINUTE)));
			}

		}

		List<String> completeList = HourUtils.completeListHours();

		setDateHourList(completeList);
		selectItensDateHourList = new ArrayList<SelectItem>();
		for (String item : completeList) {
			SelectItem newItem = new SelectItem(item);
			if (closedList.contains(item)) {
				newItem.setDisabled(true);
				for (String sc1 : dateHourClosedList) {
					if (sc1.matches(".+ - " + item + " até \\d{2}:\\d{2}")) {
						newItem.setLabel(sc1);
					}
				}
			}
			selectItensDateHourList.add(newItem);
		}
		selectItensDateHourBinding.setValue(selectItensDateHourList);

		if (accordionToOpen.equals("null"))
			return;

		RequestContext.getCurrentInstance().execute(
				String.format("PF('accordion_1').select(%s)", accordionToOpen));
	}

	public List<ClientEntity> autocompleteClient(String query) {
		List<ClientEntity> result = clientRepository
				.findByNameContaining(query);
		if (result == null || result.isEmpty()) {
			setShowButtonNewClient(true);
		} else {
			setShowButtonNewClient(false);
		}
		RequestContext.getCurrentInstance().update(
				"formScheduling:accordion_1:buttonNewCliente");
		return result;
	}

	public List<EmployeeEntity> autocompleteEmployee(String query) {
		return employeeRepository.findByNameContaining(query);
	}

	public SchedulingEntity getScheduling() {
		return this.scheduling;
	}

	public void setScheduling(SchedulingEntity scheduling) {
		this.scheduling = scheduling;
	}

	public void add() {
		this.title = this.getResourceProperty("labels", "button_add");
	}

	public void update(Long id) {
		this.scheduling = this.schedulingRepository.findOne(id);
		this.title = this.getResourceProperty("labels", "button_update");
	}

	public void prepareFinalDateProvision() {
		setInternalNotify(null);
		canFinish = false;
		preSave();
		if (!scheduling.getProcedureList().isEmpty()) {
			if (schedulingResult == null || schedulingResult.isEmpty()) {
				canFinish = true;
			}
			scheduling.setInitialDate(HourUtils.zeroMilli(scheduling
					.getInitialDate()));
			scheduling.setFinalDatePrevision(HourUtils.zeroMilli(scheduling
					.getFinalDatePrevision()));
			for (SchedulingEntity scheduled : schedulingResult) {
				scheduled.setInitialDate(HourUtils.zeroMilli(scheduled
						.getInitialDate()));
				if (scheduling.getInitialDate().before(
						scheduled.getInitialDate())
						&& scheduling.getFinalDatePrevision().after(
								scheduled.getInitialDate())) {
					setInternalNotify("Não existe tempo suficiente para finalizar todos os serviços desse agendamento, tente outro horário.");
					canFinish = false;
					RequestContext.getCurrentInstance().execute(
							"PF('dialog_erro').show()");
					RequestContext.getCurrentInstance().update(
							"formScheduling:dialog_erro");
					return;
				} else {
					canFinish = true;
				}
			}
			RequestContext.getCurrentInstance().execute(
					"PF('accordion_1').select(3)");
		}
	}

	public void save() throws IOException {
		try{
		preSave();
		if (this.scheduling != null) {
			scheduling.setFinished(false);
			// Add

			Map<SchedulingEntity, Boolean> mapToSave = new HashMap<SchedulingEntity, Boolean>();

			mapToSave.put(this.scheduling, true);

			if (repeatRule == null) {
				scheduling.setWasRepetition(false);
				scheduling.setHistory(getHistory());
				this.schedulingRepository.save(scheduling);
			}

			if (repeatRule != null) {
				for (int i = 1; true; i++) {
					boolean canSave = true;
					SchedulingEntity newScheduling = new SchedulingEntity();
					BeanUtils.copyProperties(scheduling, newScheduling, "id");
					newScheduling
							.setProcedureList(new ArrayList<ProcedureEntity>(
									newScheduling.getProcedureList()));
					Calendar c = Calendar.getInstance();
					c.setTime(newScheduling.getInitialDate());
					c.add(Calendar.DAY_OF_MONTH, repeatRule.days * i);
					// agendar repetições apenas para o ano corrente
					if (c.get(Calendar.YEAR) > Calendar.getInstance().get(
							Calendar.YEAR)) {
						break;
					}
					newScheduling.setInitialDate(c.getTime());

					List<SchedulingEntity> internalSchedulingResult = schedulingRepository
							.findByDayAndEmployee(
									getCorrectHourDay(
											newScheduling.getInitialDate(),
											true),
									getCorrectHourDay(newScheduling
											.getFinalDatePrevision(), false),
									newScheduling.getEmployee().getId());

					for (SchedulingEntity scheduled : internalSchedulingResult) {
						if (newScheduling.getInitialDate().before(
								scheduled.getInitialDate())
								&& newScheduling.getFinalDatePrevision().after(
										scheduled.getInitialDate())) {
							canSave = false;
							break;
						}
					}

					mapToSave.put(newScheduling, canSave);
				}

				List<SchedulingEntity> schedulingListToSave = new ArrayList<SchedulingEntity>();
				List<SchedulingEntity> blackList = new ArrayList<SchedulingEntity>();
				for (SchedulingEntity entity : mapToSave.keySet()) {
					entity.setWasRepetition(true);
					if (mapToSave.get(entity)) {
						entity.setHistory(getHistory());
						schedulingListToSave.add(entity);
					} else {
						blackList.add(entity);
					}
				}

				this.schedulingRepository.save(schedulingListToSave);

				if (!blackList.isEmpty()) {
					List<String> hours = new ArrayList<String>();
					for (SchedulingEntity item : blackList) {
						hours.add(item.getInitialDateFormatWithDayAndHours());
					}
					alertMessage = hours.toString();
					RequestContext.getCurrentInstance().execute(
							"PF('dialog_conflit').show()");
					RequestContext.getCurrentInstance().update(
							"formScheduling:dialog_conflit");
				} else {
					FacesContext.getCurrentInstance().getExternalContext()
							.redirect("list.faces");
				}
			}
		}
		RequestContext.getCurrentInstance()
				.execute("PF('dialog_saved').show()");
		}catch (Exception e){
			String script = ""
			+ " jQuery(document).ready(function(){"
			+ "	if(location.pathname.endsWith('pages/clientScheduling/addScheduling.faces')){"
			+ " $(window).scrollTop(0);"
			+ "	alert('Ops, ocorreu um erro. Você será direcionado para a tela inicial...');"
			+ " setTimeout(function(){location.href='http://104.236.67.194/admin'},2000)}});";
			RequestContext.getCurrentInstance().execute( script );
		}
	}

	public void addNewClient() {
		if (newClient != null) {
			Calendar birthDate = Calendar.getInstance();
			birthDate.set(Calendar.DAY_OF_MONTH, dayBirthDateClient);
			birthDate.set(Calendar.MONDAY, monthBirthDateClient - 1);
			birthDate.set(Calendar.YEAR, yearBirthDateClient);
			newClient.setBirthDate(birthDate.getTime());
			newClient.setPhone(newClient.getPhone().replaceAll("\\(", "")
					.replaceAll("\\)", "").replaceAll("-", "")
					.replaceAll(" ", "").trim());
			this.clientRepository.save(this.newClient);
			scheduling.setClient(newClient);
			RequestContext.getCurrentInstance().execute(
					"PF('dialog_newClient').hide()");
			setShowButtonNewClient(false);
			RequestContext.getCurrentInstance().update(
					"formScheduling:accordion_1:buttonNewCliente");
		}
	}

	private String getHistory() {
		String loggedUser = SecurityContextHolder.getContext()
				.getAuthentication().getName();
		String actualDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
				.format(new Date());
		return String.format("Operação realizada por %s às %s", loggedUser,
				actualDate);
	}

	public Date getCorrectHourDay(Date date, boolean init) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, init ? 0 : 23);
		c.set(Calendar.MINUTE, init ? 0 : 59);
		c.set(Calendar.SECOND, init ? 0 : 59);
		return c.getTime();
	}

	public Boolean getEmployeeCanSchedule() {
		return getScheduling().getEmployee() == null
				|| (getScheduling().getEmployee() != null && !getScheduling()
						.getEmployee().getMeetByOrder());
	}

	public String getTitle() {
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

	public List<ProcedureEntity> getProcedureList() {
		return procedureRepository.findAllActivies();
	}

	public String getDateHour() {
		return dateHour;
	}

	public List<String> getDateHourList() {
		if (dateHourList == null) {
			dateHourList = new ArrayList<String>();
		}
		return dateHourList;
	}

	public void setDateHour(String dateHour) {
		this.dateHour = dateHour;
	}

	public String getMaxDate() {
		return new SimpleDateFormat("dd/MM/yyyy").format(new Date());
	}

	public List<ProcedureEntity> getSelectedProcedureList() {
		if (selectedProcedureList == null) {
			selectedProcedureList = new ArrayList<ProcedureEntity>();
		}
		return selectedProcedureList;
	}

	public void setSelectedProcedureList(
			List<ProcedureEntity> selectedProcedureList) {
		this.selectedProcedureList = selectedProcedureList;
	}

	public Boolean getIsShow() {
		return isShow;
	}

	public void setIsShow(Boolean isShow) {
		this.isShow = isShow;
	}

	public void setDateHourList(List<String> dateHourList) {
		this.dateHourList = dateHourList;
	}

	public List<String> getDateHourClosedList() {
		return dateHourClosedList;
	}

	public void setDateHourClosedList(List<String> dateHourClosedList) {
		this.dateHourClosedList = dateHourClosedList;
	}

	public Boolean getCanFinish() {
		return canFinish;
	}

	public void setCanFinish(Boolean canFinish) {
		this.canFinish = canFinish;
	}

	public String getInternalNotify() {
		return internalNotify;
	}

	public void setInternalNotify(String internalNotify) {
		this.internalNotify = internalNotify;
	}

	public RepeatRule getRepeatRule() {
		return repeatRule;
	}

	public void setRepeatRule(RepeatRule repeatRule) {
		this.repeatRule = repeatRule;
	}

	public String getAlertMessage() {
		return alertMessage;
	}

	public void setAlertMessage(String alertMessage) {
		this.alertMessage = alertMessage;
	}

	public List<SelectItem> getSelectItensDateHourList() {
		return selectItensDateHourList;
	}

	public void setSelectItensDateHourList(
			List<SelectItem> selectItensDateHourList) {
		this.selectItensDateHourList = selectItensDateHourList;
	}

	public UISelectItems getSelectItensDateHourBinding() {
		return selectItensDateHourBinding;
	}

	public void setSelectItensDateHourBinding(
			UISelectItems selectItensDateHourBinding) {
		this.selectItensDateHourBinding = selectItensDateHourBinding;
	}

	public Boolean getShowButtonNewClient() {
		return showButtonNewClient;
	}

	public void setShowButtonNewClient(Boolean showButtonNewClient) {
		this.showButtonNewClient = showButtonNewClient;
	}

	public ClientEntity getNewClient() {
		return newClient;
	}

	public void setNewClient(ClientEntity newClient) {
		this.newClient = newClient;
	}

	public int getDayBirthDateClient() {
		return dayBirthDateClient;
	}

	public void setDayBirthDateClient(int dayBirthDateClient) {
		this.dayBirthDateClient = dayBirthDateClient;
	}

	public int getMonthBirthDateClient() {
		return monthBirthDateClient;
	}

	public void setMonthBirthDateClient(int monthBirthDateClient) {
		this.monthBirthDateClient = monthBirthDateClient;
	}

	public int getYearBirthDateClient() {
		return yearBirthDateClient;
	}

	public void setYearBirthDateClient(int yeahBirthDateClient) {
		this.yearBirthDateClient = yeahBirthDateClient;
	}

	public List<Integer> getDayList() {
		List<Integer> result = new ArrayList<Integer>();
		for (int i = 1; i <= 31; i++)
			result.add(i);
		return result;
	}

	public List<Integer> getMonthList() {
		List<Integer> result = new ArrayList<Integer>();
		for (int i = 1; i <= 12; i++)
			result.add(i);
		return result;
	}

	public List<Integer> getYearList() {
		List<Integer> result = new ArrayList<Integer>();
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		for (int i = currentYear - 80; i <= currentYear; i++)
			result.add(i);
		return result;
	}
}
