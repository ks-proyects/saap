/**
 * 
 */
package org.ec.jap.backend.pago;

import java.util.Calendar;
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
import org.ec.jap.bo.saap.ServicioBO;
import org.ec.jap.bo.sistema.FiltroBO;
import org.ec.jap.entiti.saap.Servicio;
import org.ec.jap.entiti.sistema.Filtro;

/**
 * @author Freddy G Castillo C
 * 
 */
@ManagedBean
@ViewScoped
public class LlaveListBean extends Bean {

	@EJB
	ServicioBO llaveBO;

	@EJB
	FiltroBO filtroBO;

	Filtro filtro;

	private List<Servicio> listLlaves;

	public LlaveListBean() {
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
			setNombreArchivo("Llaves-" + Calendar.getInstance().get(Calendar.YEAR));
			map = new HashMap<>();
			filtro = filtroBO.getFiltro(getUsuarioCurrent(), "FILTRO", getPage(), filtro != null ? filtro.getValorCadena() : "",filtro,false);
			map.put("filtro", filtro.getValorCadena());
			listLlaves = llaveBO.findAllByNamedQuery("Servicio.findAllByUser", map);
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}
	public void changeName(AjaxBehaviorEvent event) {
		try {
			map.put("filtro", filtro != null ? filtro : "");
			listLlaves = llaveBO.findAllByNamedQuery("Servicio.findAllByUser", map);
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	

	/**
	 * @return the filtro
	 */
	public Filtro getFiltro() {
		return filtro;
	}

	/**
	 * @param filtro the filtro to set
	 */
	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
	}

	public List<Servicio> getListLlaves() {
		return listLlaves;
	}

	public void setListLlaves(List<Servicio> listLlaves) {
		this.listLlaves = listLlaves;
	}

}
