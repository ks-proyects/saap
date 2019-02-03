/**
 * 
 */
package org.ec.jap.dao.saap.impl;

import org.ec.jap.dao.saap.ParentescoDAO;
import org.ec.jap.dao.sistema.impl.SistemaImple;
import org.ec.jap.entiti.saap.Parentesco;

/**
 * Clase de Acceso a Datos de {@link Parentesco}
 * 
 * @author Freddy
 * @version {@code 1.0}
 */
public class ParentescoDAOImpl extends SistemaImple<Parentesco, Integer> implements ParentescoDAO {

	public ParentescoDAOImpl() {
		super(Parentesco.class);
	}

}
