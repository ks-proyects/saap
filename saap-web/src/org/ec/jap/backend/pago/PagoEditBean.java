package org.ec.jap.backend.pago;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.ec.jap.backend.pagina.Bean;
import org.ec.jap.backend.utilitario.Mensaje;
import org.ec.jap.bo.saap.CabeceraPlanillaBO;
import org.ec.jap.bo.saap.DetallePlanillaBO;
import org.ec.jap.bo.saap.RegistroEconomicoBO;
import org.ec.jap.entiti.saap.CabeceraPlanilla;
import org.ec.jap.utilitario.Utilitario;

/**
 * @author Freddy
 * 
 */
@ManagedBean
@ViewScoped
public class PagoEditBean extends Bean {

	@EJB
	CabeceraPlanillaBO cabeceraPlanillaBO;

	@EJB
	DetallePlanillaBO detallePlanillaBO;
	@EJB
	RegistroEconomicoBO registroEconomicoBO;

	private CabeceraPlanilla cp;

	private Double valorAPagar;

	private Double valorPendiente;

	private Double valorAbono;

	private Double valorPagado;

	private Double valorCambio;

	private Double valorAbonoAnterior;

	private CabeceraPlanilla anterior;

	public PagoEditBean() {
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
			cp = cabeceraPlanillaBO.findByPk(getIdDocumentoEntidad());
			anterior = cabeceraPlanillaBO.getAbonoMesAnterior(cp.getIdLlave(), cp);
			valorPendiente = Utilitario.redondear(cp.getValorPendiente());
			valorAPagar = Utilitario.redondear(cp.getValorPendiente());
			if (anterior.getAbonoUsd() > cp.getValorPendiente()) {
				valorAbono = Utilitario.redondear(Utilitario.redondear(anterior.getAbonoUsd()) - Utilitario.redondear(cp.getValorPendiente()));
			} else {
				valorAbono = Utilitario.redondear(cp.getAbonoUsd());
			}
			valorPagado = Utilitario.redondear(cp.getValorPagado());
			redisplayAction(7, !cp.getValorPendiente().equals(0.0));

		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	@Override
	public String guardar() {
		try {
			// Registramos el valor
			cabeceraPlanillaBO.guardarPlanilla(getUsuarioCurrent(), valorAPagar, cp, anterior, valorAbono, getIdCambioEstado(), getIdDocumentoInteger());

			displayMessage(Mensaje.saveMessaje, Mensaje.SEVERITY_INFO);
			return getPage().getOutcome();
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
			return getPage().getOutcome();
		}
	}

	/**
	 * @return the cp
	 */
	public CabeceraPlanilla getCp() {
		return cp;
	}

	/**
	 * @param cp
	 *            the cp to set
	 */
	public void setCp(CabeceraPlanilla cp) {
		this.cp = cp;
	}

	/**
	 * @return the valorPendiente
	 */
	public Double getValorPendiente() {
		return valorPendiente;
	}

	/**
	 * @param valorPendiente
	 *            the valorPendiente to set
	 */
	public void setValorPendiente(Double valorPendiente) {
		this.valorPendiente = valorPendiente;
	}

	/**
	 * @return the valorAbono
	 */
	public Double getValorAbono() {
		return valorAbono;
	}

	/**
	 * @param valorAbono
	 *            the valorAbono to set
	 */
	public void setValorAbono(Double valorAbono) {
		this.valorAbono = valorAbono;
	}

	/**
	 * @return the valorPagado
	 */
	public Double getValorPagado() {
		return valorPagado;
	}

	/**
	 * @param valorPagado
	 *            the valorPagado to set
	 */
	public void setValorPagado(Double valorPagado) {
		this.valorPagado = valorPagado;
	}

	/**
	 * @return the valorCambio
	 */
	public Double getValorCambio() {
		return valorCambio;
	}

	/**
	 * @param valorCambio
	 *            the valorCambio to set
	 */
	public void setValorCambio(Double valorCambio) {
		this.valorCambio = valorCambio;
	}

	/**
	 * @return the anterior
	 */
	public CabeceraPlanilla getAnterior() {
		return anterior;
	}

	/**
	 * @param anterior
	 *            the anterior to set
	 */
	public void setAnterior(CabeceraPlanilla anterior) {
		this.anterior = anterior;
	}

	public Double getValorAbonoAnterior() {
		return valorAbonoAnterior;
	}

	public void setValorAbonoAnterior(Double valorAbonoAnterior) {
		this.valorAbonoAnterior = valorAbonoAnterior;
	}

	public Double getValorAPagar() {
		return valorAPagar;
	}

	public void setValorAPagar(Double valorAPagar) {
		this.valorAPagar = valorAPagar;
	}

}
