package org.ec.jap.backend.pago;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.ec.jap.backend.pagina.Bean;
import org.ec.jap.backend.utilitario.Mensaje;
import org.ec.jap.bo.saap.TipoActividadBO;
import org.ec.jap.entiti.saap.TipoActividad;
import org.ec.jap.enumerations.ValorSiNo;

@ManagedBean
@ViewScoped
public class TipoActividadEditBean extends Bean {

	/**
	 * 
	 */

	@EJB
	TipoActividadBO tipoActividadBO;

	private TipoActividad tipoActividad;

	public TipoActividadEditBean() {
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
				tipoActividad = new TipoActividad();
			} else {
				tipoActividad = tipoActividadBO.findByPk(getParam1Integer());
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
				tipoActividadBO.save(getUsuarioCurrent(), tipoActividad);
				setParam1(tipoActividad.getTipoActividad());
				setAccion("UPD");
			} else {
				tipoActividadBO.update(getUsuarioCurrent(), tipoActividad);
			}
			displayMessage(Mensaje.saveMessaje, Mensaje.SEVERITY_INFO);
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
			tipoActividadBO.delete(getUsuarioCurrent(), tipoActividad);
			displayMessage(Mensaje.deleteMessaje, Mensaje.SEVERITY_INFO);
			return getLastPage().getOutcome() + "?faces-redirect=true";
		} catch (Exception e) {
			displayMessage(Mensaje.errorMessaje, Mensaje.SEVERITY_FATAL);
			return getPage().getOutcome();
		}
	}

	public TipoActividad getTipoActividad() {
		return tipoActividad;
	}

	public void setTipoActividad(TipoActividad tipoActividad) {
		this.tipoActividad = tipoActividad;
	}
	public List<SelectItem> getActivos() {
		try {
			return getSelectItems(ValorSiNo.values());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
