/**
 * 
 */
package org.ec.jap.dao.sistema.impl;

import org.ec.jap.dao.sistema.ComunidadDAO;
import org.ec.jap.entiti.sistema.Comunidad;

/**
 * Clase de Acceso a Datos de {@link Comunidad}
 * 
 * @author Freddy G. Castillo C.
 * @version {@code 1.0}
 */
public class ComunidadDAOImpl extends SistemaImple<Comunidad, Integer> implements ComunidadDAO {

	public ComunidadDAOImpl() {
		super(Comunidad.class);
	}

}
