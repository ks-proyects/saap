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
import org.ec.jap.bo.saap.TipoLlaveBO;
import org.ec.jap.entiti.saap.TipoLlave;

/**
 * @author Freddy G Castillo C
 * 
 */
@ManagedBean
@ViewScoped
public class TipoLlaveListBean extends Bean {

	/**
	 * 
	 */
	@EJB
	TipoLlaveBO tipoLlaveBO;

	private List<TipoLlave> tipoLlaves = new ArrayList<>();

	public TipoLlaveListBean() {
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
			tipoLlaves = tipoLlaveBO.findAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String insert() {
		setAccion("INS");
		return "tipoLlaveEdit?faces-redirect=true";
	}

	public List<TipoLlave> getTipoLlaves() {
		return tipoLlaves;
	}

	public void setTipoLlaves(List<TipoLlave> tipoLlaves) {
		this.tipoLlaves = tipoLlaves;
	}

}
