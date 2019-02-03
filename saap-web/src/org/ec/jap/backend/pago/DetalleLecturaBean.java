package org.ec.jap.backend.pago;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.ec.jap.backend.pagina.Bean;
import org.ec.jap.backend.utilitario.Mensaje;
import org.ec.jap.bo.saap.CabeceraPlanillaBO;
import org.ec.jap.bo.saap.LecturaBO;
import org.ec.jap.entiti.saap.CabeceraPlanilla;
import org.ec.jap.entiti.saap.Lectura;

/**
 * @author Freddy
 * 
 */
@ManagedBean
@ViewScoped
public class DetalleLecturaBean extends Bean {

	@EJB
	LecturaBO lecturaBO;

	@EJB
	CabeceraPlanillaBO cabeceraPlanillaBO;
	private Lectura lectura;

	private CabeceraPlanilla cp;

	public DetalleLecturaBean() {
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
			cp = cabeceraPlanillaBO.findByPk(getParam2Integer());
			lectura = lecturaBO.findByPk(getParam5Integer());
			Boolean mostrar = "ING".equalsIgnoreCase(cp.getEstado()) && ("ING".equalsIgnoreCase(lectura.getEstado()) || "NOPAG".equalsIgnoreCase(lectura.getEstado()));
			redisplayAction(11, mostrar);
			setNivelBloqueo(mostrar ? 0 : 1);
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	@Override
	public String guardar() {
		try {
			if (lectura.getLecturaIngresada() < lectura.getLecturaAnterior()) {
				throw new Exception("La lectura ingresada no puede ser menor que la lectura anterior.");
			}
			cabeceraPlanillaBO.recalcularPlanilla(getUsuarioCurrent(), cp, lectura);
			displayMessage(Mensaje.procesadoMessaje, Mensaje.SEVERITY_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
		return super.guardar();
	}

	/**
	 * @return the lectura
	 */
	public Lectura getLectura() {
		return lectura;
	}

	/**
	 * @param lectura
	 *            the lectura to set
	 */
	public void setLectura(Lectura lectura) {
		this.lectura = lectura;
	}

	public CabeceraPlanilla getCp() {
		return cp;
	}

	public void setCp(CabeceraPlanilla cp) {
		this.cp = cp;
	}

}
