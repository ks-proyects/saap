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
import org.ec.jap.bo.saap.GastoBO;
import org.ec.jap.bo.saap.PeriodoPagoBO;
import org.ec.jap.bo.saap.RegistroEconomicoBO;
import org.ec.jap.entiti.saap.Destino;
import org.ec.jap.entiti.saap.Gasto;
import org.ec.jap.entiti.saap.PeriodoPago;
import org.ec.jap.entiti.saap.RegistroEconomico;
import org.ec.jap.enumerations.ValorSiNo;

@ManagedBean
@ViewScoped
public class GastoEditBean extends Bean {
	/**
	 * 
	 */

	@EJB
	GastoBO gastoBO;

	@EJB
	PeriodoPagoBO periodoPagoBO;

	@EJB
	DestinoBO destinoBO;

	@EJB
	RegistroEconomicoBO registroEconomicoBO;
	private Gasto gasto;
	private Integer idDestino;
	private Integer idPeriodoPago;

	private List<SelectItem> destinos;
	private List<SelectItem> periodoPagos;

	public GastoEditBean() {
		super();
	}

	@PostConstruct
	@Override
	public void init() {
		try {
			super.init();
			search(null);
			redisplayAction(5, !getReadOnly1());
			redisplayAction(7, !getReadOnly1());
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
				gasto = new Gasto();
				periodoPagos = getSelectItems(getUsuarioCurrent(), null, "ListaValor.findPeriododAvalibleGasto", true);
				destinos = getSelectItems(getUsuarioCurrent(), map, "ListaValor.findDestinoByEstado", true);

			} else {
				gasto = gastoBO.findByPk(getParam1Integer());
				idPeriodoPago = gasto.getIdPeriodoPago().getIdPeriodoPago();
				idDestino = gasto.getIdDestino().getIdDestino();
				map.put("idDestino", idDestino);
				destinos = getSelectItems(getUsuarioCurrent(), map, "ListaValor.findDestinoByCurrentAndEstado", true);

				map.clear();
				map.put("idPeriodoPago", idPeriodoPago);
				periodoPagos = getSelectItems(getUsuarioCurrent(), map, "ListaValor.findPeriododAvalibleAndPeriodo", true);
				setNivelBloqueo("FIN".equals(gasto.getIdPeriodoPago().getEstado()) || "FINI".equals(gasto.getIdPeriodoPago().getEstado()) ? 1 : 0);

			}
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	@Override
	public String guardar() {
		try {
			PeriodoPago periodoPago = periodoPagoBO.findByPk(idPeriodoPago);
			Destino destino = destinoBO.findByPk(idDestino);
			gasto.setIdPeriodoPago(periodoPago);
			gasto.setIdDestino(destino);

			
			map.clear();
			map.put("idPeriodoPago", periodoPago);
			map.put("tipoRegistro", "GAST");
			
			RegistroEconomico registroEconomico=registroEconomicoBO.findByNamedQuery("RegistroEconomico.findByType", map);
			
			gasto.setIdRegistroEconomico(registroEconomico);
			
			if (periodoPago == null)
				throw new Exception("Debe seleccionar un Periodo");
			if (destino == null)
				throw new Exception("Debe seleccionar un destino para el gasto");

			if ("INS".equals(getAccion())) {
				gastoBO.save(getUsuarioCurrent(), gasto);
				setParam1(gasto.getIdGasto());
				setAccion("UPD");
			} else {
				gastoBO.update(getUsuarioCurrent(), gasto);

			}
			displayMessage(Mensaje.saveMessaje, Mensaje.SEVERITY_INFO);
			return getPage().getOutcome();
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
			return "";
		}
	}

	@Override
	public String delete() {
		try {
			gastoBO.delete(getUsuarioCurrent(), gasto);
			displayMessage(Mensaje.deleteMessaje, Mensaje.SEVERITY_INFO);
			return getLastPage().getOutcome() + "?faces-redirect=true";
		} catch (Exception e) {
			displayMessage(Mensaje.errorMessaje, Mensaje.SEVERITY_FATAL);
			return getPage().getOutcome();
		}

	}

	public Gasto getGasto() {
		return gasto;
	}

	public void setGasto(Gasto gasto) {
		this.gasto = gasto;
	}

	public Integer getIdDestino() {
		return idDestino;
	}

	public void setIdDestino(Integer idDestino) {
		this.idDestino = idDestino;
	}

	public Integer getIdPeriodoPago() {
		return idPeriodoPago;
	}

	public void setIdPeriodoPago(Integer idPeriodoPago) {
		this.idPeriodoPago = idPeriodoPago;
	}

	public List<SelectItem> getDestinos() {
		return destinos;
	}

	public void setDestinos(List<SelectItem> destinos) {
		this.destinos = destinos;
	}

	public List<SelectItem> getPeriodoPagos() {
		return periodoPagos;
	}

	public void setPeriodoPagos(List<SelectItem> periodoPagos) {
		this.periodoPagos = periodoPagos;
	}

}
