/**
 * 
 */
package org.ec.jap.backend.pago;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;

import org.ec.jap.backend.pagina.Bean;
import org.ec.jap.backend.utilitario.Mensaje;
import org.ec.jap.bo.saap.PeriodoPagoBO;
import org.ec.jap.bo.sistema.BackupDBBO;
import org.ec.jap.bo.sistema.EmailBO;
import org.ec.jap.entiti.saap.PeriodoPago;
import org.ec.jap.utilitario.Utilitario;

/**
 * @author Freddy G Castillo C
 * 
 */
@ManagedBean
@ViewScoped
public class PeriodoPagoListBean extends Bean {

	@EJB
	PeriodoPagoBO periodoPagoBO;
	@EJB
	BackupDBBO backup;

	@EJB
	EmailBO emailBo;

	private List<PeriodoPago> periodoPagos;
	private String filtro;

	public PeriodoPagoListBean() {
		super();
	}

	@PostConstruct
	public void init() {
		try {
			super.init();
			search(null);
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	public void search(ActionEvent event) {
		try {
			map = new HashMap<>(0);
			map.put("filtro", filtro == null ? "%" : filtro);
			periodoPagos = periodoPagoBO.findAllByNamedQuery("PeriodoPago.findByAnio", map);
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@Override
	public String guardar() {
		try {
			String pathFile = backup.executeCommand();
			emailBo.enviarBackupDB(pathFile);
			return getPage().getOutcome();
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
			return "";
		}
	}

	public void changeName(AjaxBehaviorEvent event) {
		try {
			map.put("filtro", filtro == null ? "%" : filtro);
			periodoPagos = periodoPagoBO.findAllByNamedQuery("PeriodoPago.findByAnio", map);
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	@Override
	public String insert() {
		try {
			Date fechaInicio = (Date) periodoPagoBO.findObjectByNamedQuery("PeriodoPago.findMaxMes");
			Boolean puedeAgregarUnPeriodo = Utilitario.puedeAgregarNuevoPeriodo(fechaInicio);
			if (!puedeAgregarUnPeriodo) {
				displayMessage("Ya existe un periodo creado para el mes anterior.", Mensaje.SEVERITY_INFO);
				return "periodoPagoList";
			} else {
				setAccion("INS");
				return "periodoPagoEdit?faces-redirect=true";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public List<PeriodoPago> getPeriodoPagos() {
		return periodoPagos;
	}

	public void setPeriodoPagos(List<PeriodoPago> periodoPagos) {
		this.periodoPagos = periodoPagos;
	}

	public String getFiltro() {
		return filtro;
	}

	public void setFiltro(String filtro) {
		this.filtro = filtro;
	}

}
