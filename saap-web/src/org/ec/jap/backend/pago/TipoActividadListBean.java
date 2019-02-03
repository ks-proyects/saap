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
import org.ec.jap.bo.saap.TipoActividadBO;
import org.ec.jap.entiti.saap.TipoActividad;

/**
 * @author Freddy G Castillo C
 * 
 */
@ManagedBean
@ViewScoped
public class TipoActividadListBean extends Bean {

	@EJB
	TipoActividadBO tipoActividadBO;

	private List<TipoActividad> listTipoActividad;

	public TipoActividadListBean() {
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
	public String insert() {
		setAccion("INS");
		return "tipoActividadEdit?faces-redirect=true";
	}
	public void search(ActionEvent event) {
		try {
			listTipoActividad = tipoActividadBO.findAllByNamedQuery(
					"TipoActividad.findAll", null);
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	public List<TipoActividad> getListTipoActividad() {
		return listTipoActividad;
	}

	public void setListTipoActividad(List<TipoActividad> listTipoActividad) {
		this.listTipoActividad = listTipoActividad;
	}

}
