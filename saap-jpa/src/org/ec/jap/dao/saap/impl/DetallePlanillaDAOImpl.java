/**
 * 
 */
package org.ec.jap.dao.saap.impl;

import org.ec.jap.dao.saap.DetallePlanillaDAO;
import org.ec.jap.dao.sistema.impl.SistemaImple;
import org.ec.jap.entiti.saap.DetallePlanilla;

/**
 * Clase de Acceso a Datos de {@link DetallePlanilla}
 * 
 * @author Freddy
 * @version {@code 1.0}
 */
public class DetallePlanillaDAOImpl extends SistemaImple<DetallePlanilla, Integer> implements DetallePlanillaDAO {

	public DetallePlanillaDAOImpl() {
		super(DetallePlanilla.class);
	}

	
}
