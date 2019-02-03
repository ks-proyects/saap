/**
 * 
 */
package org.ec.jap.backend.sistema;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.ec.jap.backend.pagina.Bean;
import org.ec.jap.backend.utilitario.Mensaje;
import org.ec.jap.bo.sistema.RolBO;
import org.ec.jap.entiti.sistema.Rol;

/**
 * @author Freddy G Castillo C
 * 
 */
@ManagedBean
@ViewScoped
public class RolListBean extends Bean {

	@EJB
	RolBO rolBO;

	private List<Rol> rols;

	public RolListBean() {
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
			rols = rolBO.findAll();
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	@Override
	public String insert() {
		setAccion("INS");
		return "rolEdit?faces-redirect=true";
	}

	public List<Rol> getRols() {
		return rols;
	}

	public void setRols(List<Rol> rols) {
		this.rols = rols;
	}

}
