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
import org.ec.jap.bo.saap.LecturaBO;
import org.ec.jap.bo.saap.LlaveBO;
import org.ec.jap.bo.saap.PeriodoPagoBO;
import org.ec.jap.bo.saap.TipoRegistroBO;
import org.ec.jap.entiti.saap.Lectura;
import org.ec.jap.entiti.saap.PeriodoPago;
import org.ec.jap.utilitario.Utilitario;

@ManagedBean
@ViewScoped
public class LecturaEditBean extends Bean {

	/**
	 * 
	 */
	@EJB
	LecturaBO lecturaBO;

	@EJB
	TipoRegistroBO tipoRegistroBO;

	@EJB
	LlaveBO llaveBO;

	@EJB
	PeriodoPagoBO periodoPagoBO;

	private Lectura lectura;
	private PeriodoPago peridoPago;
	private Lectura lecturaAnterior;
	private Boolean isInicialLectura;

	public LecturaEditBean() {
		super();
	}

	@PostConstruct
	@Override
	public void init() {
		try {
			super.init();
			setTipoEntidad("LEC");
			search(null);
			redisplayActions(lectura.getEstado(), lectura.getIdLectura());
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}

	}

	@Override
	public void search(ActionEvent event) {
		try {
			if ("INS".equals(getAccion())) {
				lectura = new Lectura();
				peridoPago = periodoPagoBO.findByNamedQuery("PeriodoPago.findAbierto");
			} else {
				lectura = lecturaBO.findByPk(getParam3Integer());
				peridoPago = lectura.getIdPeriodoPago();
			}
			if (peridoPago != null) {
				map = new HashMap<>();
				Calendar calendar= Calendar.getInstance();
				calendar.setTime(peridoPago.getFechaInicio());
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				map.put("mes", calendar.get(Calendar.MONTH));
				map.put("anio", calendar.get(Calendar.YEAR));
				map.put("llave", llaveBO.findByPk(getParam2Integer()));
				lecturaAnterior = lecturaBO.findByNamedQuery("Lectura.findByAnioAndMes", map);
				isInicialLectura=lecturaAnterior==null;
				lectura.setLecturaAnterior(lecturaAnterior!=null?lecturaAnterior.getLecturaIngresada():lectura.getLecturaAnterior());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	@Override
	public String guardar() {
		try {

			if (peridoPago == null) {
				displayMessage("Primero debe tener un periodo abierto.", Mensaje.SEVERITY_WARN);
				return "";
			}
			lectura.setDescripcion("Lectura del mes de " + Utilitario.mes(peridoPago.getMes()) + "-" + peridoPago.getAnio().toString());
			lectura.setIdPeriodoPago(peridoPago);
			if (lecturaAnterior != null&&lecturaAnterior.getLecturaAnterior()!=null&&!lecturaAnterior.getLecturaAnterior().equals(0.0)) {
				lectura.setLecturaAnterior(lecturaAnterior.getLecturaIngresada());	
			}
			lectura.setMetros3(lectura.getLecturaIngresada()-lectura.getLecturaAnterior());

			map = new HashMap<>();
			map.put("idPeriodoPago", peridoPago);
			map.put("idLlave", getParam2Integer());

			if ("INS".equals(getAccion())) {
				if (lecturaBO.findByNamedQuery("Lectura.findByPeridoAndLlave", map) != null) {
					displayMessage("No puede asignar mas de dos lecturas en un mismo periodo.", Mensaje.SEVERITY_WARN);
					return "";
				}
				lectura.setFechaRegistro(Calendar.getInstance().getTime());
				lectura.setTipoRegistro(tipoRegistroBO.findByPk("CONS"));
				lectura.setIdLlave(llaveBO.findByPk(getParam2Integer()));
				lectura.setEstado("ING");
				lecturaBO.save(getUsuarioCurrent(), lectura);
				setParam3(lectura.getIdLectura());
				setAccion("UPD");
				cambioEstadoBO.cambiarEstado(1, getUsuarioCurrent(), lectura.getIdLectura());
			} else {
				lecturaBO.update(getUsuarioCurrent(), lectura);
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
		// TODO Auto-generated method stub
		try {

			lecturaBO.delete(getUsuarioCurrent(), lectura);
			displayMessage(Mensaje.deleteMessaje, Mensaje.SEVERITY_INFO);
			return getLastPage().getOutcome() + "?faces-redirect=true";
		} catch (Exception e) {
			displayMessage(Mensaje.errorMessaje, Mensaje.SEVERITY_FATAL);
			return getPage().getOutcome();
		}

	}

	public PeriodoPago getPeridoPago() {
		return peridoPago;
	}

	public void setPeridoPago(PeriodoPago peridoPago) {
		this.peridoPago = peridoPago;
	}

	public Lectura getLectura() {
		return lectura;
	}

	public void setLectura(Lectura lectura) {
		this.lectura = lectura;
	}

	public Lectura getLecturaAnterior() {
		return lecturaAnterior;
	}

	public void setLecturaAnterior(Lectura lecturaAnterior) {
		this.lecturaAnterior = lecturaAnterior;
	}

	public Boolean getIsInicialLectura() {
		return isInicialLectura;
	}

	public void setIsInicialLectura(Boolean isInicialLectura) {
		this.isInicialLectura = isInicialLectura;
	}
	
	

}
