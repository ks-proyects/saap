package org.ec.jap.backend.pago;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.ec.jap.backend.pagina.Bean;
import org.ec.jap.backend.utilitario.Mensaje;
import org.ec.jap.bo.saap.PeriodoPagoBO;
import org.ec.jap.bo.saap.RegistroEconomicoBO;
import org.ec.jap.bo.saap.TipoRegistroBO;
import org.ec.jap.entiti.saap.RegistroEconomico;

@ManagedBean
@ViewScoped
public class RegistroEconomicoEditBean extends Bean {

	/**
	 * 
	 */

	@EJB
	RegistroEconomicoBO registroEconomicoBO;

	@EJB
	PeriodoPagoBO periodoPagoBO;
	@EJB
	TipoRegistroBO tipoRegistroBO;

	private RegistroEconomico registroEconomico;

	private Integer idPeriodoPago;
	private List<SelectItem> periodoPago;

	public RegistroEconomicoEditBean() {
		super();
	}

	@PostConstruct
	@Override
	public void init() {
		try {
			super.init();
			setTipoEntidad("CUO".equals(getParam20String()) ? "REGECO" : "");
			search(null);
			redisplayActions(registroEconomico.getEstado(), registroEconomico.getIdRegistroEconomico());
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}

	}

	@Override
	public void search(ActionEvent event) {
		try {
			map = new HashMap<>();

			if ("INS".equals(getAccion())) {
				registroEconomico = new RegistroEconomico();
				map.put("idPeriodoPago", 0);
			} else {
				registroEconomico = registroEconomicoBO.findByPk(getParam1Integer());
				idPeriodoPago = registroEconomico.getIdPeriodoPago().getIdPeriodoPago();
				map.put("idPeriodoPago", idPeriodoPago);
				redisplayAction(11, !"CUO".equals(getParam20String()));
			}
			map.put("estado1", "CUO".equals(getParam20String()) ? "ING" : "ABIE");
			map.put("estado2", "CUO".equals(getParam20String()) ? "ABIE" : "ABIE");
			periodoPago = getSelectItems(getUsuarioCurrent(), map, "ListaValor.finPeridos");
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	@Override
	public String guardar() {
		try {

			if (idPeriodoPago.equals(0)) {
				displayMessage("Debe seleccionar un periodo.", Mensaje.SEVERITY_WARN);
				return getPage().getOutcome();
			}
			registroEconomico.setIdPeriodoPago(periodoPagoBO.findByIdCustom(idPeriodoPago));

			if ("INS".equals(getAccion())) {
				registroEconomico.setTipoRegistro(tipoRegistroBO.findByPk(getParam20String()));
				registroEconomico.setEstado("ING");
				registroEconomico.setCantidadAplicados(0);
				registroEconomico.setFechaRegistro(Calendar.getInstance().getTime());
				registroEconomicoBO.save(getUsuarioCurrent(), registroEconomico);
				setParam1(registroEconomico.getIdRegistroEconomico());
				setAccion("UPD");
			} else {
				registroEconomicoBO.update(getUsuarioCurrent(), registroEconomico);
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
		// TODO Auto-generated method stub
		try {
			registroEconomicoBO.delete(getUsuarioCurrent(), registroEconomico);
			displayMessage(Mensaje.deleteMessaje, Mensaje.SEVERITY_INFO);
			return getLastPage().getOutcome() + "?faces-redirect=true";
		} catch (Exception e) {
			displayMessage(Mensaje.errorMessaje, Mensaje.SEVERITY_FATAL);
			return getPage().getOutcome();
		}

	}
	
	@Override
	public String generalAccion() {
		setAccion("INS");
		return "usuarioPagoList?faces-redirect=true";
	}

	public RegistroEconomico getRegistroEconomico() {
		return registroEconomico;
	}

	public void setRegistroEconomico(RegistroEconomico registroEconomico) {
		this.registroEconomico = registroEconomico;
	}

	public Integer getIdPeriodoPago() {
		return idPeriodoPago;
	}

	public void setIdPeriodoPago(Integer idPeriodoPago) {
		this.idPeriodoPago = idPeriodoPago;
	}

	public List<SelectItem> getPeriodoPago() {
		return periodoPago;
	}

	public void setPeriodoPago(List<SelectItem> periodoPago) {
		this.periodoPago = periodoPago;
	}

}
