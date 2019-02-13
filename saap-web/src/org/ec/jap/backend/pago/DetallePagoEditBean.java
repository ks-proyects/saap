package org.ec.jap.backend.pago;

import java.util.Calendar;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.ec.jap.backend.pagina.Bean;
import org.ec.jap.backend.utilitario.Mensaje;
import org.ec.jap.bo.saap.CabeceraPlanillaBO;
import org.ec.jap.bo.saap.DetallePlanillaBO;
import org.ec.jap.bo.saap.PeriodoPagoBO;
import org.ec.jap.bo.saap.RegistroEconomicoBO;
import org.ec.jap.bo.saap.TipoRegistroBO;
import org.ec.jap.entiti.saap.CabeceraPlanilla;
import org.ec.jap.entiti.saap.DetallePlanilla;
import org.ec.jap.entiti.saap.PeriodoPago;
import org.ec.jap.entiti.saap.RegistroEconomico;

@ManagedBean
@ViewScoped
public class DetallePagoEditBean extends Bean {

	/**
	 * 
	 */

	@EJB
	RegistroEconomicoBO registroEconomicoBO;

	@EJB
	DetallePlanillaBO detallePlanillaBO;
	@EJB
	PeriodoPagoBO periodoPagoBO;
	@EJB
	TipoRegistroBO tipoRegistroBO;

	@EJB
	CabeceraPlanillaBO cabeceraPlanillaBO;

	private RegistroEconomico registroEconomico;

	private DetallePlanilla detallePlanilla;

	private PeriodoPago periodoPago;

	public DetallePagoEditBean() {
		super();
	}

	@PostConstruct
	@Override
	public void init() {
		try {
			setTipoEntidad("REGECO");
			super.init();
			search(null);
			redisplayActions(registroEconomico.getEstado(), registroEconomico.getIdRegistroEconomico());
			String estado = detallePlanilla != null ? detallePlanilla.getEstado() : "";
			setDescripcionEstado(estado.equals("ING") ? "Ingresado" : estado.equals("DESC") ? "Descartado" : estado.equals("PAG") ? "Pagado" : estado.equals("NOPAG") ? "No Pagado" : "Traspasado");

		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}

	}

