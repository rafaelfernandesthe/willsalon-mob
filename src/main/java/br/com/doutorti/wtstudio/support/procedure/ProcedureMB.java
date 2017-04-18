package br.com.doutorti.wtstudio.support.procedure;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

import br.com.doutorti.wtstudio.model.ProcedureEntity;
import br.com.doutorti.wtstudio.model.repositories.IProcedureRepository;
import br.com.doutorti.wtstudio.model.utils.BaseBeans;

// ConfigurableBeanFactory.SCOPE_SINGLETON, ConfigurableBeanFactory.SCOPE_PROTOTYPE,
// WebApplicationContext.SCOPE_REQUEST, WebApplicationContext.SCOPE_SESSION
@Scope(value = WebApplicationContext.SCOPE_SESSION)
@Named(value = "procedureMB")
public class ProcedureMB extends BaseBeans {

	private static final long serialVersionUID = 201404221641L;

	private static final Logger logger = Logger.getLogger(ProcedureMB.class);

	@Inject
	private IProcedureRepository procedureRepository;

	private List<ProcedureEntity> procedures;

	private Long id;

	private Long idToDelete;

	public ProcedureMB() {
	}

	public void onLoad() {
		this.procedures = this.procedureRepository.findAll();
	}

	public List<ProcedureEntity> getProcedures() {
		return this.procedures;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void delete() {
		if (this.idToDelete != null) {
			this.procedureRepository.delete(idToDelete);
		}
	}

	public Long getIdToDelete() {
		return idToDelete;
	}

	public void setIdToDelete(Long idToDelete) {
		this.idToDelete = idToDelete;
	}

	public void desactive(Long idToDesactive) {
		ProcedureEntity loaded = this.procedureRepository
				.findOne(idToDesactive);
		loaded.setActive(loaded.getActive() != null ? !loaded.getActive()
				: true);
		this.procedureRepository.save(loaded);
	}

}
