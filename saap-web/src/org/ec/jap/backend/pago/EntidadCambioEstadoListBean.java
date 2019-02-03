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

import org.ec.jap.backend.pagina.Bean;
import org.ec.jap.backend.utilitario.Mensaje;
import org.ec.jap.bo.sistema.EntidadCambioEstadoBO;
import org.ec.jap.bo.sistema.TipoEntidadBO;
import org.ec.jap.entiti.sistema.EntidadCambioEstado;
import org.ec.jap.entiti.sistema.TipoEntidad;

/**
 * @author Freddy G Castillo C
 * 
 */
@ManagedBean
@ViewScoped
public class EntidadCambioEstadoListBean extends Bean {

	@EJB
	EntidadCambioEstadoBO entidadCambioEstadoBO;
	@EJB
	TipoEntidadBO tipoEntidadBO;

	TipoEntidad tipoEntidadPage;

	private List<EntidadCambioEstado> list;

	public EntidadCambioEstadoListBean() {
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
			tipoEntidadPage = tipoEntidadBO.findByPk(getTipoEntidad());
			map = new HashMap<>();
			map.put("tipoEntidad", getTipoEntidad());
			map.put("idDocumento", getIdDocumentoInteger());
			list = entidadCambioEstadoBO.findAllByNamedQuery("EntidadCambioEstado.findByTipoIdDocumento", map);
		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
	}

	public List<EntidadCambioEstado> getList() {
		return list;
	}

	public void setList(List<EntidadCambioEstado> list) {
		this.list = list;
	}

	public TipoEntidad getTipoEntidadPage() {
		return tipoEntidadPage;
	}

	public void setTipoEntidadPage(TipoEntidad tipoEntidadPage) {
		this.tipoEntidadPage = tipoEntidadPage;
	}

}
