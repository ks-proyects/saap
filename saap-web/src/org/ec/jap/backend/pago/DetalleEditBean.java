package org.ec.jap.backend.pago;

import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.ec.jap.backend.pagina.Bean;
import org.ec.jap.backend.utilitario.Mensaje;
import org.ec.jap.bo.saap.CabeceraPlanillaBO;
import org.ec.jap.bo.saap.DetallePlanillaBO;
import org.ec.jap.bo.saap.RegistroEconomicoBO;
import org.ec.jap.bo.saap.TipoRegistroBO;
import org.ec.jap.bo.sistema.ListaValoreBO;
import org.ec.jap.entiti.saap.CabeceraPlanilla;
import org.ec.jap.entiti.saap.DetallePlanilla;
import org.ec.jap.entiti.saap.RegistroEconomico;
import org.ec.jap.enumerations.AccionType;
import org.ec.jap.utilitario.Constantes;
import org.ec.jap.utilitario.Utilitario;

@ManagedBean
@ViewScoped
public class DetalleEditBean extends Bean {

	@EJB
	CabeceraPlanillaBO cabeceraPlanillaBO;
	@EJB
	DetallePlanillaBO detallePlanillaBO;

	@EJB
	TipoRegistroBO tipoRegistroBO;

	@EJB
	ListaValoreBO listaValoreBO;

	@EJB
	RegistroEconomicoBO registroEconomicoBO;

	private List<SelectItem> tipoRegistros;

	private String tipoRegistro;

	private CabeceraPlanilla cp;

	private DetallePlanilla detallePlanilla;

