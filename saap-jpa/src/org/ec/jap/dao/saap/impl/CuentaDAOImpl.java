/**
 * 
 */
package org.ec.jap.dao.saap.impl;

import org.ec.jap.dao.saap.CuentaDAO;
import org.ec.jap.dao.sistema.impl.SistemaImple;
import org.ec.jap.entiti.saap.Cuenta;

/**
 * Clase de Acceso a Datos de {@link Cuenta}
 * 
 * @author Freddy
 * @version {@code 1.0}
 */
public class CuentaDAOImpl extends SistemaImple<Cuenta, Integer> implements CuentaDAO {

	public CuentaDAOImpl() {
		super(Cuenta.class);
	}

}
