/**
 * 
 */
package org.ec.jap.backend.sistema;

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
import org.ec.jap.bo.saap.ParametroBO;
import org.ec.jap.entiti.saap.Parametro;

/**
 * @author Freddy G Castillo C
 * 
 */
@ManagedBean
@ViewScoped
public class ParametroListBean extends Bean {

	@EJB
	ParametroBO parametroBO;

	private String filtro;

	private List<Parametro> parametrosList;

	public ParametroListBean() {
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
			map = new HashMap<>();
			map.put("filtro", filtro != null ? filtro : "");
			parametrosList = parametroBO.findAllByNamedQuery("Parametro.findAllByFiltro", map);
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}
	public void changeName(AjaxBehaviorEvent event) {
		try {
			map.put("filtro", filtro != null ? filtro : "");
			parametrosList = parametroBO.findAllByNamedQuery("Parametro.findAllByFiltro", map);
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}
	public String getFiltro() {
		return filtro;
	}

	public void setFiltro(String filtro) {
		this.filtro = filtro;
	}

	public List<Parametro> getParametrosList() {
		return parametrosList;
	}

	public void setParametrosList(List<Parametro> parametrosList) {
		this.parametrosList = parametrosList;
	}

	

}
