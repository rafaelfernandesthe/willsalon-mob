package br.com.doutorti.willsalon.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import br.com.doutorti.willsalon.model.utils.BaseEntities;

@Entity
@Table(name = "scheduling")
@AttributeOverride(name = "id", column = @Column(name = "pk_id_scheduling"))
public class SchedulingEntity extends BaseEntities<Long> {

	private static final long serialVersionUID = -959225768541182545L;

	@JoinColumn(name = "fk_id_client")
	@ManyToOne(optional = false)
	private ClientEntity client;

	@JoinColumn(name = "fk_id_employee")
	@ManyToOne(optional = false)
	private EmployeeEntity employee;

	@ManyToMany(fetch = FetchType.EAGER)
	private List<ProcedureEntity> procedureList;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date initialDate;

	@Column(nullable = false, columnDefinition = "tinyint(1) default 0")
	private Boolean finished;

	@Column(nullable = false, columnDefinition = "tinyint(1) default 0")
	private Boolean wasRepetition;

	@Transient
	private Date finalDatePrevision;

	private String history;

	public SchedulingEntity() {
	}

	public ClientEntity getClient() {
		return client;
	}

	public void setClient(ClientEntity client) {
		this.client = client;
	}

	public EmployeeEntity getEmployee() {
		return employee;
	}

	public void setEmployee(EmployeeEntity employee) {
		this.employee = employee;
	}

	public List<ProcedureEntity> getProcedureList() {
		if (procedureList == null) {
			procedureList = new ArrayList<ProcedureEntity>();
		}
		return procedureList;
	}

	public void setProcedureList(List<ProcedureEntity> procedureList) {
		this.procedureList = procedureList;
	}

	public Date getInitialDate() {
		return initialDate;
	}

	public void setInitialDate(Date initialDate) {
		this.initialDate = initialDate;
	}

	public Boolean getFinished() {
		return finished;
	}

	public void setFinished(Boolean finished) {
		this.finished = finished;
	}

	public Date getFinalDatePrevision() {
		if (getInitialDate() != null && getProcedureList() != null
				&& !getProcedureList().isEmpty()) {
			Calendar c = Calendar.getInstance();
			c.setTime(getInitialDate());
			for (ProcedureEntity proc : getProcedureList()) {
				c.add(Calendar.MINUTE, proc.getMinutesPrevision());
			}
			return c.getTime();
		}
		return null;
	}

	public void setFinalDatePrevision(Date finalDatePrevision) {
		this.finalDatePrevision = finalDatePrevision;
	}

	public String getInitialDateFormatWithDayAndHours() {
		if (getInitialDate() != null)
			return new SimpleDateFormat("dd/MM/yy HH:mm")
					.format(getInitialDate());
		return "";
	}

	public String getInitialDateFormatWithoutDay() {
		if (getInitialDate() != null)
			return new SimpleDateFormat("HH:mm").format(getInitialDate());
		return "";
	}

	public String getInitialDateFormatWithoutHours() {
		if (getInitialDate() != null)
			return new SimpleDateFormat("dd/MM/yy").format(getInitialDate());
		return "";
	}

	public String getFinalDatePrevisionFormatWithoutDay() {
		if (getFinalDatePrevision() != null)
			return new SimpleDateFormat("HH:mm")
					.format(getFinalDatePrevision());
		return "";
	}

	public String getFinalDateFormatWithDayAndHours() {
		if (getFinalDatePrevision() != null)
			return new SimpleDateFormat("dd/MM/yy HH:mm")
					.format(getFinalDatePrevision());
		return "";
	}

	public Boolean getWasRepetition() {
		return wasRepetition;
	}

	public void setWasRepetition(Boolean wasRepetition) {
		this.wasRepetition = wasRepetition;
	}

	public String getHistory() {
		return history;
	}

	public void setHistory(String history) {
		this.history = history;
	}

	@Override
	public String toString() {
		return "\nCliente:"
				+ (getClient() != null ? getClient().getName() : "")
				+ "\nAgendamento: " + getInitialDateFormatWithDayAndHours()
				+ " Até " + getFinalDateFormatWithDayAndHours()
				+ "\nServiço(s): " + getProcedureList().toString();
	}

}
