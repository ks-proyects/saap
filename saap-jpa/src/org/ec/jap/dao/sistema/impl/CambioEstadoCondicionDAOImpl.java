/**
 * 
 */
package org.ec.jap.dao.sistema.impl;

import org.ec.jap.dao.sistema.CambioEstadoCondicionDAO;
import org.ec.jap.entiti.sistema.CambioEstadoCondicion;

/**
 * Clase de Acceso a Datos de {@link CambioEstadoCondicion}
 * 
 * @author Freddy G. Castillo C.
 * @version {@code 1.0}
 */
public class CambioEstadoCondicionDAOImpl extends SistemaImple<CambioEstadoCondicion, Integer> implements CambioEstadoCondicionDAO {

	public CambioEstadoCondicionDAOImpl() {
		super(CambioEstadoCondicion.class);
	}

}
