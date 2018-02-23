package br.com.doutorti.willsalon.model.enuns;

public enum AbsenceTime {

	_0( "Nunca Agendou" ),
	_15_30( "entre 15 e 30 Dias" ),
	_30_60( "entre 30 e 60 Dias" ),
	_60_90( "entre 60 e 90 Dias" ),
	_90_N( "mais de 90 Dias" ),

	;

	String description;

	private AbsenceTime( String description ) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

}