	public DetalleEditBean() {
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
			map.clear();
			map.put("accion", AccionType.I);
			tipoRegistros = getSelectItems(getUsuarioCurrent(), map, "ListaValor.findTipoRegistroByTipo");
			cp = cabeceraPlanillaBO.findByPk(getParam2Integer());

			setNivelBloqueo("ING".equals(cp.getEstado()) ? 0 : 1);
			redisplayAction(5, "ING".equals(cp.getEstado()));
			redisplayAction(4, "ING".equals(cp.getEstado()));
			redisplayAction(7, "ING".equals(cp.getEstado()));

			if ("INS".equals(getAccion())) {
				detallePlanilla = new DetallePlanilla();
				detallePlanilla.setEsManual(true);
				detallePlanilla.setEstado("ING");
				detallePlanilla.setIdCabeceraPlanilla(cp);
				detallePlanilla.setValorPagado(0.0);
			} else {
				detallePlanilla = detallePlanillaBO.findByPk(getParam4Integer());
				tipoRegistro = detallePlanilla.getIdRegistroEconomico().getTipoRegistro().getTipoRegistro();
			}

		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	@Override
	public String guardar() {
		try {

			map.clear();
			map.put("idPeriodoPago", cp.getIdPeriodoPago());
			map.put("tipoRegistro", tipoRegistroBO.findByPk(tipoRegistro));

			detallePlanilla.setValorPagado(0.0);
			detallePlanilla.setValorTotal(detallePlanilla.getValorUnidad());
			detallePlanilla.setFechaRegistro(Calendar.getInstance().getTime());
			
			detallePlanilla.setValorPendiente(detallePlanilla.getValorUnidad());
			detallePlanilla.setValorTotalOrigen(detallePlanilla.getValorUnidad());
			detallePlanilla.setOrigen(Constantes.origen_mes_Actual);
			RegistroEconomico registroEconomico = registroEconomicoBO.findByNamedQuery("RegistroEconomico.findByPeriodoAndTipoLast", map);

			if (registroEconomico != null) {
				registroEconomico.setValor(Utilitario.redondear(registroEconomico.getValor() + detallePlanilla.getValorTotal()));
				registroEconomicoBO.update(getUsuarioCurrent(), registroEconomico);
			} else {
				registroEconomico = new RegistroEconomico();
				registroEconomico.setIdPeriodoPago(cp.getIdPeriodoPago());
				registroEconomico.setEstado("ING");
				registroEconomico.setFechaRegistro(Calendar.getInstance().getTime());
				registroEconomico.setTipoRegistro(tipoRegistroBO.findByPk(tipoRegistro));
				registroEconomico.setValor(detallePlanilla.getValorTotal());
				registroEconomico.setDescripcion(tipoRegistroBO.findByPk(tipoRegistro).getDescripcion() + cp.getIdPeriodoPago().getDescripcion());
				registroEconomico.setCantidadAplicados(0);
				registroEconomicoBO.save(getUsuarioCurrent(), registroEconomico);
				cambioEstadoBO.cambiarEstadoSinVerificar(8, getUsuarioCurrent(), registroEconomico.getIdRegistroEconomico(), "");
			}

			if ("CONS".equals(tipoRegistro)) {
				detallePlanilla.setOrdenStr("B");
				// detallePlanilla.setDescripcion(registroEconomico.getDescripcion());
			} else if ("MULAGU".equals(tipoRegistro)) {
				detallePlanilla.setOrdenStr("A");
				// detallePlanilla.setDescripcion(registroEconomico.getDescripcion());
			} else if ("CUO".equals(tipoRegistro)) {
				detallePlanilla.setOrdenStr("F");
				// detallePlanilla.setDescripcion(registroEconomico.getDescripcion()
				// + " " + detallePlanilla.getDescripcion());
			} else if ("CUOINI".equals(tipoRegistro)) {
				detallePlanilla.setOrdenStr("G");
				// detallePlanilla.setDescripcion(registroEconomico.getDescripcion());
			} else if ("INASIS".equals(tipoRegistro)) {
				detallePlanilla.setOrdenStr("E");
				// detallePlanilla.setDescripcion(registroEconomico.getDescripcion()
				// + " " + detallePlanilla.getDescripcion());
			} else {
				detallePlanilla.setOrdenStr("H");
			}

			detallePlanilla.setEsManual(true);
			detallePlanilla.setIdRegistroEconomico(registroEconomico);

			if ("INS".equals(getAccion())) {
				detallePlanillaBO.save(getUsuarioCurrent(), detallePlanilla);
				setAccion("UPD");
				setParam4(detallePlanilla.getIdDetallePlanilla());
			} else {
				detallePlanillaBO.update(getUsuarioCurrent(), detallePlanilla);
			}

			map.clear();
			map.put("idCabeceraPlanilla", cp);
			List<DetallePlanilla> detallePlanillas = detallePlanillaBO.findAllByNamedQuery("DetallePlanilla.findByCabecara", map);

			Double total = 0.0;

			for (DetallePlanilla detallePlanilla : detallePlanillas) {
				total = total + detallePlanilla.getValorTotal();
			}
			cp.setTotal(Utilitario.redondear(total));
			cp.setSubtotal(Utilitario.redondear(total));

			cabeceraPlanillaBO.update(getUsuarioCurrent(), cp);

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
			RegistroEconomico registroEconomico = detallePlanilla.getIdRegistroEconomico();
			registroEconomico.setValor(registroEconomico.getValor() - detallePlanilla.getValorPagado());
			registroEconomicoBO.update(getUsuarioCurrent(), registroEconomico);
			detallePlanilla.setIdCabeceraPlanilla(cp);
			detallePlanillaBO.delete(getUsuarioCurrent(), detallePlanilla);

			map.clear();
			map.put("idCabeceraPlanilla", cp);
			List<DetallePlanilla> detallePlanillas = detallePlanillaBO.findAllByNamedQuery("DetallePlanilla.findByCabecara", map);

			Double total = 0.0;

			for (DetallePlanilla detallePlanilla : detallePlanillas) {
				total = total + detallePlanilla.getValorTotal();
			}
			cp.setTotal(Utilitario.redondear(total));

			cabeceraPlanillaBO.update(getUsuarioCurrent(), cp);

			displayMessage(Mensaje.saveMessaje, Mensaje.SEVERITY_INFO);
			return getLastPage().getOutcome();
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
			return getPage().getOutcome();
		}
	}

	/**
	 * @return the tipoRegistros
	 */
	public List<SelectItem> getTipoRegistros() {
		return tipoRegistros;
	}

	/**
	 * @param tipoRegistros
	 *            the tipoRegistros to set
	 */
	public void setTipoRegistros(List<SelectItem> tipoRegistros) {
		this.tipoRegistros = tipoRegistros;
	}

	/**
	 * @return the tipoRegistro
	 */
	public String getTipoRegistro() {
		return tipoRegistro;
	}

	/**
	 * @param tipoRegistro
	 *            the tipoRegistro to set
	 */
	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}

	public CabeceraPlanilla getCp() {
		return cp;
	}

	public void setCp(CabeceraPlanilla cp) {
		this.cp = cp;
	}

	public DetallePlanilla getDetallePlanilla() {
		return detallePlanilla;
	}

	public void setDetallePlanilla(DetallePlanilla detallePlanilla) {
		this.detallePlanilla = detallePlanilla;
	}

}
