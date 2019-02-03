/**
 * 
 */
package org.ec.jap.dao.sistema.impl;

import org.ec.jap.dao.sistema.TipoFiltroDAO;
import org.ec.jap.entiti.sistema.TipoFiltro;

/**
 * Clase de Acceso a Datos de {@link TipoFiltro}
 * 
 * @author Freddy G. Castillo C.
 * @version {@code 1.0}
 */
public class TipoFiltroDAOImpl extends SistemaImple<TipoFiltro, String> implements TipoFiltroDAO {

	public TipoFiltroDAOImpl() {
		super(TipoFiltro.class);
	}

}