	@Override
	public void search(ActionEvent event) {
		try {

			if ("INS".equals(getAccion())) {
				periodoPago = periodoPagoBO.findByNamedQuery("PeriodoPago.findAbierto");
				registroEconomico = new RegistroEconomico();
				detallePlanilla = new DetallePlanilla();
			} else {
				detallePlanilla = detallePlanillaBO.findByPk(getParam4Integer());
				registroEconomico = detallePlanilla.getIdRegistroEconomico();
				periodoPago = registroEconomico.getIdPeriodoPago();

			}
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	@Override
	public String guardar() {
		try {
			map = new HashMap<>();
			map.put("estado", "ING");
			map.put("idUsuario", getParam2Integer());
			CabeceraPlanilla cabeceraPlanilla = cabeceraPlanillaBO.findByNamedQuery("CabeceraPlanilla.findByUsuarioAndEstado", map);
			if ("INS".equals(getAccion())) {
				if (periodoPago == null) {
					displayMessage("Primero debe tener un periodo abierto.", Mensaje.SEVERITY_ERROR);
					return "";
				}
				registroEconomico.setIdPeriodoPago(periodoPago);
				registroEconomico.setFechaRegistro(Calendar.getInstance().getTime());
				registroEconomico.setEstado("ING");
				registroEconomico.setCantidadAplicados(1);
				registroEconomico.setTipoRegistro(tipoRegistroBO.findByPk(getParam3String()));
				registroEconomicoBO.save(getUsuarioCurrent(), registroEconomico);
				cambioEstadoBO.cambiarEstadoSinVerificar(8, getUsuarioCurrent(), registroEconomico.getIdRegistroEconomico(), "");
				cambioEstadoBO.cambiarEstadoSinVerificar(9, getUsuarioCurrent(), registroEconomico.getIdRegistroEconomico(), "");

				String tipoRegistro = getParam3String();
				if ("CONS".equals(tipoRegistro)) {
					detallePlanilla.setOrdenStr("B");
					detallePlanilla.setDescripcion(registroEconomico.getDescripcion());
				} else if ("MULAGU".equals(tipoRegistro) || "BASCON".equalsIgnoreCase(tipoRegistro)) {
					detallePlanilla.setOrdenStr("A");
					detallePlanilla.setDescripcion(registroEconomico.getDescripcion());
				} else if ("CUO".equals(tipoRegistro)) {
					detallePlanilla.setOrdenStr("F");
					detallePlanilla.setDescripcion(registroEconomico.getDescripcion() + " " + detallePlanilla.getDescripcion());
				} else if ("CUOINI".equals(tipoRegistro)) {
					detallePlanilla.setOrdenStr("G");
					detallePlanilla.setDescripcion(registroEconomico.getDescripcion());
				} else if ("INASIS".equals(tipoRegistro)) {
					detallePlanilla.setOrdenStr("E");
					detallePlanilla.setDescripcion(registroEconomico.getDescripcion() + " " + detallePlanilla.getDescripcion());
				} else {
					detallePlanilla.setOrdenStr("H");
				}
				detallePlanilla.setEstado("ING");
				registroEconomico.setIdPeriodoPago(periodoPago);
				detallePlanilla.setIdCabeceraPlanilla(cabeceraPlanilla);
				detallePlanilla.setIdRegistroEconomico(registroEconomico);
				detallePlanilla.setValorTotal(registroEconomico.getValor());
				detallePlanilla.setFechaRegistro(Calendar.getInstance().getTime());
				detallePlanilla.setValorPendiente(registroEconomico.getValor());
				detallePlanilla.setValorPagado(0.0);
				detallePlanilla.setValorUnidad(registroEconomico.getValor());
				detallePlanillaBO.save(getUsuarioCurrent(), detallePlanilla);
				setParam4(detallePlanilla.getIdDetallePlanilla());
				setAccion("UPD");
			} else {
				registroEconomicoBO.update(getUsuarioCurrent(), registroEconomico);
				detallePlanilla.setIdRegistroEconomico(registroEconomico);
				detallePlanilla.setValorTotal(registroEconomico.getValor());
				detallePlanilla.setValorUnidad(registroEconomico.getValor());
				detallePlanilla.setDescripcion(registroEconomico.getDescripcion());
				detallePlanillaBO.update(getUsuarioCurrent(), detallePlanilla);
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
	protected void redisplayActions(String estadoActual, Object idDocumento) throws Exception {
		// TODO Auto-generated method stub
		super.redisplayActions(registroEconomico.getEstado(), registroEconomico.getIdRegistroEconomico());

	}

	@Override
	public String delete() {
		// TODO Auto-generated method stub
		try {
			detallePlanillaBO.delete(getUsuarioCurrent(), detallePlanilla);
			registroEconomicoBO.delete(getUsuarioCurrent(), registroEconomico);
			displayMessage(Mensaje.deleteMessaje, Mensaje.SEVERITY_INFO);
			return getLastPage().getOutcome() + "?faces-redirect=true";
		} catch (Exception e) {
			displayMessage(Mensaje.errorMessaje, Mensaje.SEVERITY_FATAL);
			return getPage().getOutcome();
		}

	}

	@Override
	public String insert() {
		setAccion("INS");
		return "lecturaEdit?faces-redirect=true";
	}

	public RegistroEconomico getRegistroEconomico() {
		return registroEconomico;
	}

	public void setRegistroEconomico(RegistroEconomico registroEconomico) {
		this.registroEconomico = registroEconomico;
	}

	public DetallePlanilla getDetallePlanilla() {
		return detallePlanilla;
	}

	public void setDetallePlanilla(DetallePlanilla detallePlanilla) {
		this.detallePlanilla = detallePlanilla;
	}

	public PeriodoPago getPeriodoPago() {
		return periodoPago;
	}

	public void setPeriodoPago(PeriodoPago periodoPago) {
		this.periodoPago = periodoPago;
	}

}
