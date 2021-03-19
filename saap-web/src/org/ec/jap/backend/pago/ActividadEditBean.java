package org.ec.jap.backend.pago;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.ec.jap.backend.pagina.Bean;
import org.ec.jap.backend.utilitario.Mensaje;
import org.ec.jap.bo.saap.ActividadBO;
import org.ec.jap.bo.saap.PeriodoPagoBO;
import org.ec.jap.bo.saap.TipoActividadBO;
import org.ec.jap.entiti.saap.Actividad;
import org.ec.jap.entiti.saap.PeriodoPago;
import org.ec.jap.entiti.saap.TipoActividad;
import org.ec.jap.enumerations.ValorSiNo;

@ManagedBean
@ViewScoped
public class ActividadEditBean extends Bean {

	/**
	 * 
	 */

	@EJB
	ActividadBO actividadBO;

	@EJB
	PeriodoPagoBO periodoPagoBO;

	@EJB
	TipoActividadBO tipoActividadBO;

	private Actividad actividad;
	private Integer idTipoActividad;
	private Integer idPeriodoPago;

	private List<SelectItem> tipoActividad;
	private List<SelectItem> periodoPago;

	public ActividadEditBean() {
		super();
	}

	@PostConstruct
	@Override
	public void init() {
		try {
			super.init();
			setTipoEntidad("ACT");
			search(null);
			if ("UPD".equals(getAccion()))
				redisplayActions(actividad.getActEstado(), actividad.getActividad());
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}

	}

	@Override
	public void search(ActionEvent event) {
		try {
			map.clear();
			map.put("activo", ValorSiNo.S);
			
			if ("INS".equals(getAccion())) {
				actividad = new Actividad();
				actividad.setActEstado("ING");
				periodoPago = getSelectItems(getUsuarioCurrent(), null, "ListaValor.findPeriododAvalible", true);
				tipoActividad = getSelectItems(getUsuarioCurrent(), map, "ListaValor.findByEstado", true);

			} else {
				actividad = actividadBO.findByPk(getParam1Integer());
				idPeriodoPago = actividad.getIdPeriodoPago().getIdPeriodoPago();
				idTipoActividad = actividad.getTipoActividad().getTipoActividad();
				map.put("tipoActividad", idTipoActividad);
				tipoActividad = getSelectItems(getUsuarioCurrent(), map, "ListaValor.findByCurrentAndEstado", true);

				map.clear();
				map.put("idPeriodoPago", idPeriodoPago);
				periodoPago = getSelectItems(getUsuarioCurrent(), map, "ListaValor.findPeriododAvalibleAndPeriodo", true);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	@Override
	public String guardar() {
		try {
			PeriodoPago periodoPago = periodoPagoBO.findByIdCustom(idPeriodoPago);
			TipoActividad tipoActividad = tipoActividadBO.findByPk(idTipoActividad);
			actividad.setIdPeriodoPago(periodoPago);
			actividad.setTipoActividad(tipoActividad);

			if (periodoPago == null)
				throw new Exception("Debe seleccionar un Periodo");
			if (tipoActividad == null)
				throw new Exception("Debe seleccionar un tipo de actividad");

			if ("INS".equals(getAccion())) {
				actividadBO.save(getUsuarioCurrent(), actividad);
				cambioEstadoBO.cambiarEstadoSinVerificar(42, getUsuarioCurrent(), actividad.getActividad(), "");
				setParam1(actividad.getActividad());
				setAccion("UPD");
			} else {
				actividadBO.update(getUsuarioCurrent(), actividad);

			}
			displayMessage(Mensaje.saveMessaje, Mensaje.SEVERITY_INFO);
			return getPage().getOutcome();
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
			return "";
		}
	}

	public void valueChange(ValueChangeEvent event) {
		try {
			idTipoActividad = event == null ? 0 : Integer.valueOf(event.getNewValue() != null ? event.getNewValue().toString() : "0");
			TipoActividad tipoActividad = tipoActividadBO.findByPk(idTipoActividad);
			if (tipoActividad != null)
				actividad.setValorMulta(tipoActividad.getValorMulta());

		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	@Override
	public String delete() {
		try {
			actividadBO.delete(getUsuarioCurrent(), actividad);
			displayMessage(Mensaje.deleteMessaje, Mensaje.SEVERITY_INFO);
			return getLastPage().getOutcome() + "?faces-redirect=true";
		} catch (Exception e) {
			displayMessage(Mensaje.errorMessaje, Mensaje.SEVERITY_FATAL);
			return getPage().getOutcome();
		}

	}

	public Actividad getActividad() {
		return actividad;
	}

	public void setActividad(Actividad actividad) {
		this.actividad = actividad;
	}

	public Integer getIdTipoActividad() {
		return idTipoActividad;
	}

	public void setIdTipoActividad(Integer idTipoActividad) {
		this.idTipoActividad = idTipoActividad;
	}

	public Integer getIdPeriodoPago() {
		return idPeriodoPago;
	}

	public void setIdPeriodoPago(Integer idPeriodoPago) {
		this.idPeriodoPago = idPeriodoPago;
	}

	public List<SelectItem> getTipoActividad() {
		return tipoActividad;
	}

	public void setTipoActividad(List<SelectItem> tipoActividad) {
		this.tipoActividad = tipoActividad;
	}

	public List<SelectItem> getPeriodoPago() {
		return periodoPago;
	}

	public void setPeriodoPago(List<SelectItem> periodoPago) {
		this.periodoPago = periodoPago;
	}

}
