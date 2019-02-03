/**
 * 
 */
package org.ec.jap.dao.saap.impl;

import org.ec.jap.dao.saap.CabeceraPlanillaDAO;
import org.ec.jap.dao.sistema.impl.SistemaImple;
import org.ec.jap.entiti.saap.CabeceraPlanilla;

/**
 * Clase de Acceso a Datos de {@link CabeceraPlanilla}
 * 
 * @author Freddy
 * @version {@code 1.0}
 */
public class CabeceraPlanillaDAOImpl extends SistemaImple<CabeceraPlanilla, Integer> implements CabeceraPlanillaDAO {

	public CabeceraPlanillaDAOImpl() {
		super(CabeceraPlanilla.class);
	}

}
