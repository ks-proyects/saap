package org.ec.jap.backend.pago;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.ec.jap.backend.pagina.Bean;
import org.ec.jap.backend.utilitario.Mensaje;
import org.ec.jap.bo.saap.EstadoCivilBO;
import org.ec.jap.entiti.saap.EstadoCivil;

@ManagedBean
@ViewScoped
public class EstadoCivilEditBean extends Bean {

	/**
	 * param1->idUsuario
	 */

	@EJB
	EstadoCivilBO estadoCivilBO;

	private EstadoCivil estadoCivil;

	public EstadoCivilEditBean() {
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
				estadoCivil = new EstadoCivil();
			} else {
				estadoCivil = estadoCivilBO.findByPk(getParam1Integer());
			}
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	@Override
	public String guardar() {
		try {
			if ("INS".equals(getAccion())) {
				estadoCivilBO.save(getUsuarioCurrent(), estadoCivil);
				setAccion("UPD");
				setParam1(estadoCivil.getIdEstadoCivil());
			} else {
				estadoCivilBO.update(getUsuarioCurrent(), estadoCivil);
			}
			displayMessage(Mensaje.deleteMessaje, Mensaje.SEVERITY_INFO);
			return getPage().getOutcome();
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
			return getPage().getOutcome();
		}
	}

	@Override
	public String delete() {
		try {
			map.clear();
			map.put("idEstadoCivil", estadoCivil);
			Integer numUsers= estadoCivilBO.findIntegerByNamedQuery("EstadoCivil.findUser", map);
			if(numUsers>0)
				throw new Exception("Existen usuarios que utilizan este Estado Civil, no puede eliminar");
			estadoCivilBO.delete(getUsuarioCurrent(), estadoCivil);
			displayMessage(Mensaje.deleteMessaje, Mensaje.SEVERITY_INFO);
			return getLastPage().getOutcome() + "?faces-redirect=true";
		} catch (Exception e) {
			displayMessage(Mensaje.errorMessaje, Mensaje.SEVERITY_FATAL);
			return getPage().getOutcome();
		}
	}

	public EstadoCivil getEstadoCivil() {
		return estadoCivil;
	}

	public void setEstadoCivil(EstadoCivil estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

}
