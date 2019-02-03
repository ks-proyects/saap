package org.ec.jap.backend.pago;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.ec.jap.backend.pagina.Bean;
import org.ec.jap.backend.utilitario.Mensaje;

@ManagedBean
@ViewScoped
public class CambioEstadoMotivoBean extends Bean {

	private String motivo;

	public CambioEstadoMotivoBean() {
		super();
	}

	@PostConstruct
	@Override
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
	public String guardar() {
		try {
			cambioEstadoBO.cambiarEstado((Integer) getIdCambioEstado(), getUsuarioCurrent(), getIdDocumentoEntidad(), motivo);
			init();
			displayMessage(Mensaje.defaultMessaje, Mensaje.SEVERITY_INFO);
		} catch (Exception e) {
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
			e.printStackTrace();
		}
		return getPage().getOutcome();
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

}
