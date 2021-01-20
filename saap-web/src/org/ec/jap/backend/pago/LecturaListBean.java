/**
 * 
 */
package org.ec.jap.backend.pago;

import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;

import org.ec.jap.backend.pagina.Bean;
import org.ec.jap.backend.utilitario.Mensaje;
import org.ec.jap.bo.saap.LecturaBO;
import org.ec.jap.bo.saap.PeriodoPagoBO;
import org.ec.jap.entiti.saap.Lectura;
import org.ec.jap.entiti.saap.PeriodoPago;

/**
 * @author Freddy G Castillo C
 * 
 */
@ManagedBean
@ViewScoped
public class LecturaListBean extends Bean {

	@EJB
	LecturaBO lecturaBO;

	@EJB
	PeriodoPagoBO periodoPagoBO;

	private String filtro;
	private List<Lectura> lecturas;
	private PeriodoPago periodoPago;
	private String lecturasSelect;

	public LecturaListBean() {
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
			periodoPago = periodoPagoBO.findByNamedQuery("PeriodoPago.findAbierto");
			if (periodoPago != null && "CERR".equals(periodoPago.getEstado())) {
				// redisplayAction(7, false);
				setNivelBloqueo(1);
			}

			map = new HashMap<>();
			map.put("filtro", filtro != null ? filtro : "");
			map.put("p", periodoPago);
			lecturas = lecturaBO.findAllByNamedQuery("Lectura.findByPer", map);
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	public void changeName(AjaxBehaviorEvent event) {
		try {
			map.put("filtro", filtro != null ? filtro : "");
			lecturas = lecturaBO.findAllByNamedQuery("Lectura.findByPer", map);
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	public void setDisabled() {
		Integer idLec = Integer.valueOf(lecturasSelect);
		for (Lectura lectura : lecturas) {
			if (lectura.getIdLectura().equals(idLec)) {
				lectura.setDisabled(!lectura.getDisabled());
				break;
			}
		}
	}

	@Override
	public String guardar() {
		try {
			String mensaje = "";
			if (periodoPago != null && "CERR".equals(periodoPago.getEstado())) {
				mensaje = lecturaBO.guardarLecturasCerradas(getUsuarioCurrent(), lecturas);
			} else {
				mensaje = lecturaBO.guardarLecturas(getUsuarioCurrent(), lecturas, periodoPago);
			}
			lecturas = lecturaBO.findAllByNamedQuery("Lectura.findByPer", map);
			if (!mensaje.isEmpty())
				displayMessage(mensaje, Mensaje.SEVERITY_WARN);
			displayMessage(Mensaje.defaultMessaje, Mensaje.SEVERITY_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
		return super.guardar();
	}

	public List<Lectura> getLecturas() {
		return lecturas;
	}

	public void setLecturas(List<Lectura> lecturas) {
		this.lecturas = lecturas;
	}

	public PeriodoPago getPeriodoPago() {
		return periodoPago;
	}

	public void setPeriodoPago(PeriodoPago periodoPago) {
		this.periodoPago = periodoPago;
	}

	public String getFiltro() {
		return filtro;
	}

	public void setFiltro(String filtro) {
		this.filtro = filtro;
	}

	public String getLecturasSelect() {
		return lecturasSelect;
	}

	public void setLecturasSelect(String lecturasSelect) {
		this.lecturasSelect = lecturasSelect;
	}

}
