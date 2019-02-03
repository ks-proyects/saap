/**
 * 
 */
package org.ec.jap.backend.pago;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.ec.jap.backend.pagina.Bean;
import org.ec.jap.backend.utilitario.Mensaje;
import org.ec.jap.bo.saap.EstadoCivilBO;
import org.ec.jap.entiti.saap.EstadoCivil;

/**
 * @author Freddy G Castillo C
 * 
 */
@ManagedBean
@ViewScoped
public class EstadoCivilListBean extends Bean {

	/**
	 * 
	 */
	@EJB
	EstadoCivilBO estadoCivilBO;

	private List<EstadoCivil> estadoCivils = new ArrayList<>();

	public EstadoCivilListBean() {
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

	@Override
	public void search(ActionEvent event) {
		try {
			estadoCivils = estadoCivilBO.findAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String insert() {
		setAccion("INS");
		return "estadoCivilEdit?faces-redirect=true";
	}

	public List<EstadoCivil> getEstadoCivils() {
		return estadoCivils;
	}

	public void setEstadoCivils(List<EstadoCivil> estadoCivils) {
		this.estadoCivils = estadoCivils;
	}

}
