/**
 * 
 */
package org.ec.jap.dao.saap.impl;

import org.ec.jap.dao.saap.LibroDiarioDAO;
import org.ec.jap.dao.sistema.impl.SistemaImple;
import org.ec.jap.entiti.saap.LibroDiario;

/**
 * Clase de Acceso a Datos de {@link LibroDiario}
 * 
 * @author Freddy
 * @version {@code 1.0}
 */
public class LibroDiarioDAOImpl extends SistemaImple<LibroDiario, Integer> implements LibroDiarioDAO {

	public LibroDiarioDAOImpl() {
		super(LibroDiario.class);
	}

}
