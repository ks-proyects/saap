/**
 * 
 */
package org.ec.jap.dao.sistema.impl;

import org.ec.jap.dao.sistema.ControladorDAO;
import org.ec.jap.entiti.sistema.Controlador;

/**
 * Clase de Acceso a Datos de {@link Controlador}
 * 
 * @author Freddy G. Castillo C.
 * @version {@code 1.0}
 */
public class ControladorDAOImpl extends SistemaImple<Controlador, String> implements ControladorDAO {

	public ControladorDAOImpl() {
		super(Controlador.class);
	}

}
