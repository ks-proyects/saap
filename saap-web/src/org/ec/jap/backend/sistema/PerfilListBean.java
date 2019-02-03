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
import org.ec.jap.bo.sistema.PerfilBO;
import org.ec.jap.entiti.sistema.Perfil;

/**
 * @author Freddy G Castillo C
 * 
 */
@ManagedBean
@ViewScoped
public class PerfilListBean extends Bean {

	@EJB
	PerfilBO perfilBO;

	private String filtro;

	private List<Perfil> perfils;

	public PerfilListBean() {
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
			map.put("descripcion", filtro != null ? filtro : "");
			perfils = perfilBO.findAllByNamedQuery("Perfil.findByFilter", map);
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	public void changeName(AjaxBehaviorEvent event) {
		try {
			map.put("descripcion", filtro != null ? filtro : "");
			perfils = perfilBO.findAllByNamedQuery("Perfil.findByFilter", map);
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	@Override
	public String insert() {
		setAccion("INS");
		return "perfilEdit?faces-redirect=true";
	}

	public String getFiltro() {
		return filtro;
	}

	public void setFiltro(String filtro) {
		this.filtro = filtro;
	}

	public List<Perfil> getPerfils() {
		return perfils;
	}

	public void setPerfils(List<Perfil> perfils) {
		this.perfils = perfils;
	}

}
