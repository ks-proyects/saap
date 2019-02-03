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
import org.ec.jap.bo.saap.CabeceraPlanillaBO;
import org.ec.jap.entiti.saap.CabeceraPlanilla;

/**
 * @author Freddy G Castillo C
 * 
 */
@ManagedBean
@ViewScoped
public class PlanillaListBean extends Bean {

	@EJB
	CabeceraPlanillaBO cabeceraPlanillaBO;

	private List<CabeceraPlanilla> cabeceraPlanillas;

	private String filtro;

	public PlanillaListBean() {
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
			map.put("filtro", filtro != null ? filtro : "%");
			map.put("filtro", filtro != null ? filtro : "%");
			cabeceraPlanillas = cabeceraPlanillaBO.findAllByNamedQuery("CabeceraPlanilla.findByFilters", map);
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	public void changeName(AjaxBehaviorEvent event) {
		try {

			map.put("filtro", filtro != null ? filtro : "%");
			map.put("filtro", filtro != null ? filtro : "%");
			cabeceraPlanillas = cabeceraPlanillaBO.findAllByNamedQuery("CabeceraPlanilla.findByFilters", map);
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	public List<CabeceraPlanilla> getCabeceraPlanillas() {
		return cabeceraPlanillas;
	}

	public void setCabeceraPlanillas(List<CabeceraPlanilla> cabeceraPlanillas) {
		this.cabeceraPlanillas = cabeceraPlanillas;
	}

	public String getFiltro() {
		return filtro;
	}

	public void setFiltro(String filtro) {
		this.filtro = filtro;
	}

}
