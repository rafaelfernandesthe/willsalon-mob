package br.com.doutorti.willsalon.support.clientWalletReport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClientWalletReportVO {

	private Long id;

	private String name;

	private String phone;

	private Date dtUltimoAgendamento;

	private Integer qtd;

	public static List<ClientWalletReportVO> transform(List<Object[]> in) {
		List<ClientWalletReportVO> result = new ArrayList<>();
		for (Object[] item : in) {
			result.add(new ClientWalletReportVO(Long.parseLong(String.valueOf(item[0])), String.valueOf(item[1]),
					String.valueOf(item[2]), (Date) item[3], Integer.parseInt(String.valueOf(item[4]))));
		}
		return result;
	}

	public ClientWalletReportVO(Long id, String name, String phone, Date dtUltimoAgendamento, Integer qtd) {
		this.id = id;
		this.name = name;
		this.phone = phone;
		this.dtUltimoAgendamento = dtUltimoAgendamento;
		this.qtd = qtd;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getQtd() {
		return qtd;
	}

	public void setQtd(Integer qtd) {
		this.qtd = qtd;
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

	public Date getDtUltimoAgendamento() {
		return dtUltimoAgendamento;
	}

	public void setDtUltimoAgendamento(Date dtUltimoAgendamento) {
		this.dtUltimoAgendamento = dtUltimoAgendamento;
	}

}
