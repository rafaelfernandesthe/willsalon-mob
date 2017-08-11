package br.com.doutorti.willsalon.model;

import java.math.BigDecimal;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import br.com.doutorti.willsalon.model.utils.BaseEntities;

@Entity
@Table(name = "procedure_e")
@AttributeOverride(name = "id", column = @Column(name = "pk_id_procedure_e"))
public class ProcedureEntity extends BaseEntities<Long> {

	private static final long serialVersionUID = -6140896863312846495L;

	@Column(unique = true)
	private String name;

	private Integer minutesPrevision;

	private BigDecimal value;

	private Boolean active;
	
	private Boolean administrative;

	public ProcedureEntity() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getMinutesPrevision() {
		return minutesPrevision;
	}

	public void setMinutesPrevision(Integer minutesPrevision) {
		this.minutesPrevision = minutesPrevision;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return getName();
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean getAdministrative() {
		return administrative;
	}

	public void setAdministrative(Boolean administrative) {
		this.administrative = administrative;
	}

}
