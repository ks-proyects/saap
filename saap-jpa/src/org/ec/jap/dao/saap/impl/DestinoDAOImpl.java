/**
 * 
 */
package org.ec.jap.dao.saap.impl;

import org.ec.jap.dao.saap.DestinoDAO;
import org.ec.jap.dao.sistema.impl.SistemaImple;
import org.ec.jap.entiti.saap.Destino;

/**
 * Clase de Acceso a Datos de {@link Destino}
 * 
 * @author Freddy
 * @version {@code 1.0}
 */
public class DestinoDAOImpl extends SistemaImple<Destino, Integer> implements DestinoDAO {

	public DestinoDAOImpl() {
		super(Destino.class);
	}

}
