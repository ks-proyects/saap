/**
 * 
 */
package org.ec.jap.backend.pago;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.ec.jap.backend.pagina.Bean;
import org.ec.jap.backend.utilitario.Mensaje;
import org.ec.jap.bo.saap.DestinoBO;
import org.ec.jap.entiti.saap.Destino;

/**
 * @author Freddy G Castillo C
 * 
 */
@ManagedBean
@ViewScoped
public class DestinoListBean extends Bean {

	@EJB
	DestinoBO destinoBO;

	private List<Destino> listDestinos;

	public DestinoListBean() {
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
			listDestinos = destinoBO.findAll();
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	@Override
	public String insert() {
		setAccion("INS");
		return "destinoEdit?faces-redirect=true";
	}

	public List<Destino> getListDestinos() {
		return listDestinos;
	}

	public void setListDestinos(List<Destino> listDestinos) {
		this.listDestinos = listDestinos;
	}

}
