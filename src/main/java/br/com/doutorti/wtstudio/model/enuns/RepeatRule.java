package br.com.doutorti.wtstudio.model.enuns;

public enum RepeatRule {

	SEM_1(7), SEM_2(14), SEM_3(21), SEM_4(28);

	public Integer days;

	private RepeatRule(Integer days) {
		this.days = days;
	}

}
