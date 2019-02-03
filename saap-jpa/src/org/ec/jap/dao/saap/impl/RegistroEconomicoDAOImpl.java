/**
 * 
 */
package org.ec.jap.dao.saap.impl;

import org.ec.jap.dao.saap.RegistroEconomicoDAO;
import org.ec.jap.dao.sistema.impl.SistemaImple;
import org.ec.jap.entiti.saap.RegistroEconomico;

/**
 * Clase de Acceso a Datos de {@link RegistroEconomico}
 * 
 * @author Freddy
 * @version {@code 1.0}
 */
public class RegistroEconomicoDAOImpl extends SistemaImple<RegistroEconomico, Integer> implements RegistroEconomicoDAO {

	public RegistroEconomicoDAOImpl() {
		super(RegistroEconomico.class);
	}

}
