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
import org.ec.jap.bo.saap.UsuarioBO;
import org.ec.jap.bo.sistema.UsuarioRolBO;
import org.ec.jap.entiti.sistema.UsuarioRol;
import org.ec.jap.enumerations.ValorSiNo;

@ManagedBean
@ViewScoped
public class UsuarioSRolEditBean extends Bean {

	/**
	 * 
	 */

	@EJB
	UsuarioBO usuarioBO;

	@EJB
	UsuarioRolBO usuarioRolBO;

	List<UsuarioRol> usuarioRols;

	public UsuarioSRolEditBean() {
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
			usuarioRols = usuarioRolBO.getByUser(usuarioBO.findByPk(getParam1Integer()));
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@Override
	public String guardar() {
		try {
			usuarioRolBO.savePerfil(usuarioRols, getUsuarioCurrent());
			displayMessage(Mensaje.saveMessaje, Mensaje.SEVERITY_INFO);
			return getPage().getOutcome();
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
			return "";
		}
	}

	public List<SelectItem> getSiNo() {
		try {
			return getSelectItems(ValorSiNo.values());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<UsuarioRol> getUsuarioRols() {
		return usuarioRols;
	}

	public void setUsuarioRols(List<UsuarioRol> usuarioRols) {
		this.usuarioRols = usuarioRols;
	}

}
