/**
 * 
 */
package org.ec.jap.backend.utilitario;

import javax.faces.application.FacesMessage;

/**
 * @author Freddy G Castillo C
 * 
 */
public class Mensaje extends FacesMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Mensaje cuando se guarda exitosamente
	 */
	public final static String saveMessaje;
	public final static String procesadoMessaje;
	public final static String deleteMessaje;
	public final static String infoDeleteMessaje;
	public final static String errorMessaje;
	public final static String defaultMessaje;

	static {
		saveMessaje = "Guardado exitosamente!!";
		deleteMessaje = "Eliminado exitosamente!!";
		infoDeleteMessaje = "No puede eliminar este registro";
		errorMessaje = "A ocurrido un error en la operación";
		defaultMessaje = "Operacón realizada con exito";
		procesadoMessaje = "Procesado Correctamente";
	}
}
