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
import org.ec.jap.bo.saap.DestinoBO;
import org.ec.jap.entiti.saap.Destino;
import org.ec.jap.enumerations.ValorSiNo;

@ManagedBean
@ViewScoped
public class ReporteCajaDetalleBean extends Bean {

	/**
	 * 
	 */

	@EJB
	DestinoBO destinoBO;

	private Destino destino;

	public ReporteCajaDetalleBean() {
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
				destino = new Destino();
			} else {
				destino = destinoBO.findByPk(getParam1Integer());
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
				destinoBO.save(getUsuarioCurrent(), destino);
				setParam1(destino.getIdDestino());
				setAccion("UPD");
			} else {
				destinoBO.update(getUsuarioCurrent(), destino);
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
			destinoBO.delete(getUsuarioCurrent(), destino);
			displayMessage(Mensaje.deleteMessaje, Mensaje.SEVERITY_INFO);
			return getLastPage().getOutcome() + "?faces-redirect=true";
		} catch (Exception e) {
			displayMessage(Mensaje.errorMessaje, Mensaje.SEVERITY_FATAL);
			return getPage().getOutcome();
		}
	}

	
	public Destino getDestino() {
		return destino;
	}
	public void setDestino(Destino destino) {
		this.destino = destino;
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
