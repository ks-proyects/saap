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
import org.ec.jap.bo.saap.RegistroEconomicoBO;
import org.ec.jap.entiti.saap.RegistroEconomico;

/**
 * @author Freddy G Castillo C
 * 
 */
@ManagedBean
@ViewScoped
public class RegistroEconomicoListBean extends Bean {

	@EJB
	RegistroEconomicoBO registroEconomicoBO;

	private String filtro;
	private List<RegistroEconomico> registroEconomicos;

	public RegistroEconomicoListBean() {
		super();
	}

	@PostConstruct
	public void init() {
		try {
			super.init();
			search(null);
			redisplayAction(2, true, "CUO".equals(getParam20String())?"Cuota":"Multa");
			
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	public void search(ActionEvent event) {
		try {
			map= new HashMap<>();
			map.put("tipoRegistro", getParam20String());
			map.put("filtro", filtro == null ? "%" : filtro);
			registroEconomicos = registroEconomicoBO.findAllByNamedQuery("RegistroEconomico.findByTiporegistro",map);
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}
	public void changeName(AjaxBehaviorEvent event) {
		try {
			map.put("tipoRegistro", getParam20String());
			map.put("filtro", filtro == null ? "%" : filtro);
			registroEconomicos = registroEconomicoBO.findAllByNamedQuery("RegistroEconomico.findByTiporegistro",map);
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	@Override
	public String insert() {
		setAccion("INS");
		return "registroEconomicoEdit?faces-redirect=true";
	}

	public List<RegistroEconomico> getRegistroEconomicos() {
		return registroEconomicos;
	}

	public void setRegistroEconomicos(List<RegistroEconomico> registroEconomicos) {
		this.registroEconomicos = registroEconomicos;
	}

	public String getFiltro() {
		return filtro;
	}

	public void setFiltro(String filtro) {
		this.filtro = filtro;
	}
	

}
