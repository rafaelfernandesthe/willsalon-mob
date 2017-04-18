package br.com.doutorti.wtstudio.model.enuns;

public enum SexOrientation {

	MASC( "Masculino" ),
	FEM( "Feminino" );

	String description;

	private SexOrientation( String description ) {
		this.description = description;
	}

	@Override
	public String toString() {
		return description;
	}

}
