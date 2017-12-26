package br.com.doutorti.willsalon.model;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.doutorti.willsalon.model.utils.BaseEntities;
import br.com.doutorti.willsalon.model.utils.DateHourUtils;

@Entity
@Table(name = "holiday")
@AttributeOverride(name = "id", column = @Column(name = "pk_id_holiday"))
public class HolidayEntity extends BaseEntities<Long> {

	private static final long serialVersionUID = 11644467341461842L;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date initialDate;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date finalDate;

	public HolidayEntity() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getInitialDate() {
		return initialDate;
	}

	public void setInitialDate(Date initialDate) {
		this.initialDate = initialDate;
	}

	public Date getFinalDate() {
		return finalDate;
	}

	public void setFinalDate(Date finalDate) {
		this.finalDate = finalDate;
	}

	public String getDateFormat() {
		if (initialDate != null)
			return DateHourUtils.ddMMyyyy.format(initialDate);
		return null;
	}

	@Override
	public String toString() {
		return name + " - " + (initialDate != null ? DateHourUtils.ddMMyyyy.format(initialDate) : " ");
	}

}
