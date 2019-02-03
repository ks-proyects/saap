package org.ec.jap.entiti.dto;

import org.ec.jap.entiti.saap.CabeceraPlanilla;
import org.ec.jap.entiti.saap.DetallePlanilla;
import org.ec.jap.entiti.saap.Usuario;

public class UsuarioPagoDTO {

	private Usuario usuario;
	private Boolean seleccionado;
	private String estadoDescripcion;
	private CabeceraPlanilla cabeceraPlanilla;
	private DetallePlanilla detallePlanilla;

	public UsuarioPagoDTO() {
		// TODO Auto-generated constructor stub
	}

	public UsuarioPagoDTO(Usuario usuario, Boolean seleccionado, String estadoDescripcion) {
		super();
		this.usuario = usuario;
		this.seleccionado = seleccionado;
		this.estadoDescripcion = estadoDescripcion;
	}

	/**
	 * Atributo usuario
	 * 
	 * @return el usuario
	 */
	public Usuario getUsuario() {
		return usuario;
	}

	/**
	 * El @param usuario define usuario
	 */
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	/**
	 * Atributo seleccionado
	 * 
	 * @return el seleccionado
	 */
	public Boolean getSeleccionado() {
		return seleccionado;
	}

	/**
	 * El @param seleccionado define seleccionado
	 */
	public void setSeleccionado(Boolean seleccionado) {
		this.seleccionado = seleccionado;
	}

	/**
	 * Atributo estadoDescripcion
	 * 
	 * @return el estadoDescripcion
	 */
	public String getEstadoDescripcion() {
		return estadoDescripcion;
	}

	/**
	 * El @param estadoDescripcion define estadoDescripcion
	 */
	public void setEstadoDescripcion(String estadoDescripcion) {
		this.estadoDescripcion = estadoDescripcion;
	}

	/**
	 * Atributo cabeceraPlanilla
	 * 
	 * @return el cabeceraPlanilla
	 */
	public CabeceraPlanilla getCabeceraPlanilla() {
		return cabeceraPlanilla;
	}

	/**
	 * El @param cabeceraPlanilla define cabeceraPlanilla
	 */
	public void setCabeceraPlanilla(CabeceraPlanilla cabeceraPlanilla) {
		this.cabeceraPlanilla = cabeceraPlanilla;
	}

	/**
	 * Atributo detallePlanilla
	 * 
	 * @return el detallePlanilla
	 */
	public DetallePlanilla getDetallePlanilla() {
		return detallePlanilla;
	}

	/**
	 * El @param detallePlanilla define detallePlanilla
	 */
	public void setDetallePlanilla(DetallePlanilla detallePlanilla) {
		this.detallePlanilla = detallePlanilla;
	}

}
