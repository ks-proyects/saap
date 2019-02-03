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
import org.ec.jap.bo.sistema.UsuarioPerfilBO;
import org.ec.jap.entiti.sistema.UsuarioPerfil;
import org.ec.jap.enumerations.ValorSiNo;

@ManagedBean
@ViewScoped
public class UsuarioSPerfilEditBean extends Bean {

	/**
	 * 
	 */

	@EJB
	UsuarioBO usuarioBO;

	@EJB
	UsuarioPerfilBO usuarioPerfilBO;

	List<UsuarioPerfil> usuarioPerfils;

	public UsuarioSPerfilEditBean() {
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
			usuarioPerfils = usuarioPerfilBO.getByUser(usuarioBO.findByPk(getParam1Integer()));
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@Override
	public String guardar() {
		try {
			usuarioPerfilBO.savePerfil(usuarioPerfils, getUsuarioCurrent());
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

	public List<UsuarioPerfil> getUsuarioPerfils() {
		return usuarioPerfils;
	}

	public void setUsuarioPerfils(List<UsuarioPerfil> usuarioPerfils) {
		this.usuarioPerfils = usuarioPerfils;
	}

}
