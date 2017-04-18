package br.com.doutorti.wtstudio.model;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "employee")
@AttributeOverride(name = "id", column = @Column(name = "pk_id_employee"))
public class EmployeeEntity extends PersonEntity {

	private static final long serialVersionUID = -5436015730946736361L;

	@Column(nullable = false)
	private String cpf;

	@Column(nullable = false)
	private String rg;

	@Column(nullable = false)
	private Boolean meetByOrder;

	public EmployeeEntity() {
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public Boolean getMeetByOrder() {
		return meetByOrder;
	}

	public void setMeetByOrder(Boolean meetByOrder) {
		this.meetByOrder = meetByOrder;
	}

	@Override
	public String toString() {
		return getName();
	}

}
