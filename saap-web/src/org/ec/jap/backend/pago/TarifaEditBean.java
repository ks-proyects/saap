package org.ec.jap.backend.pago;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.ec.jap.backend.pagina.Bean;
import org.ec.jap.backend.utilitario.Mensaje;
import org.ec.jap.bo.saap.RangoConsumoBO;
import org.ec.jap.bo.saap.TarifaBO;
import org.ec.jap.bo.saap.TipoLlaveBO;
import org.ec.jap.entiti.saap.RangoConsumo;
import org.ec.jap.entiti.saap.Tarifa;

@ManagedBean
@ViewScoped
public class TarifaEditBean extends Bean {

	/**
	 * param1->idUsuario
	 */

	@EJB
	TarifaBO tarifaBO;

	@EJB
	TipoLlaveBO tipoLlaveBO;

	@EJB
	RangoConsumoBO rangoConsumoBO;

	private Tarifa tarifa;
	private List<RangoConsumo> rangoConsumos;

	public TarifaEditBean() {
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
				tarifa = new Tarifa();
				redisplayAction(2, false);
			} else {
				tarifa = tarifaBO.findByPk(getParam1Integer());
				map.clear();
				map.put("idTarifa", tarifa);
				rangoConsumos = rangoConsumoBO.findAllByNamedQuery("RangoConsumo.findByTarifa", map);
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
				tarifaBO.save(getUsuarioCurrent(), this.tarifa);
				setAccion("UPD");
				setParam1(this.tarifa.getIdTarifa());
			} else {
				tarifaBO.update(getUsuarioCurrent(), tarifa);
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
			map.clear();
			map.put("idTarifa", tarifa);
			Integer num = tarifaBO.findIntegerByNamedQuery("Tarifa.findRangoConsumos", map);
			if (num > 0)
				throw new Exception("Existen rangos configurados con esta tarifa	, no puede eliminar");

			tarifaBO.delete(getUsuarioCurrent(), tarifa);
			displayMessage(Mensaje.deleteMessaje, Mensaje.SEVERITY_INFO);
			return getLastPage().getOutcome() + "?faces-redirect=true";
		} catch (Exception e) {
			displayMessage(e.getMessage(), Mensaje.SEVERITY_FATAL);
			return getPage().getOutcome();
		}
	}

	@Override
	public String insert() {
		setAccion("INS");
		return "rangoConsumoEdit?faces-redirect=true";
	}

	public Tarifa getTarifa() {
		return tarifa;
	}

	public void setTarifa(Tarifa tarifa) {
		this.tarifa = tarifa;
	}

	public List<RangoConsumo> getRangoConsumos() {
		return rangoConsumos;
	}

	public void setRangoConsumos(List<RangoConsumo> rangoConsumos) {
		this.rangoConsumos = rangoConsumos;
	}

}
