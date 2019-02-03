package org.ec.jap.backend.sistema;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.ec.jap.backend.pagina.Bean;
import org.ec.jap.backend.utilitario.Mensaje;
import org.ec.jap.bo.sistema.PerfilBO;
import org.ec.jap.entiti.sistema.Perfil;
import org.ec.jap.enumerations.PerfilEstado;

@ManagedBean
@ViewScoped
public class PerfilEditBean extends Bean {

	/**
	 * 
	 */

	Perfil perfil;

	@EJB
	PerfilBO perfilBO;

	public PerfilEditBean() {
		super();
	}

	@PostConstruct
	@Override
	public void init() {
		try {
			super.init();
			search(null);
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}

	}

	@Override
	public void search(ActionEvent event) {
		try {
			if ("INS".equals(getAccion())) {
				perfil = new Perfil();
			} else {
				perfil = perfilBO.findByPk(getParam2Integer());
			}

		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	@Override
	public String delete() {
		try {
			perfilBO.existenOpcionesAsignados(perfil);
			perfilBO.existenUsuariosAsignados(perfil);
			
			perfilBO.delete(getUsuarioCurrent(), perfil);
			return getLastPage().getOutcome();
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
			return "";
		}
	}
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@Override
	public String guardar() {
		try {
			if ("INS".equals(getAccion())) {
				perfilBO.save(getUsuarioCurrent(), perfil);
				setAccion("UPD");
				setParam2(perfil.getIdPerfil());
			} else {
				perfilBO.update(getUsuarioCurrent(), perfil);
			}
			displayMessage(Mensaje.saveMessaje, Mensaje.SEVERITY_INFO);
			return getPage().getOutcome();
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
			return "";
		}
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public List<SelectItem> getEstados() {
		try {
			return getSelectItems(PerfilEstado.values());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
