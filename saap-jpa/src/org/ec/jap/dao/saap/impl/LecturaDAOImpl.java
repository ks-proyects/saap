/**
 * 
 */
package org.ec.jap.dao.saap.impl;

import org.ec.jap.dao.saap.LecturaDAO;
import org.ec.jap.dao.sistema.impl.SistemaImple;
import org.ec.jap.entiti.saap.Lectura;

/**
 * Clase de Acceso a Datos de {@link Lectura}
 * 
 * @author Freddy
 * @version {@code 1.0}
 */
public class LecturaDAOImpl extends SistemaImple<Lectura, Integer> implements LecturaDAO {

	public LecturaDAOImpl() {
		super(Lectura.class);
	}

}
