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
import org.ec.jap.bo.saap.RangoConsumoBO;
import org.ec.jap.bo.saap.TarifaBO;
import org.ec.jap.entiti.saap.RangoConsumo;
import org.ec.jap.enumerations.EpocaEnum;

@ManagedBean
@ViewScoped
public class RangoConsumoEditBean extends Bean {

	/**
	 * param1->idUsuario
	 */

	@EJB
	RangoConsumoBO rangoConsumoBO;

	@EJB
	TarifaBO tarifaBO;

	private RangoConsumo rangoConsumo;

	public RangoConsumoEditBean() {
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
				rangoConsumo = new RangoConsumo();
			} else {
				rangoConsumo = rangoConsumoBO.findByPk(getParam2Integer());
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
				rangoConsumo.setIdTarifa(tarifaBO.findByPk(getParam1Integer()));
				rangoConsumoBO.save(getUsuarioCurrent(), rangoConsumo);
				setAccion("UPD");
				setParam2(rangoConsumo.getIdRangoConsumo());
			} else {
				rangoConsumoBO.update(getUsuarioCurrent(), rangoConsumo);
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
			rangoConsumoBO.delete(getUsuarioCurrent(), rangoConsumo);
			displayMessage(Mensaje.deleteMessaje, Mensaje.SEVERITY_INFO);
			return getLastPage().getOutcome() + "?faces-redirect=true";
		} catch (Exception e) {
			displayMessage(e.getMessage(), Mensaje.SEVERITY_FATAL);
			return getPage().getOutcome();
		}
	}

	public List<SelectItem> getEpocas() {
		try {
			return getSelectItems(EpocaEnum.values(), true);
		} catch (Exception e) {
			displayMessage(e.getMessage(), Mensaje.SEVERITY_FATAL);
		}
		return null;
	}

	public RangoConsumo getRangoConsumo() {
		return rangoConsumo;
	}

	public void setRangoConsumo(RangoConsumo rangoConsumo) {
		this.rangoConsumo = rangoConsumo;
	}

}
