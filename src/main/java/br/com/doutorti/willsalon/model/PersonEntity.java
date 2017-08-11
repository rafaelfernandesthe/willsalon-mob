package br.com.doutorti.willsalon.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import br.com.doutorti.willsalon.model.utils.BaseEntities;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "person")
@AttributeOverride(name = "id", column = @Column(name = "pk_id_person"))
public class PersonEntity extends BaseEntities<Long> {

	private static final long serialVersionUID = 2636492369937197428L;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String phone;

	private String mail;

	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date birthDate;

	@Transient
	private String birthDateFormat;

	private String address;

	public PersonEntity() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBirthDateFormat() {
		if (birthDate != null)
			return new SimpleDateFormat("dd/MM/yyyy").format(birthDate);
		return null;
	}

}
