/**
 * 
 */
package org.ec.jap.dao.saap.impl;

import org.ec.jap.dao.saap.AsientoDAO;
import org.ec.jap.dao.sistema.impl.SistemaImple;
import org.ec.jap.entiti.saap.Asiento;

/**
 * Clase de Acceso a Datos de {@link Asiento}
 * 
 * @author Freddy
 * @version {@code 1.0}
 */
public class AsientoDAOImpl extends SistemaImple<Asiento, Integer> implements AsientoDAO {

	public AsientoDAOImpl() {
		super(Asiento.class);
	}

}
