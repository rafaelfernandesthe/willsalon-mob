package br.com.doutorti.willsalon.support.clientWalletReport;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;

import br.com.doutorti.willsalon.model.EmployeeEntity;
import br.com.doutorti.willsalon.model.enuns.AbsenceTime;
import br.com.doutorti.willsalon.model.repositories.IClientRepository;
import br.com.doutorti.willsalon.model.repositories.IEmployeeRepository;
import br.com.doutorti.willsalon.model.utils.BaseBeans;

@Scope("view")
@Named(value = "clientWalletReportMB")
public class ClientWalletReportMB extends BaseBeans {

	private static final long serialVersionUID = -8716744236556383338L;

	Logger logger = Logger.getLogger(ClientWalletReportMB.class);

	private List<ClientWalletReportVO> clientHistories;

	private Long id;

	private EmployeeEntity employeeFilter;

	private boolean showOneTime;

	@Inject
	private IEmployeeRepository employeeRepository;

	@Inject
	private IClientRepository clientRepository;

	public ClientWalletReportMB() {
		employeeFilter = null;
		showOneTime = true;
		clientHistories = new ArrayList<>();
	}

	@PostConstruct
	public void onLoad() {
		// nothing
	}

	public void find() {

		if (employeeFilter != null && employeeFilter.getId() != null) {
			List<Object[]> result = clientRepository.findClientWalletReport(employeeFilter.getId());
			clientHistories = ClientWalletReportVO.transform(result);

			if (showOneTime) {
				result = clientRepository.findClientWalletReportOneTime(employeeFilter.getId());
				clientHistories.addAll(ClientWalletReportVO.transform(result));
			}
		}

	}

	public AbsenceTime[] getAbsenceTimeList() {
		return AbsenceTime.values();
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<EmployeeEntity> autocompleteEmployee(String query) {
		return employeeRepository.findByNameContaining(query);
	}

	public EmployeeEntity getEmployeeFilter() {
		return employeeFilter;
	}

	public void setEmployeeFilter(EmployeeEntity employeeFilter) {
		this.employeeFilter = employeeFilter;
	}

	public List<ClientWalletReportVO> getClientHistories() {
		return clientHistories;
	}

	public void setClientHistories(List<ClientWalletReportVO> clientHistories) {
		this.clientHistories = clientHistories;
	}

	public boolean isShowOneTime() {
		return showOneTime;
	}

	public void setShowOneTime(boolean showOneTime) {
		this.showOneTime = showOneTime;
	}

}
