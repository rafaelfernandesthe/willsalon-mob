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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((active == null) ? 0 : active.hashCode());
		result = prime * result + ((administrative == null) ? 0 : administrative.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		ProcedureEntity other = (ProcedureEntity) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}

}
