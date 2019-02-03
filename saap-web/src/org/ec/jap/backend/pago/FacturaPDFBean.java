package org.ec.jap.backend.pago;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.ec.jap.backend.pagina.Bean;
import org.ec.jap.backend.utilitario.Mensaje;

@ManagedBean
@ViewScoped
public class FacturaPDFBean extends Bean {

	private String nombre;

	public FacturaPDFBean() {
		try {

		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
