/**
 * 
 */
package org.ec.jap.dao.saap.impl;

import org.ec.jap.dao.saap.LlaveDAO;
import org.ec.jap.dao.sistema.impl.SistemaImple;
import org.ec.jap.entiti.saap.Llave;

/**
 * Clase de Acceso a Datos de {@link Llave}
 * 
 * @author Freddy
 * @version {@code 1.0}
 */
public class LlaveDAOImpl extends SistemaImple<Llave, Integer> implements LlaveDAO {

	public LlaveDAOImpl() {
		super(Llave.class);
	}

}
