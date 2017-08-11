package br.com.doutorti.willsalon.model;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import br.com.doutorti.willsalon.model.enuns.SexOrientation;

@Entity
@Table(name = "client")
@AttributeOverride(name = "id", column = @Column(name = "pk_id_client"))
public class ClientEntity extends PersonEntity {

	private static final long serialVersionUID = 967417329722509242L;

	private SexOrientation sexOrientation;

	public ClientEntity() {
	}

	public SexOrientation getSexOrientation() {
		return sexOrientation;
	}

	public void setSexOrientation(SexOrientation sexOrientation) {
		this.sexOrientation = sexOrientation;
	}

	@Override
	public String toString() {
		return String.format("%s, %s", getName(), getBirthDateFormat());
	}

}
