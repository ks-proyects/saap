/**
 * 
 */
package org.ec.jap.dao.sistema.impl;

import org.ec.jap.dao.sistema.FiltroDAO;
import org.ec.jap.entiti.sistema.Filtro;

/**
 * Clase de Acceso a Datos de {@link Filtro}
 * 
 * @author Freddy G. Castillo C.
 * @version {@code 1.0}
 */
public class FiltroDAOImpl extends SistemaImple<Filtro, Integer> implements FiltroDAO {

	public FiltroDAOImpl() {
		super(Filtro.class);
	}

}
