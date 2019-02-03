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
import org.ec.jap.bo.saap.TarifaBO;
import org.ec.jap.entiti.saap.Tarifa;

/**
 * @author Freddy G Castillo C
 * 
 */
@ManagedBean
@ViewScoped
public class TarifaListBean extends Bean {

	/**
	 * 
	 */
	@EJB
	TarifaBO tarifaBO;

	private List<Tarifa> tarifaList = new ArrayList<>();

	public TarifaListBean() {
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
			tarifaList = tarifaBO.findAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String insert() {
		setAccion("INS");
		return "tarifaEdit?faces-redirect=true";
	}

	public List<Tarifa> getTarifaList() {
		return tarifaList;
	}

	public void setTarifaList(List<Tarifa> tarifaList) {
		this.tarifaList = tarifaList;
	}

}
