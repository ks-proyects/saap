package org.ec.jap.backend.pago;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import org.ec.jap.entiti.saap.PeriodoPago;
import org.ec.jap.enumerations.EpocaEnum;
import org.ec.jap.utilitario.Utilitario;

@ManagedBean
@ViewScoped
public class PeriodoPagoEditBean extends Bean {

	/**
	 * 
	 */

	@EJB
	PeriodoPagoBO periodoPagoBO;

	private PeriodoPago periodoPago;

	private List<SelectItem> epocas;
	private String epoca;

	public PeriodoPagoEditBean() {
		super();
	}

	@PostConstruct
	@Override
	public void init() {
		try {
			super.init();
			setTipoEntidad("PERPAG");
			search(null);
			redisplayActions(periodoPago.getEstado(), periodoPago.getIdPeriodoPago());
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}

	}

	@Override
	public void search(ActionEvent event) {
		try {
			Boolean puedeAgregarUnPeriodo = true;
			if ("INS".equals(getAccion())) {
				periodoPago = new PeriodoPago();
				Date fechaInicio = (Date) periodoPagoBO.findObjectByNamedQuery("PeriodoPago.findMaxMes");
				puedeAgregarUnPeriodo = Utilitario.puedeAgregarNuevoPeriodo(fechaInicio);
				Calendar calendarInicio = Calendar.getInstance();
				calendarInicio.setTime(fechaInicio != null ? fechaInicio : Calendar.getInstance().getTime());
				if (fechaInicio == null) {
					calendarInicio.add(Calendar.MONTH, -1);
					calendarInicio.set(Calendar.DAY_OF_MONTH, 1);
				} else
					calendarInicio.add(Calendar.DAY_OF_MONTH, 1);

				periodoPago.setFechaInicio(calendarInicio.getTime());
				calendarInicio.set(Calendar.DAY_OF_MONTH, calendarInicio.getActualMaximum(Calendar.DAY_OF_MONTH));
				periodoPago.setFechaFin(calendarInicio.getTime());
			} else {
				map = new HashMap<>(0);
				map.put("idPeriodoPago",getParam1Integer());
				periodoPago = periodoPagoBO.findByNamedQuery("PeriodoPago.findById", map);
				epoca=periodoPago.getEpoca()!=null?periodoPago.getEpoca().name():"-1";
				puedeAgregarUnPeriodo = Utilitario.puedeAgregarNuevoPeriodo(periodoPago.getFechaFin());
			}
			redisplayAction(7, puedeAgregarUnPeriodo);
			redisplayAction(4, puedeAgregarUnPeriodo);

		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	@Override
	public String guardar() {
		try {

			if (epoca.equals("-1")) {
				displayMessage("Debe seleccionar una epoca.", Mensaje.SEVERITY_WARN);
				return getPage().getOutcome();
			}
			Calendar calendarInicio = Calendar.getInstance();
			calendarInicio.setTime(periodoPago.getFechaInicio());
			Calendar calendarFinaliza = Calendar.getInstance();
			calendarFinaliza.setTime(periodoPago.getFechaFin());
			periodoPago.setEpoca(EpocaEnum.valueOf(epoca));
			periodoPago.setAnio(calendarInicio.get(Calendar.YEAR));
			periodoPago.setMes(calendarInicio.get(Calendar.MONTH));
			periodoPago.setDescripcion(Utilitario.mes(calendarInicio.get(Calendar.MONTH)) + " - "
					+ String.valueOf(calendarInicio.get(Calendar.YEAR)));
			if ("INS".equals(getAccion())) {
				periodoPago.setEstado("ING");
				periodoPagoBO.save(getUsuarioCurrent(), periodoPago);
				setParam1(periodoPago.getIdPeriodoPago());
				setAccion("UPD");
				cambioEstadoBO.cambiarEstadoSinVerificar(5, getUsuarioCurrent(), periodoPago.getIdPeriodoPago(), "");
			} else {
				periodoPagoBO.update(getUsuarioCurrent(), periodoPago);
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
			periodoPagoBO.delete(getUsuarioCurrent(), periodoPago);
			displayMessage(Mensaje.deleteMessaje, Mensaje.SEVERITY_INFO);
			return getLastPage().getOutcome() + "?faces-redirect=true";
		} catch (Exception e) {
			displayMessage(Mensaje.errorMessaje, Mensaje.SEVERITY_FATAL);
			return getPage().getOutcome();
		}

	}

	@Override
	public String nuevo() {
		try {
			Date fechaInicio = (Date) periodoPagoBO.findObjectByNamedQuery("PeriodoPago.findMaxMes");
			if (!Utilitario.puedeAgregarNuevoPeriodo(fechaInicio)) {
				displayMessage("Ya existe un periodo creado para el mes actual.", Mensaje.SEVERITY_INFO);
				return "periodoPagoList";
			} else {
				setAccion("INS");
				return "periodoPagoEdit?faces-redirect=true";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.nuevo();
	}

	public List<SelectItem> getAnios() {
		List<SelectItem> anios = new ArrayList<>();
		Calendar calendar = GregorianCalendar.getInstance();
		SelectItem item = new SelectItem(calendar.get(Calendar.YEAR), String.valueOf(calendar.get(Calendar.YEAR)));
		try {
			Integer anio = (Integer) listaValoreBO.findObjectByNamedQuery("ListaValor.findMaxAnios");
			if (anio == null || anio.equals(calendar.get(Calendar.YEAR))) {
				anios.add(item);
			}
			anios.addAll(getSelectItems(getUsuarioCurrent(), null, "ListaValor.findAnios"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return anios;
	}

	public List<SelectItem> getMeses() {
		return Utilitario.getMeses();
	}

	public PeriodoPago getPeriodoPago() {
		return periodoPago;
	}

	public void setPeriodoPago(PeriodoPago periodoPago) {
		this.periodoPago = periodoPago;
	}

	public List<SelectItem> getEpocas() {
		try {
			epocas = getSelectItems(EpocaEnum.values(),true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return epocas;
	}

	public void setEpocas(List<SelectItem> epocas) {
		this.epocas = epocas;
	}

	public String getEpoca() {
		return epoca;
	}

	public void setEpoca(String epoca) {
		this.epoca = epoca;
	}

}
