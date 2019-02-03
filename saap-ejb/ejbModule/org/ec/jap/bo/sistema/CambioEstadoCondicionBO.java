package org.ec.jap.bo.sistema;

import javax.ejb.Local;

import org.ec.jap.dao.sistema.CambioEstadoCondicionDAO;
import org.ec.jap.entiti.sistema.CambioEstado;

@Local
public interface CambioEstadoCondicionBO extends CambioEstadoCondicionDAO {

	/**
	 * Metodo que verifica que se cumpla una condicion dinamica
	 * 
	 * @param cambioEstado
	 * @throws Exception
	 */
	public void cumpleCondicion(CambioEstado cambioEstado,Object idDocument) throws Exception;
}
